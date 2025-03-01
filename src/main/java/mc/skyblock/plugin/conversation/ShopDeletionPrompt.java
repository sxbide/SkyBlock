package mc.skyblock.plugin.conversation;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.shop.model.Shop;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopDeletionPrompt extends StringPrompt {

    Shop shop;

    public ShopDeletionPrompt(Shop shop) {
        this.shop = shop;
    }

    @Override
    public @NotNull String getPromptText(ConversationContext context) {
        Player player = (Player) context.getForWhom();
        return "§c✘ Bist du sicher, dass du diesen Shop permanent löschen möchtest? Schreibe 'Ja' oder 'Nein'";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();

        if (input.trim().equalsIgnoreCase("Ja")) {

            Bukkit.getScheduler().runTask(SkyBlockPlugin.instance(), () -> {
                player.sendMessage(ChatAction.of("§aDein Shop wurde erfolgreich gelöscht."));
            });

            SkyBlockPlugin.instance().getShopManager().deleteShop(shop.getName());

            return Prompt.END_OF_CONVERSATION;

        } else {

            Bukkit.getScheduler().runTask(SkyBlockPlugin.instance(), () -> {
                player.sendMessage(ChatAction.of("§cDer Vorgang zum löschen deines Shops wurde abgebrochen."));
            });

            return Prompt.END_OF_CONVERSATION;
        }
    }

}

