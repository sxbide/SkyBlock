package mc.skyblock.plugin.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mc.skyblock.plugin.util.Rarity;
import mc.skyblock.plugin.util.ResourceIcons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum Tags {

    // MiniMessage.miniMessage().deserialize("<#D6557F>I <#EA2525>❤ <#D6557F>SkyBlock")
    I_LOVE_SKYBLOCK(Component.text("§r" + ResourceIcons.SUMMER_2025_TITLE.unicode()), Rarity.MYTHIC, 10),
    GELD_GIGANT(
            MiniMessage.miniMessage().deserialize("<gradient:#FFD900:#E9B96E> Geldgigant </gradient><reset>"), Rarity.EPIC, 50000),
    RAINBOW(MiniMessage.miniMessage().deserialize("<red>🔥 <rainbow>Regenbogen</rainbow> <red>🔥"), Rarity.RARE, 25000),
    DONATOR(MiniMessage.miniMessage().deserialize("<#E43A96>★ <gradient:#B308FB:#C2F68E>Unterstüt</gradient><gradient:#C2F68E:#C2F68E>zer</gradient> <#E43A96>★"), Rarity.UNCOMMON, -1),
    FOUNDER_LEGEND(MiniMessage.miniMessage().deserialize("<b><gradient:#FF006F:#FF9B00:#00F9FF:#8000FF>◆ Gründerlegende ◆</gradient>"), Rarity.EXCLUSIVE, -1);

    Component tagText;
    Rarity rarity;
    double price;

    public static List<Tags> sortByRarity() {
        return Arrays.stream(values()).sorted((tag1, tag2) -> tag2.getRarity().compareTo(tag1.getRarity())).toList().reversed();
    }

    public static Tags getByDisplayName(String displayName) {
        return Arrays.stream(values()).filter(tags -> tags.getTagText().equals(MiniMessage.miniMessage().deserialize(displayName))).findFirst().orElse(null);
    }

    /**
     * Color correction for custom tags using ResourceIcons
     *
     * @return fixed Component with white color
     */
    public Component getFixedTag() {
        return tagText.color(NamedTextColor.WHITE);
    }

}
