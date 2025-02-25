package mc.skyblock.plugin.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerAction {

    public boolean hasPermission(Player player, String permission) {

        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatAction.getNoPermission());
            return false;
        }
        return true;
    }
}
