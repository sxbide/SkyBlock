package mc.skyblock.plugin.command.model;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractCommand extends Command {

    protected SkyBlockPlugin plugin;
    String permission;

    public AbstractCommand(@NotNull SkyBlockPlugin plugin, @NotNull String name, @Nullable String permission) {
        super(name);
        this.plugin = plugin;
        this.permission = permission;
    }

    public AbstractCommand(@NotNull SkyBlockPlugin plugin, @NotNull String name, @Nullable String permission, @Nullable String... aliases) {
        super(name, "", "", (aliases != null && aliases.length != 0) ? List.of(aliases) : List.of());
        this.plugin = plugin;
        this.permission = permission;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatAction.failure("§cDu musst ein Spieler sein, um diesen Befehl auszuführen."));
            return false;
        }
        if (permission != null && !player.hasPermission(permission)) {
            player.sendMessage(ChatAction.getNoPermission());
            return false;
        }
        run(player, strings);
        return true;
    }

    public abstract void run(Player player, String[] args);
}
