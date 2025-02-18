package eu.revanox.skyblock.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@AllArgsConstructor
@Getter
public enum Tags {

    I_LOVE_SKYBLOCK(MiniMessage.miniMessage().deserialize("<#D6557F>I <#EA2525>❤ <#D6557F>SkyBlock"), -1),
    MILLIONÄR(MiniMessage.miniMessage().deserialize("<gradient:#FCDD2A:#FCDD2A>M</gradient><gradient:#FCDD2A:#EE9828>illio</gradient><gradient:#EE9828:#EE9828>när</gradient>")
            , 1000);

    Component tagText;
    double price;

}
