package eu.revanox.skyblock.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@UtilityClass
public class SoundAction {

    public void playInventoryOpen(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 100, 1);
    }

    public void playGoodWork(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 100, 1);
    }
}
