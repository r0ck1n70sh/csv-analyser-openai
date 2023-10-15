package com.r0ck1n70sh.csvanalyzer.entities;

import com.r0ck1n70sh.csvanalyzer.utils.StringUtils;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "USER_SESSION")
@Getter
public class UserSession {
    public static final int USER_SESSION_ID_LEN = 22;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private final String userSessionId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userSession")
    @ElementCollection
    private List<ChatSession> chatSessionList;

    public UserSession() {
        this.userSessionId = StringUtils.generateRandomAlphaNumericString(USER_SESSION_ID_LEN);
    }
}
