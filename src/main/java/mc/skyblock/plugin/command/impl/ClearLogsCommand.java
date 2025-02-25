package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public class ClearLogsCommand extends AbstractCommand {


    public ClearLogsCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "clearlogs", null, "clearlog");
    }

    @Override
    public void run(Player player, String[] args) {
        Path path = Path.of("logs");
        int files = 0;
        int filesDeleted = 0;
        if (path.toFile().exists() && path.toFile().isDirectory()) {
            if (path.toFile().listFiles() == null) {
                player.sendMessage(ChatAction.of("Es gibt keine Logs zum löschen. (*.log.gz)"));
            } else {
                if (Objects.requireNonNull(path.toFile().listFiles()).length == 0) {
                    player.sendMessage(ChatAction.of("Es gibt keine Logs zum löschen. (*.log.gz)"));
                    return;
                }
            }

            for (java.io.File file : Objects.requireNonNull(path.toFile().listFiles())) {
                files++;
                if (file == null) continue;
                if (file.getName().endsWith(".log.gz")) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        filesDeleted++;
                    }
                }
            }

            player.sendMessage(ChatAction.of("Die Logs wurden gelöscht. (*.log.gz)"));
            player.sendMessage(ChatAction.of("Es wurden " + filesDeleted + " von " + files + " Logs gelöscht."));
        } else {
            player.sendMessage(ChatAction.failure("Die Logs konnten nicht gefunden werden. (*.log.gz)"));
            player.sendMessage(ChatAction.failure("Bitte kontaktiere einen Administrator."));
        }
    }
}
