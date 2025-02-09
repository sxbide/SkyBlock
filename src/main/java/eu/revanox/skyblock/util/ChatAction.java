package eu.revanox.skyblock.util;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
@FieldDefaults(makeFinal = true)
public class ChatAction {

    private String prefix = "§a✔ §7";
    private String failurePrefix = "§c✘ §c";
    @Getter
    private String noPermission = failurePrefix + "§cDazu hast du keine Berechtigung!";
    @Getter
    private String offline = failurePrefix + "§cDieser Spieler wurde nicht gefunden!";

    public String of(String message) {
        return prefix + message;
    }

    public String failure(String message) {
        return failurePrefix + message;
    }
}

