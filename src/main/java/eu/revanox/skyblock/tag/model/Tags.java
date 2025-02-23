package eu.revanox.skyblock.tag.model;

import eu.revanox.skyblock.util.ResourceIcons;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Tags {

    I_LOVE_SKYBLOCK(MiniMessage.miniMessage().deserialize("<#D6557F>I <#EA2525>❤ <#D6557F>SkyBlock"), TagRarity.COMMON, 10),
    GELD_GIGANT(
            Component.text(ResourceIcons.GOLD_INGOT.unicode())
                    .append(MiniMessage.miniMessage().deserialize("<gradient:#FFD900:#E9B96E> Geldgigant </gradient><reset>"))
                    .append(Component.text(ResourceIcons.GOLD_INGOT.unicode()))
            ,TagRarity.EPIC, 50000),
    RAINBOW(MiniMessage.miniMessage().deserialize("<red>🔥 <rainbow>Regenbogen</rainbow> <red>🔥"), TagRarity.RARE, 25000),
    DONATOR(MiniMessage.miniMessage().deserialize("<#E43A96>★ <gradient:#B308FB:#C2F68E>Unterstüt</gradient><gradient:#C2F68E:#C2F68E>zer</gradient> <#E43A96>★"), TagRarity.EXCLUSIVE, -1),
    FOUNDER_LEGEND(MiniMessage.miniMessage().deserialize("<b><gradient:#FF006F:#FF9B00:#00F9FF:#8000FF>◆ Gründerlegende ◆</gradient>"), TagRarity.EXCLUSIVE, -1);

    Component tagText;
    TagRarity rarity;
    double price;

    public static List<Tags> sortByRarity() {
        return Arrays.stream(values()).sorted((tag1, tag2) -> tag2.getRarity().compareTo(tag1.getRarity())).toList().reversed();
    }

    public static Tags getByDisplayName(String displayName) {
        return Arrays.stream(values()).filter(tags -> tags.getTagText().equals(MiniMessage.miniMessage().deserialize(displayName))).findFirst().orElse(null);
    }

}
