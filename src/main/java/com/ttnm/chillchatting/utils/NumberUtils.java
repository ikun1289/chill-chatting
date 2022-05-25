package com.ttnm.chillchatting.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/22
 * Time      : 10:10
 * Filename  : NumberUtils
 */
public class NumberUtils {
    public static double round(double rate) {
        return (double) Math.round(rate * 100.0) / 100;
    }

    public static double roundUp(double rate, int scale) {
        return new BigDecimal(rate).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
}
