package com.qc.mycomic.test;

import com.qc.mycomic.model.MyMap;

public class TestUtil {

    public static void main(String[] args) {
        MyMap<Integer, String> map = new MyMap<>();
        map.put(1, "2");
        map.put(2, "2");
        map.put(3, "45");
        map.put(11, "2");
        map.put(21, "2");
        map.put(31, "45");
        map.put(12, "2");
        map.put(22, "2");
        map.put(32, "45");
        System.out.println("map = " + map);
        System.out.println("map.getFirst() = " + map.getFirst());
        System.out.println("map.getLast() = " + map.getLast());
    }
}

