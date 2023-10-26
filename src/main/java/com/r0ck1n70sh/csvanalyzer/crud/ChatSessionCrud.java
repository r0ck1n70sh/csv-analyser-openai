package com.r0ck1n70sh.csvanalyzer.crud;

import com.r0ck1n70sh.csvanalyzer.entities.ChatMessageEntity;
import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.repositories.ChatSessionRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Getter
@Setter
public class ChatSessionCrud {
    @Autowired
    private ChatSessionRepository repository;

    private static ChatSessionCrud instance;

    public static ChatSessionCrud getInstance() {
        if (instance == null) {
            instance = new ChatSessionCrud();
        }

        return instance;
    }

    public static void save(@NonNull ChatSession entity) {
        List<ChatMessageEntity> messages = entity.getMessages();
        ChatMessageEntityCrud.saveAll(messages);
        messages.forEach(m -> m.setChatSession(entity));

        RawCsvMeta csv = entity.getCsv();
        if (csv != null) {
            RawCsvMetaCrud.save(csv);
            csv.setChatSession(entity);
        }

        ChatSessionCrud instance = getInstance();
        ChatSessionRepository repository = instance.getRepository();

        repository.save(entity);
    }
}
