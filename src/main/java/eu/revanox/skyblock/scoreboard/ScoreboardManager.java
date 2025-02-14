package eu.revanox.skyblock.scoreboard;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.island.model.SkyBlockIsland;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.NumberUtil;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

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

        SkyBlockIsland skyBlockIsland = null;

        if (player.getWorld().getName().startsWith("island_")) {
            skyBlockIsland = SkyBlockPlugin.instance().getIslandManager().getIslandByWorld(player.getWorld());
        }

        fastBoard.updateTitle(ChatAction.getPrefixGradient());

        Component tabHeader = Component.newline().append(ChatAction.getPrefixGradient().appendNewline().append(Component.text("§7§oearly access skyblock")).appendNewline());
        Component tabFooter = Component.newline().append(MiniMessage.miniMessage().deserialize("<gradient:#E2A574:#E0EF50>skyblock.blockarion.de</gradient>"))
                .appendNewline();

        player.sendPlayerListHeaderAndFooter(tabHeader, tabFooter);

        if (skyBlockIsland != null) {
            fastBoard.updateLines(
                    Component.empty(),
                    Component.text(" §8• §c" + (skyBlockIsland.getOwnerUniqueId().equals(player.getUniqueId()) ? "§cBesitzer" : "§9Besucher")),
                    Component.empty(),
                    Component.text(" §8• §7Kontostand"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#C7D96B>" + NumberUtil.formatBalance(skyBlockUser.getBalance()) + " ⛃")),
                    Component.empty(),
                    Component.text(" §8• §7Insel"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#C7D96B>" + Bukkit.getOfflinePlayer(skyBlockIsland.getOwnerUniqueId()).getName())),
                    Component.empty(),
                    Component.text(" §8• §7Besucher"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#C7D96B>" + (player.getWorld().getPlayerCount() - 1))
                            .append(Component.text(" §8» §c/list"))),
                    Component.empty()
            );
        } else {

            fastBoard.updateLines(
                    Component.empty(),
                    Component.text(" §8• §7Kontostand"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#C7D96B>" + NumberUtil.formatBalance(skyBlockUser.getBalance()) + " ⛃")),
                    Component.empty(),
                    Component.text(" §8• §7Online"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#C7D96B>" + Bukkit.getOnlinePlayers().size())),
                    Component.empty(),
                    Component.text(" §8• §7Insel"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#C7D96B>Spawn")),
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
