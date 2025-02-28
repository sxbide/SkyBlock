package mc.skyblock.plugin.scoreboard;

import fr.mrmicky.fastboard.adventure.FastBoard;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.guild.model.SkyBlockGuild;
import mc.skyblock.plugin.island.model.SkyBlockIsland;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.NumberUtil;
import mc.skyblock.plugin.util.custom.ResourceIcons;
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
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> Bukkit.getOnlinePlayers().forEach(this::updateScoreboard), 0L, 1L);
    }

    private void updateScoreboard(Player player) {
        FastBoard fastBoard = this.fastBoardMap.get(player);

        SkyBlockUser skyBlockUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        SkyBlockGuild skyBlockGuild = this.plugin.getGuildManager().getGuild(player);

        String guildOnlineMembers = (skyBlockGuild != null ? "<#818181>(<green>" + skyBlockGuild.getGuildMembers()
                .stream().filter(uuid -> Bukkit.getOfflinePlayer(uuid).isOnline()).count() + "<#818181>/<red>" + skyBlockGuild.getGuildMembers().size() + "<#818181>)" : "-");
        Component guildName = (skyBlockGuild != null ? MiniMessage.miniMessage().deserialize(skyBlockGuild.getGuildName() + " " + guildOnlineMembers) : Component.text("-", NamedTextColor.WHITE));

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
                    guildName,
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
                    guildName,
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
