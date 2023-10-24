package com.r0ck1n70sh.csvanalyzer.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.utils.StringUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "CHAT_SESSION")
@Getter
@Setter
public class ChatSession {
    public static final int CHAT_SESSION_ID_LEN = 22;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private final String chatSessionId;

    @ManyToOne
    @JoinColumn(name = "user_session_id")
    @JsonIgnore
    private UserSession userSession;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatSession")
    @ElementCollection
    private List<ChatMessageEntity> messages;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "chatSession")
    private RawCsvMeta csv;

    @Enumerated
    private ChatStatus status;

    public ChatSession() {
        this.chatSessionId = StringUtils.generateRandomAlphaNumericString(CHAT_SESSION_ID_LEN);
        this.status = ChatStatus.INITIALIZED;
        this.messages = new ArrayList<>();
    }
}
