package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.location.model.Location;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
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
