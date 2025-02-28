package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopAdminCommand extends AbstractCommand {

    public ShopAdminCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "shopadmin", "skyblock.shopadmin");
    }

    private void sendSyntax(Player player) {
        player.sendMessage(ChatAction.of("/shopadmin create <name>"));
        player.sendMessage(ChatAction.of("/shopadmin delete <name>"));
        player.sendMessage(ChatAction.of("/shopadmin edit <name>"));
    }

    @Override
    public void run(Player player, String[] args) {

    }
}
