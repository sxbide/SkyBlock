package eu.revanox.skyblock.util;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.Locale;

@UtilityClass
public class NumberUtil {

    public String formatBalance(double balance) {
        long integerPart = (long) balance;
        int cents = (int) ((balance - integerPart) * 100);

        String formattedInteger;
        if (integerPart >= 1000) {
            formattedInteger = String.format("%.1fk", integerPart / 1000.0).replace('.', ',');
        } else {
            formattedInteger = NumberFormat.getNumberInstance(Locale.GERMANY).format(integerPart);
        }
        return formattedInteger + "," + String.format("%02d", cents);
    }
}
