package eu.revanox.skyblock.util;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.Locale;

@UtilityClass
public class NumberUtil {

    public String formatBalance(double balance) {
        long integerPart = (long) balance;
        int cents = (int) ((balance - integerPart) * 100);
        String formattedInteger = NumberFormat.getNumberInstance(Locale.GERMANY).format(integerPart);
        String formattedCents = String.format("%02d", cents);
        return formattedInteger + "," + formattedCents;
    }

}
