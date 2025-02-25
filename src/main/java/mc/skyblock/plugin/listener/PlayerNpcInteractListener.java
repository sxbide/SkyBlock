package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.auctions.menu.AuctionsMenu;
import mc.skyblock.plugin.npc.event.NpcInteractionEvent;
import mc.skyblock.plugin.npc.model.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNpcInteractListener implements Listener {

    @EventHandler
    public void onNpcInteract(NpcInteractionEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();

        if (npc.getId() == 1) {
            if (event.getInteractionType().equals(NpcInteractionEvent.InteractionType.RIGHT_CLICK)) {
                new AuctionsMenu().getRyseInventory().open(player);
            }
        }
    }
}
