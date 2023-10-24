package com.r0ck1n70sh.csvanalyzer.enums;

import com.theokanning.openai.completion.chat.ChatMessageRole;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatMessageEntityRoleTest {

    @Test
    public void verify_01() {
        ChatMessageRole role = ChatMessageRole.USER;
        ChatMessageEntityRole excepted = ChatMessageEntityRole.USER;
        ChatMessageEntityRole actual = ChatMessageEntityRole.fromRole(role);
        assertEquals(excepted, actual);
    }

    @Test
    public void verify_02() {
        ChatMessageRole role = ChatMessageRole.SYSTEM;
        ChatMessageEntityRole excepted = ChatMessageEntityRole.SYSTEM;
        ChatMessageEntityRole actual = ChatMessageEntityRole.fromRole(role);
        assertEquals(excepted, actual);
    }

    @Test
    public void verify_03() {
        ChatMessageRole role = ChatMessageRole.ASSISTANT;
        ChatMessageEntityRole excepted = ChatMessageEntityRole.ASSISTANT;
        ChatMessageEntityRole actual = ChatMessageEntityRole.fromRole(role);
        assertEquals(excepted, actual);
    }

    @Test
    public void verify_04() {
        ChatMessageRole role = ChatMessageRole.FUNCTION;
        ChatMessageEntityRole excepted = ChatMessageEntityRole.FUNCTION;
        ChatMessageEntityRole actual = ChatMessageEntityRole.fromRole(role);
        assertEquals(excepted, actual);
    }
}