package mc.skyblock.plugin.caseopening.animation;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.caseopening.model.CaseItem;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.CustomSound;
import mc.skyblock.plugin.util.Rarity;
import mc.skyblock.plugin.util.SoundAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class CaseOpeningAnimation {

    Player player;
    World world;
    BlockData caseBlockData;

    public CaseOpeningAnimation(Player player) {
        this.player = player;
    }

    public void start() {
        Location blockLocation = SkyBlockPlugin.instance().getCaseConfiguration().getCaseBlockLocation();
        world = Bukkit.getWorld("CYTOOX");
        blockLocation.setWorld(world);
        this.caseBlockData = world.getBlockAt(blockLocation).getBlockData().clone();

        Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
            world.getBlockAt(blockLocation).setType(Material.AIR);
            player.playSound(blockLocation, Sound.BLOCK_GLASS_BREAK, 1.0F, 0.5F);

            for (int i = 0, x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++, i++) {
                    Location glassBlockLocation = blockLocation.clone().add(x, -1, z);
                    Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                        world.getBlockAt(glassBlockLocation).setType(Material.AIR);
                        player.playSound(glassBlockLocation, Sound.BLOCK_GLASS_BREAK, 1.0F, 0.5F);
                    }, 2L * ((x + 1) * 3L + (z + 1)));
                }
            }

            Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                Logger.getAnonymousLogger().info("All blocks broken");
                Location[] corners = {
                        new Location(world, -43, 84, -102),
                        new Location(world, -43, 84, -105),
                        new Location(world, -46, 84, -105),
                        new Location(world, -46, 84, -102)
                };

                AtomicReference<Float> pitch = new AtomicReference<>(0.1F);
                for (int i = 0; i < 3; i++) {
                    Rarity randomRarity = Rarity.getRandomRarity();
                    for (int j = 0; j < corners.length; j++) {
                        int finalJ = j;
                        Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                            sendParticleLine(randomRarity, corners[finalJ], corners[(finalJ + 1) % corners.length]);
                            player.playSound(blockLocation, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, pitch.get());
                        }, 20L * i);
                    }
                    pitch.updateAndGet(v -> v + 1.0F);
                }
                CaseItem winningItem = SkyBlockPlugin.instance().getCaseOpeningManager().getRandomCaseItem();
                Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                    Location itemLocation = blockLocation.clone().add(0.5, 1, 0.5); //-44.5, 85.5, -103.5
                    Item item = world.dropItem(itemLocation, winningItem.getItemStack());
                    item.setGravity(false);
                    item.setCanPlayerPickup(false);
                    item.setCanMobPickup(false);
                    item.setUnlimitedLifetime(true);
                    item.setItemStack(winningItem.getItemStack().clone());
                    item.setGlowing(winningItem.getRarity().getWeight() >= 5);
                    item.setVelocity(new Vector(0, 0, 0));

                    TextDisplay textDisplay = world.createEntity(itemLocation, TextDisplay.class);
                    textDisplay.setSeeThrough(true);
                    Component text = Component.empty().appendNewline()
                            .append(Component.text("§7" + winningItem.getItemStack().getAmount() + "x "))
                            .append(winningItem.getItemStack().getItemMeta().hasDisplayName() ? Objects.requireNonNull(winningItem.getItemStack().getItemMeta().displayName()) : Component.text(winningItem.getItemStack().getType().name()))
                            .appendNewline().append(Component.text("§7Rarität: ").append(winningItem.getRarity().getDisplayName())).appendNewline().appendNewline().appendSpace();
                    textDisplay.text(text);
                    textDisplay.setBillboard(Display.Billboard.VERTICAL);
                    textDisplay.setGravity(false);
                    textDisplay.setDefaultBackground(false);
                    textDisplay.setBackgroundColor(Color.fromARGB(25, 0, 0, 0));
                    textDisplay.setShadowed(true);
                    textDisplay.setPersistent(false);

                    textDisplay.spawnAt(itemLocation.clone().add(0, 1, 0));
                    item.addPassenger(textDisplay);

                    player.playSound(itemLocation, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.1F);
                    player.getInventory().addItem(winningItem.getItemStack().clone());
                    player.sendMessage(ChatAction.of("Du hast " + winningItem.getItemStack().getAmount() + "x ").append(winningItem.getItemStack().getItemMeta().hasDisplayName() ? Objects.requireNonNull(winningItem.getItemStack().getItemMeta().displayName()) : Component.text(winningItem.getItemStack().getType().name())).append(MiniMessage.miniMessage().deserialize("<#6cd414> aus der Kiste erhalten!")));

                    if (winningItem.getRarity().getWeight() >= 5) {
                        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                            onlinePlayer.sendMessage(ChatAction.of("§7" + player.getName() + " hat " + winningItem.getItemStack().getAmount() + "x ").append(winningItem.getItemStack().getItemMeta().hasDisplayName() ? Objects.requireNonNull(winningItem.getItemStack().getItemMeta().displayName()) : Component.text(winningItem.getItemStack().getType().name())).append(MiniMessage.miniMessage().deserialize(" <#6cd414>aus der Kiste erhalten!")));
                            CustomSound.WINNING.playSound(onlinePlayer, 0.2F, 1F, onlinePlayer.getLocation());
                            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) return;
                            //onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0F, 2.0F);
                        });
                    }

                    BukkitTask particleTask = Bukkit.getScheduler().runTaskTimer(SkyBlockPlugin.instance(), () -> {
                        sendParticleLine(winningItem.getRarity(), corners[0], corners[1]);
                        sendParticleLine(winningItem.getRarity(), corners[1], corners[2]);
                        sendParticleLine(winningItem.getRarity(), corners[2], corners[3]);
                        sendParticleLine(winningItem.getRarity(), corners[3], corners[0]);
                    }, 0, 2L);

                    Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                        particleTask.cancel();
                        Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                            textDisplay.remove();
                            item.remove();

                            //Place blocks back
                            world.getBlockAt(blockLocation).setType(SkyBlockPlugin.instance().getCaseConfiguration().getCaseBlockMaterial());
                            world.getBlockAt(blockLocation).setBlockData(caseBlockData);
                            for (int i = 0, x = -1; x <= 1; x++) {
                                for (int z = -1; z <= 1; z++, i++) {
                                    Location glassBlockLocation = blockLocation.clone().add(x, -1, z);
                                    world.getBlockAt(glassBlockLocation).setType(Material.BLUE_STAINED_GLASS);
                                }
                            }
                        }, 20L);
                    }, 140L);
                }, 60L);
            }, 20L);
        }, 2L);
    }

    private void sendParticleLine(Rarity rarity, Location from, Location to) {
        double distance = from.distance(to);
        double particles = distance / 0.3;
        for (double i = 0; i < particles; i++) {
            double x = from.getX() + (to.getX() - from.getX()) * (i / particles);
            double y = from.getY() + (to.getY() - from.getY()) * (i / particles);
            double z = from.getZ() + (to.getZ() - from.getZ()) * (i / particles);
            SkyBlockPlugin.instance().getParticleAPI().LIST_1_13.DUST_COLOR_TRANSITION
                    .color(rarity.getColor(), rarity.getColor(), 1.0D)
                    .packet(true, new Location(world, x,y,z), 2)
                    .sendTo(Bukkit.getOnlinePlayers());
        }
    }

}
