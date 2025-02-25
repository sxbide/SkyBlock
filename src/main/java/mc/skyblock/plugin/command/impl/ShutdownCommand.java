package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class ShutdownCommand extends AbstractCommand {

    BukkitTask task;

    public ShutdownCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "shutdown", "skyblock.command.shutdown", "stop");
    }

    @Override
    public void run(Player player, String[] args) {
        AtomicInteger seconds = new AtomicInteger(10);
        if (args.length > 0) {
            try {
                seconds.set(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                player.sendMessage(ChatAction.failure("Bitte gebe eine gültige Zahl an."));
                return;
            }
        }
        if (seconds.get() < 10) {
            player.sendMessage(ChatAction.failure("Die Zeit muss mindestens 10 Sekunden betragen."));
            return;
        }
        player.sendMessage(ChatAction.of("Shutdown eingeleitet."));
        Audience.audience(Bukkit.getOnlinePlayers()).sendMessage(
                ChatAction.info("Der Server wird in " + seconds + " Sekunden gestoppt.")
        );
        task = new BukkitRunnable() {
            @Override
            public void run() {
                seconds.getAndDecrement();
                if (seconds.get() % 10 == 0 || seconds.get() <= 5) {
                    Audience.audience(Bukkit.getOnlinePlayers()).sendMessage(
                            ChatAction.info("Der Server wird in " + seconds + " Sekunden gestoppt.")
                    );
                }
                if (seconds.get() == 0) {
                    restartServer(task);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void restartServer(BukkitTask task) {
        task.cancel();
        Bukkit.spigot().restart();
    }
}
