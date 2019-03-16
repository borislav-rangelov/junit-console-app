package com.brangelov.junitapp;

public class StringUtil {

    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }
}
