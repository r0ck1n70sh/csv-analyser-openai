package com.r0ck1n70sh.csvanalyzer.enums;

import com.theokanning.openai.completion.chat.ChatMessageRole;

public enum ChatMessageEntityRole {
    USER("user", ChatMessageRole.USER),
    SYSTEM("system", ChatMessageRole.SYSTEM),
    ASSISTANT("assistant", ChatMessageRole.ASSISTANT),
    FUNCTION("function", ChatMessageRole.FUNCTION);

    private final String name;
    private final ChatMessageRole role;

    ChatMessageEntityRole(String name, ChatMessageRole role) {
        this.name = name;
        this.role = role;
    }

    public String toString() {
        return this.name;
    }

    public static ChatMessageEntityRole fromRole(ChatMessageRole role) {
        return fromRole(role.value());
    }

    public static ChatMessageEntityRole fromRole(String role) {
        for (ChatMessageEntityRole thisRole : ChatMessageEntityRole.values()) {
            if (thisRole.name.equals(role)) {
                return thisRole;
            }
        }

        return null;
    }
}
