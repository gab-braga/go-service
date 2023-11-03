package io.github.fgabrielbraga.goservicedev.repository;

import io.github.fgabrielbraga.goservicedev.domain.UsuarioLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioLogRepository extends MongoRepository<UsuarioLog, String> {
}
