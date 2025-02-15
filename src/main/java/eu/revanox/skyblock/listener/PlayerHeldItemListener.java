package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.perks.Perks;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerHeldItemListener implements Listener {

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {

        Player player = event.getPlayer();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();


        if(offHandItem.getType() != Material.AIR) {

            Perks perk = Perks.isPerkItem(offHandItem);
            if(perk != null) {
                if(perk == Perks.FAST_BREAK) {
                    //player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, ))
                }
            }
        }
    }
}
