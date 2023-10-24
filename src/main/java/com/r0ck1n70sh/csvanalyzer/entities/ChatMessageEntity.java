package com.r0ck1n70sh.csvanalyzer.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.r0ck1n70sh.csvanalyzer.enums.ChatMessageEntityRole;
import com.theokanning.openai.completion.chat.ChatMessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Table(name = "CHAT_MESSAGE_ENTITY")
@Entity
@Getter
@Setter
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated
    private final ChatMessageEntityRole role;

    private final String name;

    private final String content;

    @ManyToOne
    @JoinColumn(name = "chat_session_id")
    @JsonIgnore
    private ChatSession chatSession;

    private final long timestamp;

    public ChatMessageEntity(@NonNull ChatMessage chatMessage) {
        this.role = ChatMessageEntityRole.fromRole(chatMessage.getRole());
        this.name = chatMessage.getName();
        this.content = chatMessage.getContent();
        this.timestamp = System.currentTimeMillis();
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
