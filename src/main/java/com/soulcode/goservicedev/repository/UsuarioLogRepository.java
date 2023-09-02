package com.soulcode.goservicedev.repository;

import com.soulcode.goservicedev.domain.UsuarioLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioLogRepository extends MongoRepository<UsuarioLog, String> {
}
