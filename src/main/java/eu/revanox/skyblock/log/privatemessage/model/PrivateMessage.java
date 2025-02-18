package eu.revanox.skyblock.log.privatemessage.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.revanox.skyblock.log.LogMessage;
import eu.revanox.skyblock.log.privatemessage.PrivateMessageLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PrivateMessage implements LogMessage<PrivateMessage> {

    @Id
    LocalDateTime timestamp;
    UUID sender;
    UUID receiver;
    String message;
    boolean offlineMessage;
    boolean read;

    @Override
    public String toBase64() {
        return Base64Coder.encodeString(String.format("%s;%s;%s;%s;%s;%s", this.sender.toString(), this.receiver.toString(), this.message, this.timestamp.toString(), this.offlineMessage, this.read));
    }

    public static PrivateMessage fromBase64(String base64) {
        String[] parts = Base64Coder.decodeString(base64).split(";");
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(UUID.fromString(parts[0]));
        privateMessage.setReceiver(UUID.fromString(parts[1]));
        privateMessage.setMessage(parts[2]);
        privateMessage.setTimestamp(LocalDateTime.parse(parts[3]));
        privateMessage.setOfflineMessage(Boolean.parseBoolean(parts[4]));
        privateMessage.setRead(Boolean.parseBoolean(parts[5]));
        return privateMessage;
    }
}
