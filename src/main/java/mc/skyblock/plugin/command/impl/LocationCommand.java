package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.location.model.Location;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocationCommand extends AbstractCommand {


    public LocationCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "location", "skyblock.location", "loc");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length > 1 && args[0].equalsIgnoreCase("set")) {
            String locationName = args[1];

            Location location = new Location();
            location.setName(locationName);
            location.setLocation(player.getLocation());

            SkyBlockPlugin.instance().getLocationManager().savePosition(location);
            player.sendMessage(ChatAction.of("§aDer Locationpunkt " + locationName + " wurde gesetzt."));
            return;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("tp")) {
            String locationName = args[1];

            Location location = SkyBlockPlugin.instance().getLocationManager().getPosition(locationName);

            if (location == null) {
                player.sendMessage(ChatAction.failure("§cDer Locationpunkt " + locationName + " existiert nicht."));
                return;
            }

            player.teleport(location.getLocation());
            player.sendMessage(ChatAction.of("§aDu wurdest zum Locationpunkt " + locationName + " teleportiert."));
            return;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("del")) {
            String locationName = args[1];

            Location location = SkyBlockPlugin.instance().getLocationManager().getPosition(locationName);

            if (location == null) {
                player.sendMessage(ChatAction.failure("§cDer Locationpunkt " + locationName + " existiert nicht."));
                return;
            }

            SkyBlockPlugin.instance().getLocationManager().deletePosition(location);
            player.sendMessage(ChatAction.of("§aDer Locationpunkt " + locationName + " wurde gelöscht."));
            return;
        }
        player.sendMessage(ChatAction.failure("/location set <name>"));
        player.sendMessage(ChatAction.failure("/location tp <name>"));
        player.sendMessage(ChatAction.failure("/location del <name>"));
        return;
    }
}
