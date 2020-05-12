package com.qin.base;

import java.util.HashMap;

public class MapBase {

    public static void main(String[] args) throws InterruptedException {
        int MAXIMUM_CAPACITY = 1 << 30;
        System.out.println(MAXIMUM_CAPACITY);
        System.out.println((MAXIMUM_CAPACITY-1)*2);
        System.out.println(Integer.MAX_VALUE);
        HashMap<Integer,Integer> map = new HashMap<>(16);
        map.put(1, 1);
        System.out.println(map);
    }
}

