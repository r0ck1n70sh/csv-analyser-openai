package com.r0ck1n70sh.csvanalyzer.utils;

import java.util.Random;

public class StringUtils {
    public static String generateRandomAlphaNumericString(int len) {
        final int startAscii = 48; // '0;
        final int endNumericAscii = startAscii + 9; // '9';

        final int endAscii = 122; // 'z'
        final int startAlphaAscii = endAscii - 26; // 'a';

        Random random = new Random();
        return random.ints(startAscii, endAscii + 1)
                .filter(i -> (i < endNumericAscii) || (i > startAlphaAscii))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
