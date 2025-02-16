package eu.revanox.skyblock.island;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.island.model.SkyBlockIsland;
import eu.revanox.skyblock.island.repository.IslandRepository;
import eu.revanox.skyblock.util.ChatAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class IslandManager {

    SlimePlugin slimePlugin;
    SlimeLoader slimeLoader;
    String islandWorldPrefix = "island_";
    IslandRepository repository;
    Map<UUID, SkyBlockIsland> islands;

    public IslandManager() {
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        if (slimePlugin == null) {
            return;
        }
        this.islands = new HashMap<>();
        this.repository = SkyBlockPlugin.instance().getMongoManager().create(IslandRepository.class);

        for (SkyBlockIsland skyBlockIsland : this.repository.findAll()) {
            islands.put(skyBlockIsland.getUniqueId(), skyBlockIsland);
        }

        this.slimeLoader = slimePlugin.getLoader("mongodb");
    }

    public void saveAll() {
        islands.forEach((_, skyBlockIsland) -> saveIsland(skyBlockIsland));
    }

    public void saveIsland(SkyBlockIsland island) {
        this.repository.save(island);
    }

    public void saveIsland(SkyBlockIsland skyBlockIsland, Player player) {
        this.islands.put(player.getUniqueId(), skyBlockIsland);
        this.repository.save(skyBlockIsland);
    }

    /**
     * @param player
     * @return null if no island is given
     */
    public SkyBlockIsland getIslandByPlayer(Player player) {
        return this.repository.findFirstByOwnerUniqueId(player.getUniqueId());
    }

    /**
     * @param world
     * @return null if no island is given
     */
    public SkyBlockIsland getIslandByWorld(World world) {
        String worldName = world.getName().replaceAll(this.islandWorldPrefix, "");
        UUID ownerUniqueId = UUID.fromString(worldName);
        for (SkyBlockIsland value : this.islands.values()) {
            if (value.getOwnerUniqueId().equals(ownerUniqueId)) {
                return value;
            }
        }
        return null;
    }

    public void createIsland(Player player) {
        SlimeWorld slimeWorld = this.slimePlugin.getWorld("islandTemplateDefault");


        if (slimeWorld == null) {
            throw new NullPointerException("SlimeWorld could not be found..");
        }

        try {
            slimeWorld = slimeWorld.clone(this.islandWorldPrefix + player.getUniqueId(), this.slimeLoader);
            if (slimeWorld == null) {
                throw new NullPointerException("SlimeWorld could not be cloned..");
            }
        } catch (WorldAlreadyExistsException | IOException e) {
            e.printStackTrace();
        }

        CompletableFuture.runAsync(() -> {
            try {
                slimePlugin.loadWorld(slimeLoader, this.islandWorldPrefix + player.getUniqueId(), false, new SlimePropertyMap());
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldLockedException e) {
                throw new RuntimeException(e);
            }
        });

        SlimeWorld finalSlimeWorld = slimeWorld;
        Bukkit.getScheduler().runTask(SkyBlockPlugin.instance(), () -> {
            try {
                slimePlugin.loadWorld(finalSlimeWorld);
            } catch (UnknownWorldException | WorldLockedException | IOException e) {
                throw new RuntimeException(e);
            }

            Location location = new Location(Bukkit.getWorld(this.islandWorldPrefix + player.getUniqueId()), 0.5, 62, 0.5);
            player.teleportAsync(location);
        });

        SkyBlockIsland skyBlockIsland = new SkyBlockIsland();
        skyBlockIsland.setUniqueId(UUID.randomUUID());
        skyBlockIsland.setOwnerUniqueId(player.getUniqueId());
        skyBlockIsland.setLevel(0);
        skyBlockIsland.setIslandSettings(new HashMap<>());
        skyBlockIsland.setBoughtWarpLocation(null);


        this.islands.put(player.getUniqueId(), skyBlockIsland);
        this.repository.save(skyBlockIsland);

        player.sendMessage(ChatAction.of("§7Eine neue Insel wurde für dich erstellt!"));
    }

    public boolean isIslandLoaded(Player player) {
        String worldName = islandWorldPrefix + player.getUniqueId();
        return slimePlugin.getWorld(worldName) != null;
    }

    public boolean islandExists(Player player) {
        return this.repository.findFirstByOwnerUniqueId(player.getUniqueId()) != null && slimePlugin.getWorld(this.islandWorldPrefix + player.getUniqueId()) != null;
    }

    public World loadIsland(Player player) {
        AtomicReference<World> world = new AtomicReference<>(Bukkit.getWorld(this.islandWorldPrefix + player.getUniqueId()));

        if (world.get() != null) return null;

        if (this.repository.findFirstByOwnerUniqueId(player.getUniqueId()) == null) {
            return null;
        }

        String worldName = islandWorldPrefix + player.getUniqueId();
        long millis = System.currentTimeMillis();

        AtomicReference<SlimeWorld> slimeWorldReference = new AtomicReference<>();
        CompletableFuture.runAsync(() -> {
            SlimeWorld slimeWorld;
            try {
                slimeWorld = slimePlugin.loadWorld(slimeLoader, worldName, false, new SlimePropertyMap());
                slimeWorldReference.set(slimeWorld);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldLockedException e) {
                throw new RuntimeException(e);
            }
        }).thenRun(() -> Bukkit.getScheduler().runTask(SkyBlockPlugin.instance(), () -> {
            SlimeWorld slimeWorld = slimeWorldReference.get();
            if (slimeWorld == null) {
                return;
            }
            try {
                slimePlugin.loadWorld(slimeWorld);
                World loadedBukkitWorld = Bukkit.getWorld(this.islandWorldPrefix + player.getUniqueId());
                world.set(loadedBukkitWorld);

                player.sendMessage(ChatAction.of("§7Deine Insel wurde erfolgreich geladen! (§e" + (System.currentTimeMillis() - millis) + " ms§7)"));
            } catch (UnknownWorldException | WorldLockedException | IOException e) {
                throw new RuntimeException(e);
            }
        }));
        return world.get();
    }

    public void teleportToIsland(Player player) {
        World world = Bukkit.getWorld(this.islandWorldPrefix + player.getUniqueId());

        if (world == null && islandExists(player)) {
            CompletableFuture.supplyAsync(() -> this.loadIsland(player))
                    .thenAccept(loadedWorld -> {
                        Location location = new Location(loadedWorld, 0.5, 62, 0.5);
                        player.teleportAsync(location);
                        player.sendMessage(ChatAction.of("§7Du wurdest auf deine Insel teleportiert."));
                    });

        } else if (world != null) {
            Location location = new Location(world, 0.5, 62, 0.5);
            player.teleportAsync(location);
            player.sendMessage(ChatAction.of("§7Du wurdest auf deine Insel teleportiert."));
        } else {
            createIsland(player);
        }
    }

    public void deleteIsland(Player player) {
        if (this.repository.findFirstByOwnerUniqueId(player.getUniqueId()) == null) {
            return;
        }

        SlimeWorld slimeWorld = slimePlugin.getWorld(islandWorldPrefix + player.getUniqueId());
        if (!slimePlugin.getLoadedWorlds().contains(slimeWorld)) {
            return;
        }

        World world = Bukkit.getWorld(slimeWorld.getName());
        if (world == null) {
            return;
        }

        long millis = System.currentTimeMillis();

        SkyBlockIsland skyBlockIsland = this.repository.findFirstByOwnerUniqueId(player.getUniqueId());

        if (!world.getPlayers().isEmpty()) {
            CompletableFuture<Void> completableFuture =
                    CompletableFuture.allOf(world.getPlayers().stream().map(worldPayer ->
                            worldPayer.teleportAsync(SkyBlockPlugin.instance().getLocationManager().getPosition("spawn").getLocation())).toList().toArray(CompletableFuture[]::new));

            completableFuture.thenRun(() -> {
                Bukkit.unloadWorld(world, false);
            });
        } else {
            Bukkit.unloadWorld(world, false);
        }
        try {
            this.slimeLoader.deleteWorld(slimeWorld.getName());
            player.sendMessage(ChatAction.of("§7Insel gelöscht in §e" + (System.currentTimeMillis() - millis) + "§7ms"));

            this.islands.remove(player.getUniqueId());
            this.repository.delete(skyBlockIsland);
        } catch (UnknownWorldException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
