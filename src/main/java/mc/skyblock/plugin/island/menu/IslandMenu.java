package mc.skyblock.plugin.island.menu;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.conversation.IslandDeletionPrompt;
import mc.skyblock.plugin.island.model.SkyBlockIsland;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.ItemBuilder;
import mc.skyblock.plugin.util.SoundAction;
import mc.skyblock.plugin.util.Util;
import mc.skyblock.plugin.util.custom.CustomSounds;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@Getter
public class IslandMenu implements InventoryProvider {

    RyseInventory ryseInventory;

    public IslandMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Insel")
                .rows(3)
                .provider(this)
                .disableUpdateTask()
                .build(SkyBlockPlugin.instance());
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        Util.defaultInventory(contents);

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
            contents.set(13, IntelligentItem.of(islandItemNone.build(), event -> {
                SkyBlockPlugin.instance().getIslandManager().createIsland(player);
            }));
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

            contents.set(10, IntelligentItem.of(islandItem.build(), event -> {
                SkyBlockPlugin.instance().getIslandManager().teleportToIsland(player);
            }));

            contents.set(11, IntelligentItem.of(warpIsland.build(), event -> {

                SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

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
                        CustomSounds.CASHIER.playSound(player, 0.6F, 1F, player.getLocation());
                        player.sendMessage(ChatAction.of("§aDein Kauf wurde erfolgreich abgeschlossen!"));
                        player.closeInventory();
                        return;
                    }
                    player.sendMessage(ChatAction.failure("§cDazu ist dein Kontostand zu niedrig."));
                    return;
                } else {
                    if (event.getClick() == ClickType.SHIFT_RIGHT) {
                        skyBlockIsland.setBoughtWarpLocation(null);

                        SoundAction.playNotification(player);
                        player.sendMessage(ChatAction.of("§aDie Aktion wurde erfolgreich abgeschlossen!"));
                        player.closeInventory();
                        return;
                    }
                }
            }));

            contents.set(16, IntelligentItem.of(deleteIsland.build(), event -> {
                player.closeInventory();
                ConversationFactory factory = new ConversationFactory(SkyBlockPlugin.instance())
                        .withFirstPrompt(new IslandDeletionPrompt())
                        .withLocalEcho(false)
                        .withEscapeSequence("cancel");
                Conversation conversation = factory.buildConversation(player);
                conversation.begin();
            }));
        }

        SoundAction.playInventoryOpen(player);


    }
}
