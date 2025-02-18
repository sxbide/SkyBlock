package eu.revanox.skyblock.log;

import eu.revanox.skyblock.log.privatemessage.model.PrivateMessage;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.UUID;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractLog<E> {

    String logName;
    UUID logOwner;
    TreeMap<LocalDateTime, LogMessage<E>> logEntries;
    Class<E> entryClass;

    public AbstractLog(String logName, UUID logOwner, Class<E> entryClass) {
        this.logName = logName;
        this.logOwner = logOwner;
        this.logEntries = new TreeMap<>();
        this.entryClass = entryClass;
    }

    public void addEntry(LocalDateTime timestamp, LogMessage<E> entry) {
        this.logEntries.put(timestamp, entry);
    }

    public abstract E getLatestEntry();

    public abstract E getEntry(LocalDateTime timestamp);

    public void removeEntry(LocalDateTime timestamp) {
        this.logEntries.remove(timestamp);
    }

    public void clearLog() {
        this.logEntries.clear();
    }

    public void clearLogBefore(LocalDateTime timestamp) {
        this.logEntries.headMap(timestamp).clear();
    }

    public void clearLogAfter(LocalDateTime timestamp) {
        this.logEntries.tailMap(timestamp).clear();
    }

    public void clearLogBetween(LocalDateTime start, LocalDateTime end) {
        this.logEntries.subMap(start, end).clear();
    }

}
