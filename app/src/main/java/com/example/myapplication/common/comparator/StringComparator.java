package com.example.myapplication.common.comparator;

import com.example.myapplication.utils.StringUtils;

import java.util.Comparator;



public class StringComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        return StringUtils.compare(s1, s2);
    }

    @Override
    public Comparator<String> reversed() {
        return (s1, s2) -> StringUtils.compare(s2, s1);
    }

    public static StringComparator create(){
        return new StringComparator();
    }
}
