package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.auctions.menu.AuctionsMenu;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.NumberUtil;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AuctionsCommand extends AbstractCommand {


    public AuctionsCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "auctions", null, "auktionen", "auktion", "auction", "auktionshaus", "ah", "auctionhouse");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            new AuctionsMenu().getRyseInventory().open(player);
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("sell")) {
            try {
                double price = Double.parseDouble(args[1]);

                if (price < 0) {
                    player.sendMessage(ChatAction.failure("§cDer Preis darf nicht kleiner als 1 ⛃ sein."));
                    return;
                }

                if (price > 1000000) {
                    player.sendMessage(ChatAction.failure("§cDer Preis darf nicht höher als 1 Millionen ⛃ sein."));
                    return;
                }

                ItemStack itemStack = player.getInventory().getItemInMainHand();

                if (itemStack.getType() != Material.AIR) {

//                        if (SkyBlockPlugin.instance().getAuctionsManager().getAuctionsByPlayer(player.getUniqueId()).size() == 5) {
//                            player.sendMessage(ChatAction.failure("§cDu kannst nicht mehr als 5 Auktionen zugleich haben."));
//                            return;
//                        }

                    SkyBlockPlugin.instance().getAuctionsManager().createAuction(player.getUniqueId(), itemStack, price);
                    player.sendMessage(ChatAction.of("§aAuktion für dein Item wurde für " + NumberUtil.formatBalance(price) + " ⛃ gelistet."));
                    player.getInventory().setItemInMainHand(ItemStack.empty());
                } else {
                    player.sendMessage(ChatAction.failure("§cBitte halte ein gültiges Item in der Hand."));
                }

            } catch (NumberFormatException ignored) {
                player.sendMessage(ChatAction.failure("§cBitte gebe einen korrekten Preis an."));
            }
        }
    }
}
