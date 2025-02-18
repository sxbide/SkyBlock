package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.log.privatemessage.model.PrivateMessage;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class ReplyCommand extends AbstractCommand {

    public ReplyCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "reply", null, "r", "answer");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUsage: /reply <message>"); //TODO: Replace message
            return;
        }
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        SkyBlockUser user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user.getPrivateMessageLog().getLogEntries().isEmpty()) {
            player.sendMessage("§cDu hast keine Nachrichten, auf die du antworten kannst.");
            return;
        }
        if (user.getPrivateMessageLog().getLatestEntry().getReceiver() == player.getUniqueId()) {
            player.sendMessage("§cDu hast keine Nachrichten, auf die du antworten kannst.");
            return;
        }
        OfflinePlayer receiver = plugin.getServer().getOfflinePlayer(user.getPrivateMessageLog().getLatestEntry().getReceiver());
        LocalDateTime time = LocalDateTime.now();
        user.getPrivateMessageLog().addEntry(time, new PrivateMessage(time, player.getUniqueId(), receiver.getUniqueId(), message.toString(), !receiver.isOnline(), false));
        if (!receiver.isOnline()) {
            player.sendMessage(ChatAction.failure("Der Spieler ist nicht online, aber diese Nachricht wird ihm zugestellt, wenn er sich einloggt."));
        }
        String miniMessage = "<#00ffff>⚑ <gold>DM an <#00ffff>" + receiver.getName() + "<gold>: <#00ffff>" + message;
        player.sendMessage(MiniMessage.miniMessage().deserialize(miniMessage));
        SkyBlockUser targetUser = plugin.getUserManager().getUser(receiver.getUniqueId());
        targetUser.sendPrivateMessage(player.getUniqueId(), message.toString());
    }
}
