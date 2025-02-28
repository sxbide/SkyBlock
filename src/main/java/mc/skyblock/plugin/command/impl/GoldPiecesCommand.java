package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GoldPiecesCommand extends AbstractCommand {


    public GoldPiecesCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "goldpieces", null, "goldstücke", "gold", "gp");
    }

    @Override
    public void run(Player player, String[] args) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
        player.sendMessage(ChatAction.of("§7Du hast aktuell §e" + skyBlockUser.getGoldPieces() + " §7Goldstücke"));
    }
}
