package mc.skyblock.plugin.npc;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.npc.event.NpcInteractionEvent;
import mc.skyblock.plugin.npc.model.NPC;
import mc.skyblock.plugin.npc.repository.NPCRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NPCManager implements Listener {

    NPCRepository repository;
    Map<Integer, NPC> npcMap;
    Map<Integer, Entity> entityMap;

    public NPCManager() {
        this.repository = SkyBlockPlugin.instance().getMongoManager().create(NPCRepository.class);
        this.npcMap = this.repository.findAll().stream().collect(Collectors.toMap(NPC::getId, Function.identity()));
        this.entityMap = new HashMap<>();

        this.npcMap.values().forEach(this::spawn);

        Bukkit.getPluginManager().registerEvents(this, SkyBlockPlugin.instance());
    }

    private void spawn(NPC npc) {
        if (npc.getId() == 0) {
            npc.setId(this.getNextAvailableId());
            npcMap.put(npc.getId(), npc);
        }
        EntityType entityType = npc.getEntityType();
        Entity entity = npc.getLocation().getWorld().spawnEntity(npc.getLocation(), entityType);
        Component holoText = Component.empty();

        if (npc.getHologramLines().size() == 1) {
            holoText = MiniMessage.miniMessage().deserialize(npc.getHologramLines().getFirst());
        } else if (npc.getHologramLines().size() > 1) {
            for (String line : npc.getHologramLines()) {
                holoText = holoText.append(MiniMessage.miniMessage().deserialize(line)).append(Component.newline());
            }
        }

        TextDisplay textDisplay = npc.getLocation().getWorld().spawn(entity.getLocation(), TextDisplay.class);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setSeeThrough(true);
        textDisplay.setGravity(false);
        textDisplay.text(holoText);

        entity.setSilent(true);
        entity.setInvulnerable(true);

        entity.addPassenger(textDisplay);

        this.entityMap.put(npc.getId(), entity);
    }

    public void despawn(NPC npc) {
        EntityType entityType = npc.getEntityType();
        Entity entity = this.entityMap.get(npc.getId());
        for (Entity passenger : entity.getPassengers()) {
            entity.removePassenger(passenger);
            passenger.remove();
        }
        entity.remove();
    }

    public void despawnAll() {
        this.npcMap.values().forEach(this::despawn);
    }

    public void saveAll() {
        this.npcMap.values().forEach(this::save);
    }

    public void save(NPC npc) {
        this.npcMap.put(npc.getId(), npc);
        if (!this.entityMap.containsKey(npc.getId())) {
            this.spawn(npc);
        }
        this.repository.save(npc);
    }

    public void delete(NPC npc) {
        this.despawn(npc);
        this.npcMap.remove(npc.getId());
        this.repository.delete(npc);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (this.entityMap.containsValue(event.getEntity())) {
            NPC npc = this.npcMap.values().stream().filter(n -> this.entityMap.get(n.getId()).equals(event.getEntity())).findFirst().orElse(null);
            if (npc != null) {
                this.despawn(npc);
            }
        }
    }

    @EventHandler
    public void onEntityInteractByEntity(PlayerInteractAtEntityEvent event) {
        if (this.entityMap.containsValue(event.getRightClicked())) {
            event.setCancelled(true);
            NPC npc = this.npcMap.values().stream().filter(n -> this.entityMap.get(n.getId()).equals(event.getRightClicked())).findFirst().orElse(null);
            if (npc != null) {
                NpcInteractionEvent npcInteractionEvent = new NpcInteractionEvent(event.getRightClicked(), npc, NpcInteractionEvent.InteractionType.RIGHT_CLICK, event.getPlayer());
                Bukkit.getPluginManager().callEvent(npcInteractionEvent);
            }
        }
    }

    public NPC getNPCById(int id) {
        return this.npcMap.get(id);
    }

    public int getNextAvailableId() {
        return this.npcMap.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
    }

}
