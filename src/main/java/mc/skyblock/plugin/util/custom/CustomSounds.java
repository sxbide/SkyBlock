package mc.skyblock.plugin.util.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Accessors(fluent = true)
public enum CustomSounds {

    BACKGROUND_ECLIPSE(Key.key(Key.MINECRAFT_NAMESPACE, "skyblock.background.eclipse")),
    ERROR(Key.key(Key.MINECRAFT_NAMESPACE, "skyblock.error")),
    LEVEL_UP(Key.key(Key.MINECRAFT_NAMESPACE, "skyblock.level_up")),
    WINNING(Key.key(Key.MINECRAFT_NAMESPACE, "skyblock.winning")),
    CASHIER(Key.key(Key.MINECRAFT_NAMESPACE, "skyblock.cashier")),
    NOTIFICATION(Key.key(Key.MINECRAFT_NAMESPACE, "skyblock.notification")),
    ;

    Key key;

    public void playSound(Player player, float volume, float pitch, Location location) {
        player.playSound(Sound.sound(this.key, Sound.Source.NEUTRAL, volume, pitch), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void playSound(Collection<? extends Player> players, float volume, float pitch, Location location) {
        players.forEach(player -> player.playSound(Sound.sound(this.key, Sound.Source.NEUTRAL, volume, pitch), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

}
