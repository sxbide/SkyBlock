package mc.skyblock.plugin.caseopening.animation;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.caseopening.model.CaseItem;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.Rarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CaseOpeningAnimation {

    Player player;
    BlockData caseBlockData;

    public CaseOpeningAnimation(Player player) {



    }

    @SuppressWarnings("unchecked")
    public void start() {
        Location blockLocation = SkyBlockPlugin.instance().getCaseConfiguration().getCaseBlockLocation();
        World world = blockLocation.getWorld();
        this.caseBlockData = world.getBlockAt(blockLocation).getBlockData().clone();

        CompletableFuture<Void> mainBlockFuture = CompletableFuture.runAsync(() -> {
            world.getBlockAt(blockLocation).breakNaturally(false, false);
            player.playSound(blockLocation, Sound.BLOCK_STONE_BREAK, 1.0F, 0.5F);
        });

        CompletableFuture<Void>[] glassBreakFutures = new CompletableFuture[9];
        for (int i = 0, x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++, i++) {
                Location glassBlockLocation = blockLocation.clone().add(x, -1, z);
                int finalX = x;
                int finalZ = z;
                glassBreakFutures[i] = CompletableFuture.runAsync(() -> {
                    Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                        world.getBlockAt(glassBlockLocation).breakNaturally(false, false);
                        player.playSound(glassBlockLocation, Sound.BLOCK_GLASS_BREAK, 1.0F, 0.5F);
                    }, 2L * ((finalX + 1) * 3L + (finalZ + 1)));
                });
            }
        }

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(mainBlockFuture, CompletableFuture.allOf(glassBreakFutures));
        combinedFuture.thenAccept(_ -> {
            Location[] corners = {
                    blockLocation.clone().add(-1, -1, -1),
                    blockLocation.clone().add(1, -1, 1),
                    blockLocation.clone().add(1, -1, -1),
                    blockLocation.clone().add(-1, -1, 1)
            };

            CompletableFuture.runAsync(() -> {
                float pitch = 0.1F;
                for (int i = 0; i < 3; i++) {
                    Rarity randomRarity = Rarity.getRandomRarity();
                    for (int j = 0; j < corners.length; j++) {
                        sendParticleLine(randomRarity, corners[j], corners[(j + 1) % corners.length]);
                    }
                    player.playSound(blockLocation, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, pitch);
                    pitch = Math.min(pitch + 0.4F, 1.0F);
                    try {
                        CompletableFuture<Void> delay = CompletableFuture.runAsync(() -> {}, CompletableFuture.delayedExecutor(20 * 50, TimeUnit.MILLISECONDS));
                        delay.join();
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).thenAccept(_ -> {
                CaseItem winningItem = SkyBlockPlugin.instance().getCaseOpeningManager().getRandomCaseItem();
                CompletableFuture.runAsync(() -> {
                    Location itemLocation = blockLocation.clone().add(0, 1, 0);
                    Item item = world.dropItem(itemLocation, winningItem.getItemStack());
                    item.setGravity(false);
                    item.setCanPlayerPickup(false);
                    item.setCanMobPickup(false);
                    item.setUnlimitedLifetime(true);
                    item.setItemStack(winningItem.getItemStack().clone());
                    item.setGlowing(winningItem.getRarity().getWeight() >= 5);

                    TextDisplay textDisplay = world.createEntity(itemLocation, TextDisplay.class);
                    textDisplay.setSeeThrough(true);
                    Component text = Component.empty().appendNewline()
                            .append(Component.text("§7" + winningItem.getItemStack().getAmount() + "x "))
                            .append(winningItem.getItemStack().getItemMeta().hasDisplayName() ? Objects.requireNonNull(winningItem.getItemStack().getItemMeta().displayName()) : Component.text(winningItem.getItemStack().getType().name()))
                            .appendNewline().append(Component.text("§7Rarität: ").append(winningItem.getRarity().getDisplayName())).appendNewline().appendSpace();
                    textDisplay.text(text);
                    textDisplay.setBillboard(Display.Billboard.VERTICAL);
                    textDisplay.setGravity(false);
                    textDisplay.setDefaultBackground(true);
                    textDisplay.setShadowed(true);
                    textDisplay.setPersistent(false);

                    player.playSound(itemLocation, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    player.getInventory().addItem(winningItem.getItemStack().clone());
                    player.sendMessage(ChatAction.of("Du hast " + winningItem.getItemStack().getAmount() + "x ").append(winningItem.getItemStack().getItemMeta().hasDisplayName() ? Objects.requireNonNull(winningItem.getItemStack().getItemMeta().displayName()) : Component.text(winningItem.getItemStack().getType().name())).append(MiniMessage.miniMessage().deserialize("<#6cd414> aus der Kiste erhalten!")));

                    BukkitTask particleTask = Bukkit.getScheduler().runTaskTimer(SkyBlockPlugin.instance(), () -> {
                        sendParticleLine(winningItem.getRarity(), corners[0], corners[1]);
                        sendParticleLine(winningItem.getRarity(), corners[1], corners[2]);
                        sendParticleLine(winningItem.getRarity(), corners[2], corners[3]);
                        sendParticleLine(winningItem.getRarity(), corners[3], corners[0]);
                    }, 0, 2L);

                    Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.instance(), () -> {
                        particleTask.cancel();
                        item.remove();
                        textDisplay.remove();

                        //Place blocks back
                        world.getBlockAt(blockLocation).setType(SkyBlockPlugin.instance().getCaseConfiguration().getCaseBlockMaterial());
                        world.getBlockAt(blockLocation).setBlockData(caseBlockData);
                        for (int i = 0, x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++, i++) {
                                Location glassBlockLocation = blockLocation.clone().add(x, -1, z);
                                world.getBlockAt(glassBlockLocation).setType(Material.BLUE_STAINED_GLASS);
                            }
                        }
                    }, 20 * 3L);
                });
            });
        });
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
                    .packet(true, x, y, z, 2)
                    .sendTo(Bukkit.getOnlinePlayers());
        }
    }

}
