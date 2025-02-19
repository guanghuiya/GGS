package com.meiqiu.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author sgh
 * @Date 2025/2/18
 * @Time 12:47
 */
public class Test {
    public static void main(String[] args) {
        String a = "aa";
        System.out.println(a);
        StringBuffer b = new StringBuffer("bb");
        System.out.println(b.toString());

        Map<String, String> map = new HashMap<>();
        map.hashCode();

    }
}
