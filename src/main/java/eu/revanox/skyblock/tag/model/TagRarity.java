package eu.revanox.skyblock.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Getter
@AllArgsConstructor
public enum TagRarity {

    COMMON(MiniMessage.miniMessage().deserialize("<gradient:#818181:#818181>ɢᴇ</gradient><gradient:#818181:#797979>ᴡöʜɴʟ</gradient><gradient:#797979:#797979>ɪᴄʜ</gradient>")),
    RARE(MiniMessage.miniMessage().deserialize("<gradient:#49C34D:#49C34D>ѕ</gradient><gradient:#49C34D:#8AD947>ᴇʟᴛ</gradient><gradient:#8AD947:#8AD947>ᴇɴ</gradient>")),
    SUPER_RARE(MiniMessage.miniMessage().deserialize("<gradient:#4951C3:#4951C3>ѕᴜ</gradient><gradient:#4951C3:#6747D9>ᴘᴇʀ ѕᴇ</gradient><gradient:#6747D9:#6747D9>ʟᴛᴇɴ</gradient>")),
    EXCLUSIVE(MiniMessage.miniMessage().deserialize("<b><gradient:#D200FF:#D200FF>ᴇ</gradient><gradient:#D200FF:#D7D947>хᴋʟᴜѕ</gradient><gradient:#D7D947:#D7D947>ɪᴠ</gradient></b>"));

    Component displayName;
}
