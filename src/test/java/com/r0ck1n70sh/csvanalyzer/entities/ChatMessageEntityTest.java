package com.r0ck1n70sh.csvanalyzer.entities;

import com.r0ck1n70sh.csvanalyzer.enums.ChatMessageEntityRole;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.junit.Test;

import static org.junit.Assert.*;


public class ChatMessageEntityTest {

    @Test
    public void verify() {
        ChatMessageEntityRole role = ChatMessageEntityRole.SYSTEM;
        String content = "test content";
        String name = "name";

        ChatMessage message = new ChatMessage(role.name, content, name);
        ChatMessageEntity entity = new ChatMessageEntity(message);

        ChatMessage actual = entity.toMessage();

        assertEquals(message.getRole(), actual.getRole());
        assertEquals(message.getContent(), actual.getContent());
        assertEquals(message.getName(), actual.getName());
    }

    @Test
    public void verify02() {
        ChatMessageEntityRole role = ChatMessageEntityRole.SYSTEM;
        String content = "test content";
        String name = "name";

        ChatMessage message = new ChatMessage(role.name, content, name);

        ChatMessageEntity expected = new ChatMessageEntity(message);
        ChatMessageEntity actual = new ChatMessageEntity(message);

        assertEquals(expected, actual);
    }
}