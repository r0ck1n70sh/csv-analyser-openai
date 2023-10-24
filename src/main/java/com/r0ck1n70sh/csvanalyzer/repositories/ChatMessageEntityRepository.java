package com.r0ck1n70sh.csvanalyzer.repositories;

import com.r0ck1n70sh.csvanalyzer.entities.ChatMessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface ChatMessageEntityRepository extends CrudRepository<ChatMessageEntity, Long> {
}
