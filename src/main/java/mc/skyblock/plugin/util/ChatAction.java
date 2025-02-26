package mc.skyblock.plugin.util;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;

@UtilityClass
@FieldDefaults(makeFinal = true)
public class ChatAction {

    @Getter
    private final Component prefixGradient = MiniMessage.miniMessage().deserialize("<gradient:#E2A574:#E0EF50>Blockarion</gradient>");
    private final Component failurePrefix = MiniMessage.miniMessage().deserialize("<#fb1d1d>✘ ");
    private final Component prefix = MiniMessage.miniMessage().deserialize("<#6cd414>✔ ");
    private final Component info = MiniMessage.miniMessage().deserialize("<#c0f0fb>⚑ ");

    @Getter
    private final Component noPermission = failurePrefix.append(MiniMessage.miniMessage().deserialize("<#fb1d1d>Dazu hast du keine Berechtigung."));
    @Getter
    private final Component offline = failurePrefix.append(MiniMessage.miniMessage().deserialize("<#fb1d1d>Dieser Spieler wurde nicht am Server gefunden."));

    public Component of(String message) {
        return prefix.append(MiniMessage.miniMessage().deserialize("<#6cd414>" + ChatColor.stripColor(message)));
    }

    public Component gray(String message) {
        return MiniMessage.miniMessage().deserialize("<#a6a19d> › " + ChatColor.stripColor(message));
    }

    public Component failure(String message) {
        return failurePrefix.append(MiniMessage.miniMessage().deserialize("<#fb1d1d>" + ChatColor.stripColor(message)));
    }

    public Component info(String message) {
        return info.append(MiniMessage.miniMessage().deserialize("<#a6a19d>" + ChatColor.stripColor(message)));
    }

}


