package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.perks.Perks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class PlayerHeldItemListener implements Listener {

    private Map<Player, BukkitTask> perkTimer = new HashMap<>();

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {

        Player player = event.getPlayer();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        
        if (offHandItem.getType() != Material.AIR) {

            Perks perk = Perks.isPerkItem(offHandItem);
            if (perk != null) {
                if (perk == Perks.FAST_BREAK) {

                    BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(SkyBlockPlugin.instance(), () -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 1,1,false,false));
                    }, 0L, 20L);
                    perkTimer.put(player, bukkitTask);
                }
            } else {
                if(perkTimer.containsKey(player)) {
                    perkTimer.get(player).cancel();
                }
            }
        }
    }
}
