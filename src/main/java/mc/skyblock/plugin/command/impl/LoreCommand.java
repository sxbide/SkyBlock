package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LoreCommand extends AbstractCommand {

    public LoreCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "lore", "skyblock.rename");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatAction.of("/lore add <text>"));
            player.sendMessage(ChatAction.of("/lore remove <index>"));
            player.sendMessage(ChatAction.of("/lore insertafter <index> <text>"));
            player.sendMessage(ChatAction.of("/lore set <index> <text>"));
            player.sendMessage(ChatAction.of("/lore clear"));
            SoundAction.playTaskFailed(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "add":
                handleAdd(player, args);
                break;
            case "remove":
                handleRemove(player, args);
                break;
            case "insertafter":
                handleInsertAfter(player, args);
                break;
            case "set":
                handleSet(player, args);
                break;
            case "clear":
                handleClear(player);
                break;
            default:
                player.sendMessage(ChatAction.of("/lore add <text>"));
                player.sendMessage(ChatAction.of("/lore remove <index>"));
                player.sendMessage(ChatAction.of("/lore insertafter <index> <text>"));
                player.sendMessage(ChatAction.of("/lore set <index> <text>"));
                player.sendMessage(ChatAction.of("/lore clear"));
                SoundAction.playTaskFailed(player);
                break;
        }

    }

    public void handleAdd(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatAction.of("/lore add <text>"));
            SoundAction.playTaskFailed(player);
            return;
        }
        StringBuilder lore = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            lore.append(args[i]).append(" ");
        }
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            player.getInventory().getItemInMainHand().getItemMeta().lore(new ArrayList<>());
        }
        player.getInventory().getItemInMainHand().getItemMeta().lore().add(MiniMessage.miniMessage().deserialize(lore.toString()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        player.sendMessage(ChatAction.of("Lore hinzugefügt. Index: " + (player.getInventory().getItemInMainHand().getItemMeta().lore().size() - 1)));
        SoundAction.playTaskComplete(player);
    }

    public void handleRemove(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatAction.of("/lore remove <index>"));
            SoundAction.playTaskFailed(player);
            return;
        }
        int index = Integer.parseInt(args[1]);
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            player.sendMessage(ChatAction.failure("Keine Lore vorhanden."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (index < 0 || index >= player.getInventory().getItemInMainHand().getItemMeta().lore().size()) {
            player.sendMessage(ChatAction.failure("Ungültiger Index."));
            SoundAction.playTaskFailed(player);
            return;
        }
        player.getInventory().getItemInMainHand().getItemMeta().lore().remove(index);
        player.sendMessage(ChatAction.of("Lore entfernt."));
        SoundAction.playTaskComplete(player);
    }

    public void handleInsertAfter(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatAction.of("/lore insertafter <index> <text>"));
            SoundAction.playTaskFailed(player);
            return;
        }
        int index = Integer.parseInt(args[1]);
        StringBuilder lore = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            lore.append(args[i]).append(" ");
        }
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            player.sendMessage(ChatAction.failure("Keine Lore vorhanden."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (index < 0 || index >= player.getInventory().getItemInMainHand().getItemMeta().lore().size()) {
            player.sendMessage(ChatAction.failure("Ungültiger Index."));
            SoundAction.playTaskFailed(player);
            return;
        }
        player.getInventory().getItemInMainHand().getItemMeta().lore().add(index + 1, MiniMessage.miniMessage().deserialize(lore.toString()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        player.sendMessage(ChatAction.of("Lore hinzugefügt. Index: " + (index + 1)));
        SoundAction.playTaskComplete(player);
    }

    public void handleSet(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatAction.of("/lore set <index> <text>"));
            SoundAction.playTaskFailed(player);
            return;
        }
        int index = Integer.parseInt(args[1]);
        StringBuilder lore = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            lore.append(args[i]).append(" ");
        }
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            player.sendMessage(ChatAction.failure("Keine Lore vorhanden."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (index < 0 || index >= player.getInventory().getItemInMainHand().getItemMeta().lore().size()) {
            player.sendMessage(ChatAction.failure("Ungültiger Index."));
            SoundAction.playTaskFailed(player);
            return;
        }
        player.getInventory().getItemInMainHand().getItemMeta().lore().set(index, MiniMessage.miniMessage().deserialize(lore.toString()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        player.sendMessage(ChatAction.of("Lore gesetzt."));
        SoundAction.playTaskComplete(player);
    }

    public void handleClear(Player player) {
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            player.sendMessage(ChatAction.failure("Keine Lore vorhanden."));
            SoundAction.playTaskFailed(player);
            return;
        }
        player.getInventory().getItemInMainHand().getItemMeta().lore().clear();
        player.sendMessage(ChatAction.of("Lore entfernt."));
        SoundAction.playTaskComplete(player);
    }
}
