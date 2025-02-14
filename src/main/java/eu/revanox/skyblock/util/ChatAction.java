package eu.revanox.skyblock.util;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
@FieldDefaults(makeFinal = true)
public class ChatAction {

    @Getter
    private Component prefixGradient = MiniMessage.miniMessage().deserialize("<gradient:#E2A574:#E0EF50>Blockarion</gradient>");
    private String prefix = "§a✔ §7";
    private String failurePrefix = "§c✘ §c";
    @Getter
    private String noPermission = failurePrefix + "§cDazu hast du keine Berechtigung!";
    @Getter
    private String offline = failurePrefix + "§cDieser Spieler wurde nicht gefunden!";

    public String of(String message) {
        return prefix + message;
    }

    public String failure(String message) {
        return failurePrefix + message;
    }
}

