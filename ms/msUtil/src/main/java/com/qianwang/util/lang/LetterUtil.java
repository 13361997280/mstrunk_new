package com.qianwang.util.lang;

import java.util.Arrays;
import java.util.List;

/**
 * Created by songjie on 16/10/19.
 */
public class LetterUtil {

    private final static
    List<String> allLetter = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "*"
    );


    public static Boolean assertLetter(String letter) {
        return allLetter.contains(letter);
    }
}
