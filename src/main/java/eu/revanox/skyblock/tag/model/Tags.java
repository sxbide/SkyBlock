package eu.revanox.skyblock.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@AllArgsConstructor
@Getter
public enum Tags {

    I_LOVE_SKYBLOCK(MiniMessage.miniMessage().deserialize("<#D6557F>I <#EA2525>❤ <#D6557F>SkyBlock"), TagRarity.COMMON, -1),
    MILLIONÄR(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.RARE, 1000),
    TEST_1(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 1000),
    TEST_3(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 10000),
    TEST_4(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 10000),
    TEST_5(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 100000),
    TEST_6(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 1000000),
    TEST_7(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 10000000),
    TEST_8(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 312.31),
    TEST_9(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 432.44),
    TEST_11(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 123),
    TEST_13(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 2313),
    TEST_15(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.SUPER_RARE, 65464),
    TEST_2(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , TagRarity.EXCLUSIVE, 1000);

    Component tagText;
    TagRarity rarity;
    double price;

}
