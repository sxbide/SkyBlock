package eu.revanox.skyblock.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@AllArgsConstructor
@Getter
public enum Tags {

    I_LOVE_SKYBLOCK(MiniMessage.miniMessage().deserialize("<#D6557F>I <#EA2525>❤ <#D6557F>SkyBlock"), TagRarity.COMMON, -1),
    GELD_GIGANT(MiniMessage.miniMessage().deserialize("<gradient:#FFD900:#E9B96E>Geldgigant</gradient>")
            ,TagRarity.RARE, 1000),
    FOUNDER_LEGEND(MiniMessage.miniMessage().deserialize("<b><gradient:#FF006F:#FF9B00:#00F9FF:#8000FF>◆ Gründerlegende ◆</gradient>"), TagRarity.EXCLUSIVE, -1);

    Component tagText;
    TagRarity rarity;
    double price;

}
