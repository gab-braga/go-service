package io.github.fgabrielbraga.goservicedev.service.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class AgendamentoNaoEncontradoException extends EntityNotFoundException {

    public AgendamentoNaoEncontradoException() {
        super("Agendamento não foi encontrado.");
    }

    public AgendamentoNaoEncontradoException(String message) {
        super(message);
    }
}
