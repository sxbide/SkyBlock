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

        Component tabHeader = Component.newline().append(ChatAction.getPrefixGradient().appendNewline().append(Component.text("§7§odein skyblock abenteuer")).appendNewline());
        Component tabFooter = Component.newline().append(MiniMessage.miniMessage().deserialize("<gradient:#FAEDCB:#FAEDCB>pixelbande.net</gradient>"))
                .appendNewline();

        player.sendPlayerListHeaderAndFooter(tabHeader, tabFooter);

        if (skyBlockIsland != null) {
            fastBoard.updateLines(
                    Component.empty(),
                    Component.text(" §8• §c" + (skyBlockIsland.getOwnerUniqueId().equals(player.getUniqueId()) ? "§cBesitzer" : "§9Besucher")),
                    Component.empty(),
                    Component.text(" §8• §7Kontostand"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#FDAF65>" + NumberUtil.formatBalance(skyBlockUser.getBalance()) + " ⛃")),
                    Component.empty(),
                    Component.text(" §8• §7Insel"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#FDAF65>" + Bukkit.getOfflinePlayer(skyBlockIsland.getOwnerUniqueId()).getName())),
                    Component.empty(),
                    Component.text(" §8• §7Besucher"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#FDAF65>" + (player.getWorld().getPlayerCount() - 1) + " §8» §c/list")),
                    Component.empty()
            );
        } else {

            fastBoard.updateLines(
                    Component.empty(),
                    Component.text(" §8• §7Kontostand"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#FDAF65>" + NumberUtil.formatBalance(skyBlockUser.getBalance()) + " ⛃")),
                    Component.empty(),
                    Component.text(" §8• §7Spieler"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#FDAF65>" + Bukkit.getOnlinePlayers().size())),
                    Component.empty(),
                    Component.text(" §8• §7Insel"),
                    Component.text("  §8► ").append(MiniMessage.miniMessage().deserialize("<color:#FDAF65>Spawn")),
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
