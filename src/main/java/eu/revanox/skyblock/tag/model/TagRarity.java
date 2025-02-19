package eu.revanox.skyblock.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum TagRarity {

    COMMON(MiniMessage.miniMessage().deserialize("<#818181>ɢᴇᴡöʜɴʟɪᴄʜ")),
    RARE(MiniMessage.miniMessage().deserialize("<#49C34D>ѕᴇʟᴛᴇɴ")),
    EPIC(MiniMessage.miniMessage().deserialize("<#C34949>ᴇᴘɪѕᴄʜ")),
    EXCLUSIVE(MiniMessage.miniMessage().deserialize("<rainbow><b>ᴇхᴋʟᴜѕɪᴠ</b></rainbow>"));

    Component displayName;
}
