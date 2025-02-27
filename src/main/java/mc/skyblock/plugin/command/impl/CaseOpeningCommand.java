package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CaseOpeningCommand extends AbstractCommand {

    public CaseOpeningCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "caseopening", null, "co");
    }

    private void sendSyntax(Player player) {
        String[] commands = {
                "/caseopening setkey",
                "/caseopening additem <chance>",
                "/caseopening removeitem",
                "/caseopening listitems",
                "/caseopening open",
                "/caseopening givekey <player> [amount]",
                "/caseopening givekeyall [amount]"
        };
        for (String command : commands) {
            player.sendMessage(ChatAction.of(command));
        }
        SoundAction.playTaskFailed(player);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!player.hasPermission("skyblock.caseopening")) {
            plugin.getTagManager().teleportPlayer(player, plugin.getLocationManager().getPosition("caseopening").getLocation());
            return;
        }
        if (args.length == 0) {
            sendSyntax(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "setkey":
                handleSetKey(player, args);
                break;
            case "additem":
                handleAddItem(player, args);
                break;
            case "removeitem":
                handleRemoveItem(player, args);
                break;
            case "listitems":
                handleListItems(player, args);
                break;
            case "open":
                handleOpen(player, args);
                break;
            case "givekey":
                handleGiveKey(player, args);
                break;
            case "givekeyall":
                handleGiveKeyAll(player, args);
                break;
            default:
                sendSyntax(player);
                break;
        }
    }

    private void handleSetKey(Player player, String[] args) {
        if (args.length != 1) {
            sendSyntax(player);
            return;
        }
        plugin.getCaseOpeningManager().getACase().setKeyItem(player.getInventory().getItemInMainHand().clone());
        plugin.getCaseOpeningManager().getRepository().save(plugin.getCaseOpeningManager().getACase());
        player.sendMessage(ChatAction.of("Der Schlüssel wurde gesetzt."));
        SoundAction.playTaskComplete(player);
    }

    private void handleAddItem(Player player, String[] args) {
        if (args.length != 2) {
            sendSyntax(player);
            return;
        }
        plugin.getCaseOpeningManager().getACase().addCaseItem(player.getInventory().getItemInMainHand().clone(), Double.parseDouble(args[1]));
        plugin.getCaseOpeningManager().getRepository().save(plugin.getCaseOpeningManager().getACase());
        player.sendMessage(ChatAction.of("Das Item wurde hinzugefügt."));
        SoundAction.playTaskComplete(player);
    }

    private void handleRemoveItem(Player player, String[] args) {
        if (args.length != 1) {
            sendSyntax(player);
            return;
        }
        plugin.getCaseOpeningManager().getACase().removeCaseItem(player.getInventory().getItemInMainHand());
        plugin.getCaseOpeningManager().getRepository().save(plugin.getCaseOpeningManager().getACase());
        player.sendMessage(ChatAction.of("Das Item wurde entfernt."));
        SoundAction.playTaskComplete(player);
    }

    private void handleListItems(Player player, String[] args) {
        if (args.length != 1) {
            sendSyntax(player);
            return;
        }
        player.sendMessage(Component.text("preview gui"));
        SoundAction.playTaskComplete(player);
    }

    private void handleOpen(Player player, String[] args) {
        if (args.length != 1) {
            sendSyntax(player);
            return;
        }
        plugin.getCaseOpeningManager().open(player);
    }

    private void handleGiveKey(Player player, String[] args) {
        if (args.length < 2) {
            sendSyntax(player);
            return;
        }
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatAction.failure("Der Spieler ist nicht online."));
            SoundAction.playTaskFailed(player);
            return;
        }
        int amount = args.length == 3 ? Integer.parseInt(args[2]) : 1;
        target.getInventory().addItem(plugin.getCaseOpeningManager().getACase().getKeyItem().clone());
        player.sendMessage(ChatAction.of("Der Spieler hat den Schlüssel erhalten."));
        target.sendMessage(ChatAction.of("Du hast " + amount + "x Schlüssel für die Antike Kiste erhalten."));
        SoundAction.playTaskComplete(player);
    }

    private void handleGiveKeyAll(Player player, String[] args) {
        if (args.length > 2) {
            sendSyntax(player);
            return;
        }
        int amount = args.length == 2 ? Integer.parseInt(args[1]) : 1;
        plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.getInventory().addItem(plugin.getCaseOpeningManager().getACase().getKeyItem().clone());
            onlinePlayer.sendMessage(ChatAction.of("Du hast " + amount + "x Schlüssel für die Antike Kiste erhalten."));
        });
        player.sendMessage(ChatAction.of("Alle Spieler haben den Schlüssel erhalten."));
        SoundAction.playTaskComplete(player);
    }
}