package eu.revanox.skyblock.command.impl;

import com.google.common.collect.Lists;
import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.location.model.Location;
import eu.revanox.skyblock.npc.model.NPC;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Color;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GoldPiecesCommand extends AbstractCommand {


    public GoldPiecesCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "goldpieces", null, "goldstücke", "gold", "gp");
    }

    @Override
    public void run(Player player, String[] args) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
        player.sendMessage(ChatAction.of("§7Du hast aktuell §e" + skyBlockUser.getGoldPieces() + " §7Goldstücke"));


        NPC npc = new NPC();
        npc.setDisplayName("Nigger");
        npc.setLocation(player.getLocation());
        npc.setEntityType(EntityType.VILLAGER);
        npc.setId(1);
        npc.setHologramLines(List.of("Niggers stinken", "nach kacke"));

        SkyBlockPlugin.instance().getNpcManager().save(npc);
    }
}
