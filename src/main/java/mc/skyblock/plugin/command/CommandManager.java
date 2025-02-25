package mc.skyblock.plugin.command;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import org.bukkit.command.CommandMap;
import org.reflections.Reflections;

import java.util.Set;

public class CommandManager {

    SkyBlockPlugin plugin;
    CommandMap commandMap;

    public CommandManager(SkyBlockPlugin plugin) {
        this.plugin = plugin;
        this.commandMap = plugin.getServer().getCommandMap();

        this.registerCommands();
    }

    private void registerCommands() {
        Reflections reflections = new Reflections("mc.skyblock.plugin.command.impl");
        Set<Class<? extends AbstractCommand>> commands = reflections.getSubTypesOf(AbstractCommand.class);
        commands.forEach(command -> {
            try {
                AbstractCommand skyBlockCommand = command.getDeclaredConstructor(SkyBlockPlugin.class).newInstance(this.plugin);
                this.commandMap.register("skyblock", skyBlockCommand);
                this.plugin.getLogger().info("Registered command: " + skyBlockCommand.getName());
            } catch (Exception e) {
                this.plugin.getLogger().warning("Failed to register command: " + command.getName() + ", skipping... - Stacktrace:");
                e.printStackTrace();
            }
        });
        this.plugin.getLogger().info("Registered all commands (" + commands.size() + ")");
    }

}
