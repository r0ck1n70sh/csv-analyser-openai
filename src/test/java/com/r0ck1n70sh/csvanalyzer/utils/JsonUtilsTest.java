package com.r0ck1n70sh.csvanalyzer.utils;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonUtilsTest {
    @Test
    public void verify() {
        String jsonStr = "[ \"Index\", \"User Id\", \"First Name\", \"Last Name\", \"Sex\", \"Email\", \"Phone\", \"Date of birth\", \"Job Title\" ]";
        boolean isJsonObject = JsonUtils.isValidJsonObjectString(jsonStr);
        assertFalse(isJsonObject);
    }

}