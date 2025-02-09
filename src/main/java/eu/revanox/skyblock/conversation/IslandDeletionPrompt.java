package eu.revanox.skyblock.conversation;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.util.ChatAction;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class IslandDeletionPrompt extends StringPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        Player player = (Player) context.getForWhom();
        return ChatAction.failure("§cBist du sicher, dass du deine Insel permanent löschen möchtest? Schreibe 'Ja' oder 'Nein'");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();

        if (input.trim().equalsIgnoreCase("Ja")) {

            Bukkit.getScheduler().runTask(SkyBlockPlugin.instance(), () -> {
                player.sendMessage(ChatAction.of("§aDeine Insel wurde erfolgreich permanent gelöscht!"));
            });

            SkyBlockPlugin.instance().getIslandManager().deleteIsland(player);

            return Prompt.END_OF_CONVERSATION;

        } else {

            Bukkit.getScheduler().runTask(SkyBlockPlugin.instance(), () -> {
                player.sendMessage(ChatAction.of("§cDer Vorgang zum löschen deiner Insel wurde abgebrochen."));
            });

            return Prompt.END_OF_CONVERSATION;
        }
    }

}

