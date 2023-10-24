package com.r0ck1n70sh.csvanalyzer.crud;

import com.r0ck1n70sh.csvanalyzer.entities.ChatMessageEntity;
import com.r0ck1n70sh.csvanalyzer.repositories.ChatMessageEntityRepository;
import lombok.Getter;


@Getter
public class ChatMessageEntityCrud {
    private ChatMessageEntityRepository repository;

    private static ChatMessageEntityCrud instance;

    public static ChatMessageEntityCrud getInstance() {
        if (instance == null) {
            instance = new ChatMessageEntityCrud();
        }

        return instance;
    }

    public static void save(ChatMessageEntity entity) {
        ChatMessageEntityCrud instance = getInstance();
        ChatMessageEntityRepository repository = instance.getRepository();

        repository.save(entity);
    }
}
