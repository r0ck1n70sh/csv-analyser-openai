package com.r0ck1n70sh.csvanalyzer.repositories;

import com.r0ck1n70sh.csvanalyzer.entities.UserSession;
import org.springframework.data.repository.CrudRepository;


public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
}
