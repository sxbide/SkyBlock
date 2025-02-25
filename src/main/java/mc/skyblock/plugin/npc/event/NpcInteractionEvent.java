package mc.skyblock.plugin.npc.event;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.npc.model.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NpcInteractionEvent extends EntityEvent {

    public static final HandlerList HANDLERS = new HandlerList();
    NPC npc;
    InteractionType interactionType;
    Player player;

    public NpcInteractionEvent(@NotNull Entity what, @NotNull NPC npc, @NotNull InteractionType interactionType, @NotNull Player player) {
        super(what);
        this.npc = npc;
        this.interactionType = interactionType;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public enum InteractionType {
        LEFT_CLICK,
        RIGHT_CLICK
    }
}
