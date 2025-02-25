package mc.skyblock.plugin.configuration;

import com.google.gson.*;
import mc.skyblock.plugin.configuration.annotation.ConfigPath;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class Configuration {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static <T> T load(String configPath, Class<T> configClass) {
        try {
            JsonObject jsonObject;
            File configFile = new File(configPath);
            File configDir = configFile.getParentFile();
            if (configDir != null && !configDir.exists()) {
                configDir.mkdirs();
            }
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configPath)) {
                    jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                }
            } else {
                jsonObject = new JsonObject();
            }

            T configInstance = GSON.fromJson(jsonObject, configClass);

            for (Field field : configClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigPath.class)) {
                    ConfigPath configPathAnnotation = field.getAnnotation(ConfigPath.class);
                    String[] path = configPathAnnotation.value().split("\\.");
                    JsonElement element = jsonObject;
                    for (String key : path) {
                        if (element.getAsJsonObject().has(key)) {
                            element = element.getAsJsonObject().get(key);
                        } else {
                            element = null;
                            break;
                        }
                    }
                    field.setAccessible(true);
                    if (element != null) {
                        field.set(configInstance, GSON.fromJson(element, field.getType()));
                    } else {
                        // Save default value to JSON
                        JsonObject current = jsonObject;
                        for (int i = 0; i < path.length - 1; i++) {
                            if (!current.has(path[i])) {
                                current.add(path[i], new JsonObject());
                            }
                            current = current.getAsJsonObject(path[i]);
                        }
                        current.add(path[path.length - 1], GSON.toJsonTree(field.get(configInstance)));
                    }
                }
            }

            // Save the updated JSON with default values
            try (FileWriter writer = new FileWriter(configPath)) {
                GSON.toJson(jsonObject, writer);
            }

            return configInstance;
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static <T> void save(String configPath, T configInstance) {
        try {
            JsonObject jsonObject = new JsonObject();

            for (Field field : configInstance.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigPath.class)) {
                    ConfigPath configPathAnnotation = field.getAnnotation(ConfigPath.class);
                    String[] path = configPathAnnotation.value().split("\\.");
                    JsonObject current = jsonObject;
                    for (int i = 0; i < path.length - 1; i++) {
                        if (!current.has(path[i])) {
                            current.add(path[i], new JsonObject());
                        }
                        current = current.getAsJsonObject(path[i]);
                    }
                    field.setAccessible(true);
                    current.add(path[path.length - 1], GSON.toJsonTree(field.get(configInstance)));
                }
            }

            try (FileWriter writer = new FileWriter(configPath)) {
                GSON.toJson(jsonObject, writer);
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Failed to save configuration", e);
        }
    }

}
