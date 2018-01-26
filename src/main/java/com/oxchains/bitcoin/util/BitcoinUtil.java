package com.oxchains.bitcoin.util;

/**
 * @author oxchains
 * @time 2017-10-30 9:52
 * @nameBitcoinUtil
 * @desc:
 */
public class BitcoinUtil {
    public static double normalizeAmount(double amount) {
        return (long)(0.5d + (amount / 0.00000001)) * 0.00000001;
    }
}
