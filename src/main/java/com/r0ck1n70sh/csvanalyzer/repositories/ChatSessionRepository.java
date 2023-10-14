package com.r0ck1n70sh.csvanalyzer.repositories;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import org.springframework.data.repository.CrudRepository;


public interface ChatSessionRepository extends CrudRepository<ChatSession, Long> {
}
