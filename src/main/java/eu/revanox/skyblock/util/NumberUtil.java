package eu.revanox.skyblock.util;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

@UtilityClass
public class NumberUtil {
    private static final Random RANDOM = new Random();

    public String formatBalance(double balance) {
        long integerPart = (long) balance;
        int cents = (int) ((balance - integerPart) * 100);
        String formattedInteger = NumberFormat.getNumberInstance(Locale.GERMANY).format(integerPart);
        String formattedCents = String.format("%02d", cents);
        return formattedInteger + "," + formattedCents;
    }


    public int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

}
