package eu.revanox.skyblock.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@UtilityClass
public class SoundAction {

    public void playInventoryOpen(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 50, 0.2F);
    }

    public void playGoodWork(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 100, 1);
    }

    public void playTaskComplete(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);
    }

    public void playTaskFailed(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
    }
}
