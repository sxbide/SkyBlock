package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.auctions.menu.AuctionsMenu;
import eu.revanox.skyblock.npc.event.NpcInteractionEvent;
import eu.revanox.skyblock.npc.model.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNpcInteractListener implements Listener {

    @EventHandler
    public void onNpcInteract(NpcInteractionEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();

        if(npc.getId() == 1) {
            if(event.getInteractionType().equals(NpcInteractionEvent.InteractionType.RIGHT_CLICK)) {
                new AuctionsMenu().getRyseInventory().open(player);
            }
        }
    }
}
