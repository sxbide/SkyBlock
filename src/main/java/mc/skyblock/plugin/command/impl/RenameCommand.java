package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenameCommand extends AbstractCommand {

    public RenameCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "rename", "skyblock.rename");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatAction.of("/rename <Name>"));
            SoundAction.playTaskFailed(player);
            return;
        }
        String name = String.join(" ", args);
        ItemStack item = player.getInventory().getItemInMainHand();
        item.editMeta(meta -> meta.displayName(MiniMessage.miniMessage().deserialize(name).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        player.getInventory().setItemInMainHand(item);
        player.sendMessage(ChatAction.of("Du hast das Item umbenannt!"));
        SoundAction.playTaskComplete(player);
    }
}
