package eu.revanox.skyblock.scoreboard;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.guild.model.SkyBlockGuild;
import eu.revanox.skyblock.island.model.SkyBlockIsland;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.NumberUtil;
import eu.revanox.skyblock.util.ResourceIcons;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScoreboardManager {

    SkyBlockPlugin plugin;
    Map<Player, FastBoard> fastBoardMap;


    public ScoreboardManager(SkyBlockPlugin plugin) {
        this.fastBoardMap = new HashMap<>();
        this.plugin = plugin;
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> Bukkit.getOnlinePlayers().forEach(this::updateScoreboard), 0L, 20L);
    }

    private void updateScoreboard(Player player) {
        FastBoard fastBoard = this.fastBoardMap.get(player);

        SkyBlockUser skyBlockUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        SkyBlockGuild skyBlockGuild = this.plugin.getGuildManager().getGuild(player);

        String guildName = (skyBlockGuild != null ? skyBlockGuild.getGuildName() : "-");

        SkyBlockIsland skyBlockIsland = null;

        if (player.getWorld().getName().startsWith("island_")) {
            skyBlockIsland = SkyBlockPlugin.instance().getIslandManager().getIslandByWorld(player.getWorld());
        }

        fastBoard.updateTitle(Component.text("§r" + ResourceIcons.SCOREBOARD_HEADER.unicode()));

        Component tabHeader = Component.text("§1                                                      §r")
                .appendNewline()
                .append(Component.text("§r" + ResourceIcons.SCOREBOARD_HEADER.unicode()).append(Component.text(" §8• §7" + Bukkit.getOnlinePlayers().size() + " online"))
                .appendNewline()
                .appendSpace());
        Component tabFooter = Component.newline()
                .append(Component.text("§7Schaue dir unseren sozialen Medien an:"))
                .appendNewline()
                .append(Component.text("§r" + ResourceIcons.SOCIALS_TAG_TABLIST.unicode()))
                .appendNewline()
                .appendSpace();

        player.sendPlayerListHeaderAndFooter(tabHeader, tabFooter);

        if (skyBlockIsland != null) {
            fastBoard.updateLines(
                    Component.empty(),
                    Component.text("§r" + (skyBlockIsland.getOwnerUniqueId().equals(player.getUniqueId()) ? ResourceIcons.OWNER_TAG_SCOREBOARD.unicode() : ResourceIcons.VISITOR_TAG_SCOREBOARD.unicode())),
                    Component.empty(),
                    Component.empty(),
                    Component.text("§r" + ResourceIcons.BALANCE_TAG_SCOREBOARD.unicode()),
                    Component.text(NumberUtil.formatBalance(skyBlockUser.getBalance()), NamedTextColor.WHITE),
                    Component.empty(),
                    Component.text("§r" + ResourceIcons.ISLAND_TAG_SCOREBOARD.unicode()),
                    Component.text(Objects.requireNonNull(Bukkit.getOfflinePlayer(skyBlockIsland.getOwnerUniqueId()).getName()), NamedTextColor.WHITE),
                    Component.empty(),
                    Component.text("§r" + ResourceIcons.GUILD_TAG_SCOREBOARD.unicode()),
                    Component.text(guildName, NamedTextColor.WHITE),
                    Component.empty()
            );
        } else {

            fastBoard.updateLines(
                    Component.empty(),
                    Component.text("§r" + ResourceIcons.BALANCE_TAG_SCOREBOARD.unicode()),
                    Component.text(NumberUtil.formatBalance(skyBlockUser.getBalance()), NamedTextColor.WHITE),
                    Component.empty(),
                    Component.text("§r" + ResourceIcons.ONLINE_TAG_SCOREBOARD.unicode()),
                    Component.text(Bukkit.getOnlinePlayers().size(), NamedTextColor.WHITE),
                    Component.empty(),
                    Component.text("§r" + ResourceIcons.GUILD_TAG_SCOREBOARD.unicode()),
                    Component.text(guildName, NamedTextColor.WHITE),
                    Component.empty(),
                    Component.text("§r" + ResourceIcons.ISLAND_TAG_SCOREBOARD.unicode()),
                    Component.text("Spawn", NamedTextColor.WHITE),
                    Component.empty()
            );
        }
    }

    public void createScoreboard(Player player) {
        this.fastBoardMap.put(player, new FastBoard(player));
    }

    public void destroyScoreboard(Player player) {
        this.fastBoardMap.remove(player).delete();
    }
}
