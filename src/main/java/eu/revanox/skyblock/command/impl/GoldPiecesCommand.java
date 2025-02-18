package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.location.model.Location;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GoldPiecesCommand extends AbstractCommand {


    public GoldPiecesCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "goldpieces", null, "goldstücke", "gold", "gp");
    }

    @Override
    public void run(Player player, String[] args) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
        player.sendMessage(ChatAction.of("§7Du hast aktuell §e" + skyBlockUser.getGoldPieces() + " §7Goldstücke"));


        TextDisplay textDisplay = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
        textDisplay.text(Component.newline().append(Component.newline()).append(ChatAction.getPrefixGradient()));
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setShadowed(false);
        textDisplay.setBackgroundColor(null);
        textDisplay.setGravity(false);
        textDisplay.setPersistent(false);

        player.addPassenger(textDisplay);
    }
}
