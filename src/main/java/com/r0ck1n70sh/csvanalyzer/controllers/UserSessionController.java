package com.r0ck1n70sh.csvanalyzer.controllers;

import com.r0ck1n70sh.csvanalyzer.entities.UserSession;
import com.r0ck1n70sh.csvanalyzer.repositories.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/user_session")
public class UserSessionController {
    @Autowired
    private UserSessionRepository userSessionRepository;

    @GetMapping("/")
    @ResponseBody
    public String view() {
        return "user_session";
    }

    @PostMapping("/")
    @ResponseBody
    public UserSession create() {
        UserSession userSession = new UserSession();
        return userSessionRepository.save(userSession);
    }
}
