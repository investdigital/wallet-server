package com.oxchains.bitcoin.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author oxchains
 * @time 2017-10-30 9:48
 * @nameMapWrapper
 * @desc:
 */
public class MapWrapper {
    public final Map m;

    public MapWrapper(Map m) {
        this.m = m;
    }

    public boolean mapBool(String key) {
        return mapBool(m, key);
    }

    public float mapFloat(String key) {
        return mapFloat(m, key);
    }

    public double mapDouble(String key) {
        return mapDouble(m, key);
    }

    public int mapInt(String key) {
        return mapInt(m, key);
    }

    public long mapLong(String key) {
        return mapLong(m, key);
    }

    public String mapStr(String key) {
        return mapStr(m, key);
    }

    public Date mapCTime(String key) {
        return mapCTime(m, key);
    }

    public BigDecimal mapBigDecimal(String key) {
        return mapBigDecimal(m, key);
    }

    public static boolean mapBool(Map m, String key) {
        return ((Boolean)m.get(key));
    }

    public static BigDecimal mapBigDecimal(Map m, String key) {
        return new BigDecimal((String) m.get(key));
    }

    public static float mapFloat(Map m, String key) {
        return ((Number)m.get(key)).floatValue();
    }

    public static double mapDouble(Map m, String key) {
        return ((Number)m.get(key)).doubleValue();
    }

    public static int mapInt(Map m, String key) {
        return ((Number)m.get(key)).intValue();
    }

    public static long mapLong(Map m, String key) {
        return ((Number)m.get(key)).longValue();
    }

    public static String mapStr(Map m, String key) {
        Object v = m.get(key);
        return v == null ? null : String.valueOf(v);
    }

    public static Date mapCTime(Map m, String key) {
        Object v = m.get(key);
        return v == null ? null : new Date(mapLong(m, key) * 1000);
    }

    @Override
    public String toString() {
        return String.valueOf(m);
    }
}
