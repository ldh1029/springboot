package com.xhcoding;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Chars;

import java.util.*;

/**
 * Created by xin on 2018/1/12.
 */
public class Password {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();
        char[] chars = str.toCharArray();
        List<String> des = Lists.newArrayList("5", "6", "3", "7", "7", "0");
        List<String> src = Lists.newArrayList();
        for (char c : chars) {
            src.add(String.valueOf(c));
        }

        Iterator<String> iterator = src.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (!des.contains(next)) {
                iterator.remove();
            }
        }
        System.out.println(src);

    }
}
