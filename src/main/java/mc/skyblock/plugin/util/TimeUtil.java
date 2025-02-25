package mc.skyblock.plugin.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class TimeUtil {

    public String formatTime(Duration duration, boolean shortString, boolean withSeconds) {
        long millis = duration.toMillis();
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        StringBuilder response = new StringBuilder();

        if (days > 0) {
            response.append(days).append(shortString ? "d" : " Tag").append(days > 1 ? "e" : "").append(", ");
        }

        if (hours > 0) {
            response.append(hours % 24).append(shortString ? "h" : " Stunde").append(hours % 24 > 1 ? "n" : "").append(", ");
        }

        if (minutes > 0) {
            response.append(minutes % 60).append(shortString ? "m" : " Minute").append(minutes % 60 > 1 ? "n" : "").append(", ");
        }

        if (withSeconds) {
            response.append(seconds % 60).append(shortString ? "s" : " Sekunde").append(seconds % 60 > 1 ? "n" : "");
        }

        return response.toString();
    }

    public String formatTime(Duration duration, boolean shortString) {
        return formatTime(duration, shortString, true);
    }

    public String formatTime(Duration duration) {
        return formatTime(duration, false, true);
    }

    public String formatTime(long millis, boolean shortString, boolean withSeconds) {
        return formatTime(Duration.ofMillis(millis), shortString, withSeconds);
    }

    public String formatTime(long millis, boolean shortString) {
        return formatTime(Duration.ofMillis(millis), shortString, true);
    }

    public String formatTime(long millis) {
        return formatTime(Duration.ofMillis(millis), false, true);
    }

}
