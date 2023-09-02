package com.soulcode.goservicedev.service.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class UsuarioNaoAutenticadoException extends EntityNotFoundException {

    public UsuarioNaoAutenticadoException() {
        super("Usuário não está autenticado.");
    }

    public UsuarioNaoAutenticadoException(String message) {
        super(message);
    }
}
