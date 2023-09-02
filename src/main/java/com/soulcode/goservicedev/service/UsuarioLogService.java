package com.soulcode.goservicedev.service;

import com.soulcode.goservicedev.domain.UsuarioLog;
import com.soulcode.goservicedev.repository.UsuarioLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioLogService {

    @Autowired
    private UsuarioLogRepository usuarioLogRepository;

    public UsuarioLog create(UsuarioLog usuarioLog) {
        return usuarioLogRepository.save(usuarioLog);
    }

    public List<UsuarioLog> findAll() {
        return usuarioLogRepository.findAll();
    }
}
