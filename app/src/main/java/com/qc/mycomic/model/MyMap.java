package com.qc.mycomic.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class MyMap<K, V> extends LinkedHashMap<K, V> {

    public V getValueByIndex(int index) {
        int i = 0;
        for (Entry<K, V> kvEntry : entrySet()) {
            if (i++ == index) {
                return kvEntry.getValue();
            }
        }
        return null;
    }

    public V getFirstValue() {
        return getValueByIndex(0);
    }

    public V getLastValue() {
        return getValueByIndex(size() - 1);
    }

    public Entry<K, V> getByIndex(int index) {
        int i = 0;
        for (Entry<K, V> kvEntry : entrySet()) {
            if (i++ == index) {
                return kvEntry;
            }
        }
        return null;
    }

    public Entry<K, V> getFirst() {
        return getByIndex(0);
    }

    public Entry<K, V> getLast() {
        return getByIndex(size() - 1);
    }

    public int indexOf(K key) {
        int index = 0;
        for (Entry<K, V> kvEntry : entrySet()) {
            if (Objects.equals(key, kvEntry.getKey())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public List<V> getValueList() {
        List<V> list = new LinkedList<>();
        for (Entry<K, V> kvEntry : entrySet()) {
            list.add(kvEntry.getValue());
        }
        return list;
    }

    public List<K> getKeyList() {
        List<K> list = new LinkedList<>();
        for (Entry<K, V> kvEntry : entrySet()) {
            list.add(kvEntry.getKey());
        }
        return list;
    }
}
