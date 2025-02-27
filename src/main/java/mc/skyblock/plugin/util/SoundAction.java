package mc.skyblock.plugin.util;

import lombok.experimental.UtilityClass;
import mc.skyblock.plugin.util.custom.CustomSounds;
import org.bukkit.entity.Player;

@UtilityClass
public class SoundAction {

    public void playInventoryOpen(Player player) {
        //player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 50, 0.2F);
        CustomSounds.NOTIFICATION.playSound(player, 100, 1, player.getLocation());
    }

    public void playNotification(Player player) {
        //player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 100, 1);
        CustomSounds.NOTIFICATION.playSound(player, 100, 1, player.getLocation());
    }

    public void playTaskComplete(Player player) {
        //player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);
        CustomSounds.NOTIFICATION.playSound(player, 100, 1, player.getLocation());

    }

    public void playTaskFailed(Player player) {
        CustomSounds.ERROR.playSound(player, 100, 1, player.getLocation());
    }
}
