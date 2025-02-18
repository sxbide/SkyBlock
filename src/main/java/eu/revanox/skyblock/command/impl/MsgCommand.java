package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.log.privatemessage.model.PrivateMessage;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class MsgCommand extends AbstractCommand {

    public MsgCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "msg", null, "privatemessage", "pm", "tell", "whisper");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /msg <player> <message>"); //TODO: Replace message
            return;
        }
        String targetName = args[0];
        OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        LocalDateTime time = LocalDateTime.now();
        SkyBlockUser user = plugin.getUserManager().getUser(player.getUniqueId());
        user.getPrivateMessageLog().addEntry(time, new PrivateMessage(time, player.getUniqueId(), target.getUniqueId(), message.toString(), !target.isOnline(), false));
        if (!target.isOnline()) {
            player.sendMessage(ChatAction.failure("Der Spieler ist nicht online, aber diese Nachricht wird ihm zugestellt, wenn er sich einloggt."));
        }
        String miniMessage = "<#00ffff>⚑ <gold>DM an <#00ffff>" + target.getName() + "<gold>: <#00ffff>" + message;
        player.sendMessage(MiniMessage.miniMessage().deserialize(miniMessage));
        SkyBlockUser targetUser = plugin.getUserManager().getUser(target.getUniqueId());
        targetUser.sendPrivateMessage(player.getUniqueId(), message.toString());
    }
}
