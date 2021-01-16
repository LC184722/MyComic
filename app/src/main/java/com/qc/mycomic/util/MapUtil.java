package com.qc.mycomic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/13 0:02
 * @ver 1.0
 */
public class MapUtil {

    public static <K, V> int indexOf(Map<K, V> map, Object key) {
        int i = 0;
        for (Object o : map.keySet()) {
            if (Objects.equals(o, key)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static <K, V> Map.Entry<K, V> getFirst(Map<K, V> map) {
        return map.entrySet().iterator().next();
    }

    public static <K, V> V getFirstValue(Map<K, V> map) {
        if (map != null && !map.isEmpty()) {
            return map.values().iterator().next();
        }
        return null;
    }

    public static <K, V> List<K> getKeyList(Map<K, V> map) {
        return new ArrayList<>(map.keySet());
    }

    public static <K, V> V getValueByIndex(Map<K, V> map, int index) {
        int i = 0;
        for (V value : map.values()) {
            if (i++ == index) {
                return value;
            }
        }
        return null;
    }

}
