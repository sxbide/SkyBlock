package mc.skyblock.plugin.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;

@UtilityClass
public class TimeUtil {

    public String formatTime(Duration duration, boolean shortString, boolean withSeconds) {
        long seconds = duration.getSeconds();
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder response = new StringBuilder();

        if (days > 0) {
            response.append(days).append(shortString ? "d" : " Tag").append(days > 1 && !shortString ? "e" : "").append(", ");
        }
        if (hours > 0) {
            response.append(hours).append(shortString ? "h" : " Stunde").append(hours > 1 && !shortString ? "n" : "").append(", ");
        }
        if (minutes > 0) {
            response.append(minutes).append(shortString ? "m" : " Minute").append(minutes > 1 && !shortString ? "n" : "").append(", ");
        }
        if (withSeconds) {
            response.append(secs).append(shortString ? "s" : " Sekunde").append(secs > 1 && !shortString ? "n" : "");
        }

        if (response.toString().isEmpty()) {
            return "0" + (shortString ? "s" : " Sekunden");
        }
        if (response.toString().endsWith(", ")) {
            return response.substring(0, response.length() - 2);
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

    public String formatTime(LocalDateTime start, LocalDateTime end, boolean shortString, boolean withSeconds) {
        return formatTime(Duration.between(start, end), shortString, withSeconds);
    }

    public String formatTime(LocalDateTime start, LocalDateTime end, boolean shortString) {
        return formatTime(Duration.between(start, end), shortString);
    }

    public String formatTime(LocalDateTime start, LocalDateTime end) {
        return formatTime(Duration.between(start, end));
    }

}
