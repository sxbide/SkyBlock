package eu.revanox.skyblock.inventory;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.conversation.IslandDeletionPrompt;
import eu.revanox.skyblock.island.model.SkyBlockIsland;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.ItemBuilder;
import eu.revanox.skyblock.util.SoundAction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class IslandInventory implements Listener {

    public IslandInventory() {
        Bukkit.getPluginManager().registerEvents(this, SkyBlockPlugin.instance());
    }

    public Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, Component.text("Island"));
        SkyBlockIsland skyBlockIsland = SkyBlockPlugin.instance().getIslandManager().getIslandByPlayer(player);

        ItemBuilder islandItem = ItemBuilder.of(Material.BARREL)
                .displayName(Component.text("§aErste Insel von " + player.getName()))
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum teleportieren>")
                );

        ItemBuilder islandItemNone = ItemBuilder.of(Material.BARRIER)
                .displayName(Component.text("§cDu besitzt keine Insel"))
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum erstellen>")
                );

        ItemBuilder deleteIsland = ItemBuilder.of(Material.BARRIER)
                .displayName(Component.text("§cDeine Insel permanent löschen"))
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum löschen>")
                );


        if (skyBlockIsland == null) {
            inventory.setItem(13, islandItemNone.build());
        } else {

            ItemBuilder warpIsland = ItemBuilder.of(Material.WARPED_HANGING_SIGN)
                    .displayName((skyBlockIsland.getWarpLocation() == null
                            ? Component.text("§aKaufe einen Insel Warp")
                            : Component.text("§aDu besitzt einen Insel Warp")));

            if (skyBlockIsland.getWarpLocation() == null) {
                warpIsland.lore(
                        Component.empty(),
                        Component.text("§7Kosten: §e5.000 ⛃"),
                        Component.empty(),
                        Component.text("§7Mit einem Inselwarp können andere"),
                        Component.text("§7Spieler deine Insel betreten.")
                );
            } else {
                warpIsland.lore(
                        Component.empty(),
                        Component.text("§7Andere Spieler können mit"),
                        Component.text("§7/visit " + player.getName() + " deine Insel betreten."),
                        Component.empty(),
                        Component.text("§c<Shift+Rechtsklicke zum löschen>")
                );
            }

            inventory.setItem(10, islandItem.build());
            inventory.setItem(11, warpIsland.build());
            inventory.setItem(16, deleteIsland.build());
        }

        SoundAction.playInventoryOpen(player);
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (event.getView().title().equals(Component.text("Island"))) {
                event.setCancelled(true);

                SkyBlockIsland skyBlockIsland = SkyBlockPlugin.instance().getIslandManager().getIslandByPlayer(player);
                SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

                if (skyBlockIsland == null) {
                    if (event.getRawSlot() == 13) {
                        SkyBlockPlugin.instance().getIslandManager().createIsland(player);
                    }
                } else {
                    if (event.getRawSlot() == 10) {
                        SkyBlockPlugin.instance().getIslandManager().teleportToIsland(player);
                        return;
                    }

                    if (event.getRawSlot() == 11) {
                        if (skyBlockIsland.getWarpLocation() == null) {
                            if (skyBlockUser.getBalance() >= 5000) {
                                World world = Bukkit.getWorld("island_" + player.getUniqueId());

                                if (!SkyBlockPlugin.instance().getIslandManager().isIslandLoaded(player)) {
                                    player.sendMessage(ChatAction.failure("§cDiese Aktion konnte nicht durchgeführt werden."));
                                    return;
                                }

                                Location location = new Location(world, 0.5, 62, 0.5);
                                skyBlockIsland.setBoughtWarpLocation(location);

                                skyBlockUser.removeBalance(5000);
                                SoundAction.playGoodWork(player);
                                player.sendMessage(ChatAction.of("§aDein Kauf wurde erfolgreich abgeschlossen!"));
                                player.closeInventory();
                                return;
                            }
                            player.sendMessage(ChatAction.failure("§cDazu ist dein Kontostand zu niedrig."));
                            return;
                        } else {
                            if (event.getClick() == ClickType.SHIFT_RIGHT) {
                                skyBlockIsland.setBoughtWarpLocation(null);

                                SoundAction.playGoodWork(player);
                                player.sendMessage(ChatAction.of("§aDie Aktion wurde erfolgreich abgeschlossen!"));
                                player.closeInventory();
                                return;
                            }
                        }
                        return;
                    }

                    if (event.getRawSlot() == 16) {
                        player.closeInventory();
                        ConversationFactory factory = new ConversationFactory(SkyBlockPlugin.instance())
                                .withFirstPrompt(new IslandDeletionPrompt())
                                .withLocalEcho(false)
                                .withEscapeSequence("cancel");
                        Conversation conversation = factory.buildConversation(player);
                        conversation.begin();
                    }
                }
            }
        }
    }
}
