package eu.revanox.skyblock.tag;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TagManager {

    private final Map<UUID, TextDisplay> tagMap;

    public TagManager() {
        this.tagMap = new HashMap<>();
    }

    public void updateTag(Player player) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

        TextDisplay textDisplay = this.tagMap.computeIfAbsent(player.getUniqueId(), _ -> {
            TextDisplay display = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
            display.text(Component.empty().appendNewline().appendNewline());
            display.setBillboard(Display.Billboard.CENTER);
            display.setShadowed(false);
            display.setBackgroundColor(Color.fromARGB(25, 0, 0, 0));
            display.setSeeThrough(false);
            display.setGravity(false);
            display.setPersistent(false);
            return display;
        });
        textDisplay.text(skyBlockUser.getSelectedTag().getTagText().appendNewline().appendNewline());
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setShadowed(false);
        textDisplay.setBackgroundColor(Color.fromARGB(25, 0, 0, 0));
        textDisplay.setSeeThrough(false);
        textDisplay.setGravity(false);
        textDisplay.setPersistent(false);

        for (Entity passenger : player.getPassengers()) {
            if (passenger instanceof TextDisplay) {
                passenger.remove();
            }
        }
        player.addPassenger(textDisplay);

    }

    public void destroyTag(Player player) {
        TextDisplay textDisplay = this.tagMap.remove(player.getUniqueId());
        if(textDisplay != null) {
            textDisplay.remove();
        }
    }
}
