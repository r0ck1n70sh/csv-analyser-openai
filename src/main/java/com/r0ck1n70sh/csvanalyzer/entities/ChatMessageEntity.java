package com.r0ck1n70sh.csvanalyzer.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.r0ck1n70sh.csvanalyzer.enums.ChatMessageEntityRole;
import com.r0ck1n70sh.csvanalyzer.utils.JsonUtils;
import com.theokanning.openai.completion.chat.ChatFunctionCall;
import com.theokanning.openai.completion.chat.ChatMessage;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;


@Table(name = "CHAT_MESSAGE_ENTITY")
@Entity
@Getter
@Setter
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated
    private ChatMessageEntityRole role;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "chat_session_id")
    @JsonIgnore
    private ChatSession chatSession;

    private String functionCallName;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "function_call_args", joinColumns = @JoinColumn(name = "function_call_args_id"))
    private Map<String, String> functionCallArgs;

    private final long timestamp;

    public ChatMessageEntity(@NonNull ChatMessage chatMessage) {
        this();

        this.role = ChatMessageEntityRole.fromRole(chatMessage.getRole());
        this.name = chatMessage.getName();
        this.content = chatMessage.getContent();
        this.functionCallName = chatMessage.getFunctionCall() == null
                ? null
                : chatMessage.getFunctionCall().getName();

        this.functionCallArgs = chatMessage.getFunctionCall() == null
                ? null
                : JsonUtils.jsonNodeAsMap(chatMessage.getFunctionCall().getArguments());
    }

    public ChatMessageEntity() {
        this.timestamp = System.currentTimeMillis();
    }

    public ChatMessage toMessage() {
        ChatFunctionCall chatFunction = null;

        try {
            chatFunction = functionCallName == null
                    ? null
                    : new ChatFunctionCall(functionCallName, JsonUtils.mapToJsonNode(functionCallArgs));
        } catch (JsonProcessingException e) {

        }


        return new ChatMessage(role.name, content, name, chatFunction);
    }

    @Override
    public String toString() {
        return String.format(
                """
                        {
                            "id": %d,
                            "role": %s,
                            "name": %s,
                            "content": %s
                        }
                        """,
                id, role, name, content
        );
    }
}
