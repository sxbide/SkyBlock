package eu.revanox.skyblock.log.privatemessage;

import eu.revanox.skyblock.log.AbstractLog;
import eu.revanox.skyblock.log.LogMessage;
import eu.revanox.skyblock.log.privatemessage.model.PrivateMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrivateMessageLog extends AbstractLog<PrivateMessage> {

    public PrivateMessageLog(UUID logOwner) {
        super("privateMessages", logOwner, PrivateMessage.class);
    }

    public List<PrivateMessage> getAllUnreadMessages() {
        List<PrivateMessage> unreadMessages = new ArrayList<>();
        for (LogMessage<PrivateMessage> messageEntry : this.logEntries.values()) {
            PrivateMessage message = (PrivateMessage) messageEntry;
            if (message.getReceiver().equals(logOwner) && !message.isRead()) {
                unreadMessages.add(message);
            }
        }
        return unreadMessages;
    }

    @Override
    public PrivateMessage getLatestEntry() {
        return (PrivateMessage) logEntries.lastEntry().getValue();
    }

    @Override
    public PrivateMessage getEntry(LocalDateTime timestamp) {
        return (PrivateMessage) logEntries.get(timestamp);
    }
}
