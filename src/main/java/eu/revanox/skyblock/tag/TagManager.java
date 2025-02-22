package eu.revanox.skyblock.tag;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TagManager {

    private final Map<UUID, TextDisplay> tagMap;

    public TagManager() {
        this.tagMap = new HashMap<>();
    }

    public void updateTag(Player player) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());


        TextDisplay currentDisplay = (TextDisplay) player.getPassenger();

        if (currentDisplay != null) {
            currentDisplay.remove();
        }


        if (skyBlockUser.getSelectedTag() != null) {
            TextDisplay textDisplay = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
            textDisplay.text(skyBlockUser.getSelectedTag().getTagText().appendNewline().appendNewline());
            textDisplay.setRotation(0,0);
            textDisplay.setBillboard(Display.Billboard.VERTICAL);
            textDisplay.setBackgroundColor(Color.fromARGB(25, 0, 0, 0));
            textDisplay.setShadowed(true);
            textDisplay.setSeeThrough(false);
            textDisplay.setGravity(false);
            textDisplay.setPersistent(false);

            this.tagMap.put(player.getUniqueId(), textDisplay);
            player.addPassenger(textDisplay);
        }

    }

    public void teleportPlayer(Player player, Location location) {
        List<Entity> passengers = player.getPassengers();
        for (Entity passenger : passengers) {
            player.removePassenger(passenger);
        }

        player.teleport(location);
        for (Entity passenger : passengers) {
            passenger.teleport(location);
            player.addPassenger(passenger);
        }
    }

    public void deleteExistingTags() {
        this.tagMap.values().forEach(this::deleteTag);
    }

    public boolean isTag(TextDisplay textDisplay) {
        return this.tagMap.containsValue(textDisplay);
    }

    public void deleteTag(TextDisplay textDisplay) {
        for (TextDisplay value : this.tagMap.values()) {
            if(value.equals(textDisplay)) {
                value.remove();
            }
        }
    }

    public void destroyTag(Player player) {
        TextDisplay textDisplay = this.tagMap.remove(player.getUniqueId());
        if (textDisplay != null) {
            textDisplay.remove();
        }
        
        TextDisplay currentDisplay = (TextDisplay) player.getPassenger();

        if (currentDisplay != null) {
            currentDisplay.remove();
        }

        player.getPassengers().forEach(Entity::remove);
    }
}
