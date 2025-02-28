package mc.skyblock.plugin.listener;

import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.shop.menu.ShopListMenu;
import mc.skyblock.plugin.shop.model.Shop;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class NpcInteractListener implements Listener {

    @EventHandler
    public void onNpcInteract(NpcInteractEvent event) {
        Npc npc = event.getNpc();
        Player player = event.getPlayer();

        Optional<Shop> optionalShop = SkyBlockPlugin.instance().getShopManager().getShopByNpcId(npc.getData().getName());
        if (optionalShop.isPresent()) {
            event.setCancelled(true);
            optionalShop.ifPresentOrElse(shop -> {
                new ShopListMenu(shop).getRyseInventory().open(player);
            }, () -> {
                player.sendMessage(ChatAction.failure("Der Shop, welcher mit diesem NPC assoziiert ist, konnte nicht gefunden werden. Bitte kontaktiere einen Administrator."));
                SoundAction.playTaskFailed(player);
            });
            return;
        }

        //TODO: Handle other NPC interactions
    }

}
