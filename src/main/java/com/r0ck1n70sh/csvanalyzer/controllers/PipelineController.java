package com.r0ck1n70sh.csvanalyzer.controllers;

import com.r0ck1n70sh.csvanalyzer.crud.ChatSessionCrud;
import com.r0ck1n70sh.csvanalyzer.crud.RawCsvColumnCrud;
import com.r0ck1n70sh.csvanalyzer.crud.RawCsvDataPointCrud;
import com.r0ck1n70sh.csvanalyzer.crud.RawCsvMetaCrud;
import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import com.r0ck1n70sh.csvanalyzer.entities.UserSession;
import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.pipeline.*;
import com.r0ck1n70sh.csvanalyzer.repositories.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/api/pipeline/")
public class PipelineController {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageEntityRepository chatMessageEntityRepository;

    @Autowired
    private RawCsvMetaRepository rawCsvMetaRepository;

    @Autowired
    private RawCsvColumnRepository rawCsvColumnRepository;

    @Autowired
    private RawCsvDataPointRepository rawCsvDataPointRepository;

    @PostConstruct
    public void initialize() {
        ChatSessionCrud chatSessionCrud = ChatSessionCrud.getInstance();
        chatSessionCrud.setRepository(chatSessionRepository);

        RawCsvDataPointCrud rawCsvDataPointCrud = RawCsvDataPointCrud.getInstance();
        rawCsvDataPointCrud.setRepository(rawCsvDataPointRepository);

        RawCsvColumnCrud rawCsvColumnCrud = RawCsvColumnCrud.getInstance();
        rawCsvColumnCrud.setRepository(rawCsvColumnRepository);

        RawCsvMetaCrud rawCsvMetaCrud = RawCsvMetaCrud.getInstance();
        rawCsvMetaCrud.setRepository(rawCsvMetaRepository);
    }

    @PostMapping("/run/{user_session_id}/{chat_session_id}")
    @ResponseBody
    public PipelineResponse run(@PathVariable String user_session_id,
                                @PathVariable String chat_session_id,
                                @RequestBody PipelineRequest request) {

        Iterable<UserSession> userSessionIterable = userSessionRepository.findAll();

        UserSession userSession = StreamSupport.stream(userSessionIterable.spliterator(), false)
                .filter(session -> session.getUserSessionId().equals(user_session_id))
                .findFirst()
                .orElse(null);

        if (userSession == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("invalid user session id %s", user_session_id)
            );
        }

        ChatSession chatSession = userSession.getChatSessionList()
                .stream()
                .filter(session -> session.getChatSessionId().equals(chat_session_id))
                .findFirst()
                .orElse(null);

        if (chatSession == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("invalid chat session id %s", chat_session_id)
            );
        }

        ComponentFactory componentFactory = getComponentFactory(request, chatSession);
        Component component = componentFactory.create(chatSession);

        ComponentResponse componentResponse = component.conduct();

        PipelineConductor pipelineConductor = new PipelineConductor(chatSession, componentResponse);

        return pipelineConductor.updateSessionAndRespond();
    }

    private static ComponentFactory getComponentFactory(PipelineRequest request, ChatSession chatSession) {
        ChatStatus prevStatus = chatSession.getStatus();
        ChatStatus status = prevStatus.getNext();

        AbstractComponentFactory abstractComponentFactory = new AbstractComponentFactory(
                request.getCsvStr(),
                request.getCsvName(),
                null,
                status);

        ComponentFactory componentFactory = abstractComponentFactory.create();
        return componentFactory;
    }
}
