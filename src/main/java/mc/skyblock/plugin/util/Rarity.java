package mc.skyblock.plugin.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Rarity {

    COMMON("Gewöhnlich", MiniMessage.miniMessage().deserialize("<#818181>ɢᴇᴡöʜɴʟɪᴄʜ</#818181>"), 1, Color.fromRGB(129, 129, 129)),
    UNCOMMON("Ungewöhnlich", MiniMessage.miniMessage().deserialize("<#49C34D>ᴜɴɢᴇᴡöʜɴʟɪᴄʜ</#49C34D>"), 2, Color.fromRGB(73, 195, 77)),
    RARE("Selten", MiniMessage.miniMessage().deserialize("<#4D6DC3>ꜱᴇʟᴛᴇɴ</#4D6DC3>"), 3, Color.fromRGB(77, 109, 195)),
    EPIC("Episch", MiniMessage.miniMessage().deserialize("<#7621CC>ᴇᴘɪꜱᴄʜ</#7621CC>"), 4, Color.fromRGB(118, 33, 204)),
    LEGENDARY("Legendär", MiniMessage.miniMessage().deserialize("<#FFD800>ʟᴇɢᴇɴᴅäʀ</#FFD800>"), 5, Color.fromRGB(255, 216, 0)),
    MYTHIC("Mythisch", MiniMessage.miniMessage().deserialize("<gradient:#ff0036:red>ᴍʏᴛʜɪꜱᴄʜ</gradient>"), 6, Color.fromRGB(255, 0, 54)),
    EXCLUSIVE("Exklusiv", MiniMessage.miniMessage().deserialize("<rainbow>ᴇxᴋʟᴜꜱɪᴠ</rainbow>"), 7, Color.RED);

    public static final Rarity[] VALUES = values();
    String name;
    Component displayName;
    int weight;
    Color color;

    public static Rarity getRarityByWeight(int weight) {
        for (Rarity rarity : VALUES) {
            if (rarity.getWeight() == weight) {
                return rarity;
            }
        }
        return null;
    }

    public static Rarity getRarityByName(String name) {
        for (Rarity rarity : VALUES) {
            if (rarity.getName().equalsIgnoreCase(name)) {
                return rarity;
            }
        }
        return null;
    }

    public static List<Rarity> sortRaritiesByWeight() {
        List<Rarity> rarities = new ArrayList<>(Arrays.asList(VALUES));
        rarities.sort(Comparator.comparingInt(Rarity::getWeight));
        return rarities;
    }


    public static Rarity getRandomRarity() {
        return VALUES[(int) (Math.random() * VALUES.length)];
    }
}
