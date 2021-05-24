package com.qin.mp;

import java.util.UUID;

/**
 * @author qinjp
 * @date 2020/10/29
 */
public class s {
    public static void main(String[] args) {

        UUID uuid = UUID.randomUUID();
        System.out.println( uuid.toString().replace("-",""));
    }
}
