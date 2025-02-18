package eu.revanox.skyblock.tag;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TagManager {

    private Map<UUID, TextDisplay> tagMap;

    public TagManager() {
        this.tagMap = new HashMap<>();
    }

    public void updateTag(Player player) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

        TextDisplay textDisplay = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
        textDisplay.text(skyBlockUser.getSelectedTag().getTagText().append(Component.newline()).append(Component.newline()));
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setShadowed(false);
        textDisplay.setBackgroundColor(Color.fromARGB(25, 0, 0, 0));
        textDisplay.setSeeThrough(false);
        textDisplay.setGravity(false);
        textDisplay.setPersistent(false);

        player.addPassenger(textDisplay);



    }

    public void destroyTag(Player player) {
        TextDisplay textDisplay = this.tagMap.get(player.getUniqueId());
        if(textDisplay != null) {
            textDisplay.remove();
        }
    }
}
