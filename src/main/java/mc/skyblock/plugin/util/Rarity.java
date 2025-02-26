package mc.skyblock.plugin.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Rarity {

    COMMON("Gewöhnlich", MiniMessage.miniMessage().deserialize("<#818181>ɢᴇᴡöʜɴʟɪᴄʜ</#818181>"), 1),
    UNCOMMON("Ungewöhnlich", MiniMessage.miniMessage().deserialize("<#49C34D>ᴜɴɢᴇᴡöʜɴʟɪᴄʜ</#49C34D>"), 2),
    RARE("Selten", MiniMessage.miniMessage().deserialize("<#4D6DC3>ꜱᴇʟᴛᴇɴ</#4D6DC3>"), 3),
    EPIC("Episch", MiniMessage.miniMessage().deserialize("<#7621CC>ᴇᴘɪꜱᴄʜ</#7621CC>"), 4),
    LEGENDARY("Legendär", MiniMessage.miniMessage().deserialize("<#FFD800>ʟᴇɢᴇɴᴅäʀ</#FFD800>"), 5),
    MYTHIC("Mythisch", MiniMessage.miniMessage().deserialize("<gradient:#ff0036:red>ᴍʏᴛʜɪꜱᴄʜ</gradient>"), 6),
    EXCLUSIVE("Exklusiv", MiniMessage.miniMessage().deserialize("<rainbow>ᴇxᴋʟᴜꜱɪᴠ</rainbow>"), 7)
    ;

    String name;
    Component displayName;
    int weight;

    public static Rarity getRarityByWeight(int weight) {
        for (Rarity rarity : values()) {
            if (rarity.getWeight() == weight) {
                return rarity;
            }
        }
        return null;
    }

    public static Rarity getRarityByName(String name) {
        for (Rarity rarity : values()) {
            if (rarity.getName().equalsIgnoreCase(name)) {
                return rarity;
            }
        }
        return null;
    }

    public static List<Rarity> sortRaritiesByWeight() {
        List<Rarity> rarities = new ArrayList<>(Arrays.asList(values()));
        rarities.sort(Comparator.comparingInt(Rarity::getWeight));
        return rarities;
    }


}
