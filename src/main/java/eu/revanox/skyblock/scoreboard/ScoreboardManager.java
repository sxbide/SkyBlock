package eu.revanox.skyblock.scoreboard;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.island.model.SkyBlockIsland;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.NumberUtil;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
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

        fastBoard.updateTitle("§7SkyBlock");

        if (skyBlockIsland != null) {
            fastBoard.updateLines(
                    "",
                    " §8• §c" + (skyBlockIsland.getOwnerUniqueId().equals(player.getUniqueId()) ? "§cBesitzer" : "§9Besucher"),
                    "",
                    " §8• §7Kontostand",
                    "  §8► §e§o" + NumberUtil.formatBalance(skyBlockUser.getBalance()) + " ⛃",
                    "",
                    " §8• §7Insel",
                    "  §8► §e§o" + Bukkit.getOfflinePlayer(skyBlockIsland.getOwnerUniqueId()).getName(),
                    "",
                    " §8• §7Besucher",
                    "  §8► §e§o" + (player.getWorld().getPlayerCount() - 1) + " §8» §c/list",
                    ""
            );
        } else {

            fastBoard.updateLines(
                    "",
                    " §8• §7Kontostand",
                    "  §8► §e§o" + NumberUtil.formatBalance(skyBlockUser.getBalance()) + " ⛃",
                    "",
                    " §8• §7Spieler",
                    "  §8► §e§o" + Bukkit.getOnlinePlayers().size(),
                    "",
                    " §8• §7Inseln",
                    "  §8► §e§oSpawn",
                    ""
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
