package com.r0ck1n70sh.csvanalyzer.controllers;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import com.r0ck1n70sh.csvanalyzer.entities.UserSession;
import com.r0ck1n70sh.csvanalyzer.repositories.ChatSessionRepository;
import com.r0ck1n70sh.csvanalyzer.repositories.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/api/chat_session")
public class ChatSessionController {
    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @GetMapping("/")
    @ResponseBody
    public List<ChatSession> get() {
        Iterable<ChatSession> iterable = chatSessionRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    @PostMapping("/")
    @ResponseBody
    public ChatSession create(@RequestBody Map<String, String> body) {
        String userSessionId = body.get("user_session_id");

        UserSession userSession = findByUserSessionId(userSessionId);
        if (userSession == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("invalid user_session_id: %s", userSessionId)
            );
        }

        ChatSession chatSession = new ChatSession();
        chatSession.setUserSession(userSession);
        chatSessionRepository.save(chatSession);

        userSession.getChatSessionList().add(chatSession);
        userSessionRepository.save(userSession);

        return chatSession;
    }

    private UserSession findByUserSessionId(String userSessionId) {
        Iterable<UserSession> iterable = userSessionRepository.findAll();

        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(userSession -> userSession.getUserSessionId().equals(userSessionId))
                .findFirst()
                .orElse(null);
    }
}
