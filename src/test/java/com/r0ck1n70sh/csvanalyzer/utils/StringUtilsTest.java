package com.r0ck1n70sh.csvanalyzer.utils;


import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void verifyGenerateRandomAlphaNumericString() {
        int size = 10;
        String random = StringUtils.generateRandomAlphaNumericString(size);

        assertEquals(random.length(), size);
    }

    @Test
    public void verifyGenerateRandomAlphaNumericString2() {
        int size = 10;

        String random1 = StringUtils.generateRandomAlphaNumericString(size);
        String random2 = StringUtils.generateRandomAlphaNumericString(size);

        assertNotEquals(random1, random2);
    }
}