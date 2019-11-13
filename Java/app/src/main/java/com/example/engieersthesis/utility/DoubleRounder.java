package com.example.engieersthesis.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleRounder {
    public static double roundDouble(double valueToRound, int decimalPlaces) {
        if (decimalPlaces < 0)
            throw new IllegalArgumentException("decimalPlaces must be greater than 0!");

        BigDecimal bd = BigDecimal.valueOf(valueToRound);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
