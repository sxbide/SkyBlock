package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.shop.model.currency.ShopCurrencyFormat;
import mc.skyblock.plugin.shop.model.item.ShopItem;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShopAdminCommand extends AbstractCommand {

    public ShopAdminCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "shopadmin", "skyblock.shopadmin");
    }

    private void sendSyntax(Player player) {
        player.sendMessage(ChatAction.of("/shopadmin create <name>"));
        player.sendMessage(ChatAction.of("/shopadmin delete <name>"));
        player.sendMessage(ChatAction.of("/shopadmin paymentType <name> <type>"));
        player.sendMessage(ChatAction.of("/shopadmin additem <shop> <price>"));
        player.sendMessage(ChatAction.of("/shopadmin setNpc <name> <npc>"));
        player.sendMessage(ChatAction.of("/shopadmin edit <name>"));
        SoundAction.playTaskFailed(player);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            sendSyntax(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length != 2) {
                    sendSyntax(player);
                    return;
                }
                if (SkyBlockPlugin.instance().getShopManager().getShop(args[1]) != null) {
                    player.sendMessage(ChatAction.failure("Ein Shop mit diesem Namen existiert bereits."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                SkyBlockPlugin.instance().getShopManager().createShop(args[1]);
                player.sendMessage(ChatAction.of("Shop " + args[1] + " erstellt."));
                SoundAction.playTaskComplete(player);
                break;
            case "delete":
                if (args.length != 2) {
                    sendSyntax(player);
                    return;
                }
                if (SkyBlockPlugin.instance().getShopManager().getShop(args[1]) == null) {
                    player.sendMessage(ChatAction.failure("Ein Shop mit diesem Namen existiert nicht."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                SkyBlockPlugin.instance().getShopManager().deleteShop(args[1]);
                player.sendMessage(ChatAction.of("Shop " + args[1] + " gelöscht."));
                SoundAction.playTaskComplete(player);
                break;
            case "paymenttype":
                if (args.length != 3) {
                    sendSyntax(player);
                    return;
                }
                if (SkyBlockPlugin.instance().getShopManager().getShop(args[1]) == null) {
                    player.sendMessage(ChatAction.failure("Ein Shop mit diesem Namen existiert nicht."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                ShopCurrencyFormat currencyFormat;
                try {
                    currencyFormat = ShopCurrencyFormat.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatAction.failure("Ungültige Zahlungsmethode. Verfügbare Zahlungsmethoden: GOLD_PIECES, COINS"));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                SkyBlockPlugin.instance().getShopManager().editShop(args[1], shop -> shop.setCurrencyFormat(currencyFormat));
                player.sendMessage(ChatAction.of("Zahlungsmethode für Shop " + args[1] + " gesetzt."));
                SoundAction.playTaskComplete(player);
                break;
            case "setnpc":
                if (args.length != 3) {
                    sendSyntax(player);
                    return;
                }
                if (SkyBlockPlugin.instance().getShopManager().getShop(args[1]) == null) {
                    player.sendMessage(ChatAction.failure("Ein Shop mit diesem Namen existiert nicht."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                SkyBlockPlugin.instance().getShopManager().editShop(args[1], shop -> shop.setNpcId(args[2]));
                player.sendMessage(ChatAction.of("NPC für Shop " + args[1] + " gesetzt."));
                player.sendMessage(ChatAction.of("Bitte beachte, dass der NPC-Name nicht überprüft wird."));
                SoundAction.playTaskComplete(player);
                break;
            case "edit":
                if (args.length != 2) {
                    sendSyntax(player);
                    return;
                }
                if (SkyBlockPlugin.instance().getShopManager().getShop(args[1]) == null) {
                    player.sendMessage(ChatAction.failure("Ein Shop mit diesem Namen existiert nicht."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                //TODO: Open edit inventory
                SoundAction.playInventoryOpen(player);
                break;
            case "additem":
                if (args.length != 3) {
                    sendSyntax(player);
                    return;
                }
                if (SkyBlockPlugin.instance().getShopManager().getShop(args[1]) == null) {
                    player.sendMessage(ChatAction.failure("Ein Shop mit diesem Namen existiert nicht."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                ItemStack item = player.getInventory().getItemInMainHand().clone();
                if (item.getType().isAir() || item.getAmount() == 0 || item.getAmount() > item.getMaxStackSize()) {
                    player.sendMessage(ChatAction.failure("Bitte halte ein Item in der Hand."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                Number price;
                try {
                    price = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatAction.failure("Ungültiger Preis."));
                    SoundAction.playTaskFailed(player);
                    return;
                }
                SkyBlockPlugin.instance().getShopManager().editShop(args[1], shop -> {
                    ShopItem shopItem = new ShopItem();
                    shopItem.setId(shop.getItems().size());
                    shopItem.setPrice(price.doubleValue());
                    shopItem.setItemStack(item);
                    shopItem.setAmount(item.getAmount());
                    shopItem.setDiscount(false);
                    shopItem.setDiscountPercentage(0);
                    shop.getItems().add(shopItem);
                });
                player.sendMessage(ChatAction.of("Item hinzugefügt."));
                SoundAction.playTaskComplete(player);
            default:
                sendSyntax(player);
                break;
        }
    }
}
