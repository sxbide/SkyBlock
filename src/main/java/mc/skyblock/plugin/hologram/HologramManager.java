package mc.skyblock.plugin.hologram;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.hologram.model.Hologram;
import mc.skyblock.plugin.hologram.repository.HologramRepository;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HologramManager {

    HologramRepository hologramRepository;
    Map<String, Hologram> hologramMap;
    Map<String, UUID> entityIdMap;

    public HologramManager() {
        this.hologramRepository = SkyBlockPlugin.instance().getMongoManager().create(HologramRepository.class);
        this.hologramMap = this.hologramRepository.findAll().stream().collect(Collectors.toMap(Hologram::getName, Function.identity()));
        this.entityIdMap = new ConcurrentHashMap<>();
    }

    public Hologram getHologram(String name) {
        Objects.requireNonNull(name, "Name cannot be null.");

        return this.hologramMap.get(name);
    }

    public Hologram createHologram(String name, Location location, boolean persistent, String... miniMessageLines) {
        Objects.requireNonNull(name, "Name cannot be null.");
        Objects.requireNonNull(location, "Location cannot be null.");
        miniMessageLines = Objects.requireNonNullElse(miniMessageLines, new String[0]);

        if (this.hologramMap.containsKey(name)) {
            throw new IllegalArgumentException("Hologram with name " + name + " already exists.");
        }

        Hologram hologram = new Hologram();
        hologram.setName(name);
        hologram.setLocation(location);
        hologram.setBillboard(TextDisplay.Billboard.FIXED);
        hologram.setBackgroundColor(Color.fromARGB(25, 0, 0, 0));
        hologram.setPersistent(persistent);
        hologram.setLines(Arrays.stream(miniMessageLines).toList());

        this.hologramMap.put(name, hologram);
        this.hologramRepository.save(hologram);

        return hologram;
    }

    public void editHologram(String name, Consumer<Hologram> consumer) {
        Objects.requireNonNull(name, "Name cannot be null.");
        Objects.requireNonNull(consumer, "Consumer cannot be null.");

        Hologram hologram = this.hologramMap.get(name);
        if (hologram == null) {
            throw new IllegalArgumentException("Hologram with name " + name + " does not exist.");
        }
        consumer.accept(hologram);
        this.hologramRepository.save(hologram);
    }

    public void spawnHologram(Hologram hologram) {
        Objects.requireNonNull(hologram, "Hologram cannot be null.");

        TextDisplay textDisplay = hologram.getLocation().getWorld().spawn(hologram.getLocation(), TextDisplay.class);

        textDisplay.text(hologram.getLines().stream().map(MiniMessage.miniMessage()::deserialize).reduce((s1, s2) -> s1.appendNewline().append(s2)).orElse(null));
        textDisplay.setBillboard(hologram.getBillboard());
        textDisplay.setPersistent(false);
        textDisplay.setGravity(false);
        textDisplay.setSeeThrough(false);
        textDisplay.setShadowed(true);
        textDisplay.setDefaultBackground(false);
        textDisplay.setBackgroundColor(hologram.getBackgroundColor());

        this.entityIdMap.put(hologram.getName(), textDisplay.getUniqueId());
    }

    public void removeHologram(Hologram hologram, boolean delete) {
        Objects.requireNonNull(hologram, "Hologram cannot be null.");

        UUID entityId = this.entityIdMap.get(hologram.getName());
        TextDisplay textDisplay = (TextDisplay) hologram.getLocation().getWorld().getEntity(entityId);

        if (textDisplay != null) {
            textDisplay.remove();
        }

        this.hologramMap.remove(hologram.getName());
        this.entityIdMap.remove(hologram.getName());
        if (delete) {
            this.hologramRepository.delete(hologram);
        }
    }

    public void updateHologram(Hologram hologram) {
        UUID entityId = this.entityIdMap.get(hologram.getName());
        TextDisplay textDisplay = (TextDisplay) hologram.getLocation().getWorld().getEntity(entityId);

        if (textDisplay != null) {
            textDisplay.remove();
        }

        this.spawnHologram(hologram);
    }

    public void updateHolograms() {
        this.hologramMap.values().forEach(this::updateHologram);
    }

    public void despawnAll() {
        this.hologramMap.values().forEach(hologram -> this.removeHologram(hologram, false));
    }

}
