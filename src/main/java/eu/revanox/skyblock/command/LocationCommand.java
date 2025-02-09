package eu.revanox.skyblock.command;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.location.model.Location;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.PlayerAction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            if (PlayerAction.hasPermission(player, "skyblock.location")) {

                if (args.length > 0) {
                    String locationName = args[0];

                    Location location = new Location();
                    location.setName(locationName);
                    location.setLocation(player.getLocation());

                    SkyBlockPlugin.instance().getLocationManager().savePosition(location);
                    player.sendMessage(ChatAction.of("§aDer Locationpunkt " + locationName + " wurde gesetzt."));
                    return false;
                }
                player.sendMessage(ChatAction.failure("Verwende dazu: /setlocation <name>"));
                return false;
            }
        }
        return false;
    }
}
