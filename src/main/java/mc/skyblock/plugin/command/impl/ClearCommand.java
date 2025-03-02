package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.cosmetic.Cosmetics;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClearCommand extends AbstractCommand {

    public ClearCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "clear", "skyblock.command.clear");
    }

    @Override
    public void run(Player player, String[] args) {

        if(args.length == 0) {
            for (@Nullable ItemStack content : player.getInventory().getContents()) {
                if (content == null) continue;
                if (Cosmetics.isCosmeticItem(content)) continue;
                player.getInventory().remove(content);
            }
            player.sendMessage(ChatAction.of("Dein Inventar wurde geleert."));
            return;
        }

        if(args.length == 1) {

            Player targetPlayer = Bukkit.getPlayer(args[0]);

            for (@Nullable ItemStack content : targetPlayer.getInventory().getContents()) {
                if (content == null) continue;
                if (Cosmetics.isCosmeticItem(content)) continue;
                targetPlayer.getInventory().remove(content);
            }
            player.sendMessage(ChatAction.of("Das Inventar von " + targetPlayer.getName() + " wurde geleert."));
            return;
        }
        player.sendMessage(ChatAction.of("/clear <spielername>"));
    }
}
