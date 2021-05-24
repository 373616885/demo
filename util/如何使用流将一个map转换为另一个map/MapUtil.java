package com.qin.mp.web;

import com.qin.mp.domain.ApiDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MapUtil {
    public static void main(String[] args) {
        HashMap<String, String> src = new HashMap<>();
        TreeMap<String, String> dest = src.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue, (a, b) -> a, TreeMap::new));

        Map<String, Set<String>> collect = src.entrySet().stream()
                .collect(Collectors.toMap(
                        key -> key.getKey(),
                        value -> Set.of(value.getValue())));

    }
}
