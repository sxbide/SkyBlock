package mc.skyblock.plugin.shop.model.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum ShopCurrencyFormat {

    COINS("Coins", "⛃"),
    GOLD_PIECES("Goldstücke", null);

    String displayName;
    String icon;
}
