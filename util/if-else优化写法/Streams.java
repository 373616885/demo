package com.qin.mp.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Streams {
    public void inForEach() {
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ints.stream().forEach(i -> {
            if (i.intValue() % 2 == 0) {
                System.out.println("i is even");
            } else {
                System.out.println("i is old");
            }
        });

        // 上面的优化写法
        List<Integer> ints2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Stream<Integer> evenIntegers = ints2.stream().filter(i -> i.intValue() % 2 == 0);
        Stream<Integer> oddIntegers = ints2.stream().filter(i -> i.intValue() % 2 != 0);

        evenIntegers.forEach(i -> System.out.println("i is even"));
        oddIntegers.forEach(i -> System.out.println("i is old"));
    }


}
