package com.r0ck1n70sh.csvanalyzer.entities;


import com.r0ck1n70sh.csvanalyzer.utils.StringUtils;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "CHAT_SESSION")
@Getter
public class ChatSession {
    public static final int CHAT_SESSION_ID_LEN = 22;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private final String chatSessionId;

    @ManyToOne
    @JoinColumn(name="user_session_id")
    private UserSession userSession;

    public ChatSession() {
        this.chatSessionId = StringUtils.generateRandomAlphaNumericString(CHAT_SESSION_ID_LEN);
    }
}
