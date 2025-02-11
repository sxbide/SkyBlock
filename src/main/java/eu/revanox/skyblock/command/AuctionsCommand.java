package eu.revanox.skyblock.command;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.auctions.menu.AuctionsMenu;
import eu.revanox.skyblock.util.ChatAction;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AuctionsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player player) {

            if(args.length == 0) {
                new AuctionsMenu().getRyseInventory().open(player);
                return false;
            }

            if(args.length == 2 && args[0].equalsIgnoreCase("sell")) {
                try {
                    double price = Double.parseDouble(args[1]);

                    if(price < 0) {
                        player.sendMessage(ChatAction.failure("§cDer Preis darf nicht kleiner als 1 ⛃ sein."));
                        return false;
                    }

                    if(price > 1000000) {
                        player.sendMessage(ChatAction.failure("§cDer Preis darf nicht höher als 1 Millionen ⛃ sein."));
                        return false;
                    }

                    ItemStack itemStack = player.getInventory().getItemInMainHand();

                    if(itemStack.getType() != Material.AIR) {

                        if(SkyBlockPlugin.instance().getAuctionsManager().getAuctionsByPlayer(player.getUniqueId()).size() == 5) {
                            player.sendMessage(ChatAction.failure("§cDu kannst nicht mehr als 5 Auktionen zugleich haben."));
                            return false;
                        }
                        SkyBlockPlugin.instance().getAuctionsManager().createAuction(player.getUniqueId(), itemStack, price);
                        player.sendMessage(ChatAction.of("§aAuktion für dein Item wurde für " + price + " ⛃ gelistet."));
                        player.getInventory().setItemInMainHand(ItemStack.empty());
                    } else {
                        player.sendMessage(ChatAction.failure("§cBitte halte ein gültiges Item in der Hand."));
                    }

                } catch (NumberFormatException ignored) {
                    player.sendMessage(ChatAction.failure("§cBitte gebe einen korrekten Preis an."));
                }
            }
        }
        return false;
    }
}
