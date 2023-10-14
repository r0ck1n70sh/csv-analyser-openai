package com.r0ck1n70sh.csvanalyzer.controllers;

import com.r0ck1n70sh.csvanalyzer.repositories.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chat_session")
public class ChatSessionController {
    @Autowired
    private ChatSessionRepository repository;
}
