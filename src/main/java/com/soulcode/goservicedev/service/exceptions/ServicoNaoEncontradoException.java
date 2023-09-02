package com.soulcode.goservicedev.service.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class ServicoNaoEncontradoException extends EntityNotFoundException {

    public ServicoNaoEncontradoException() {
        super("Serviço não encontrado.");
    }

    public ServicoNaoEncontradoException(String message) {
        super(message);
    }
}
