package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.SkyBlockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;

public class PlayerEntityDismountListener implements Listener {

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {

        if(event.getEntity() instanceof Player player) {

            Entity dismountedEntity = event.getDismounted();

            if(dismountedEntity instanceof TextDisplay textDisplay) {
                if(SkyBlockPlugin.instance().getTagManager().isTag(textDisplay)) {
                    SkyBlockPlugin.instance().getTagManager().deleteTag(textDisplay);
                }
            }
        }
    }
}
