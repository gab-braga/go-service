package io.github.fgabrielbraga.goservicedev.service.exceptions;

public class StatusAgendamentoImutavelException extends RuntimeException {

    public StatusAgendamentoImutavelException() {
        super("O status do agendamento não pode ser alterado.");
    }

    public StatusAgendamentoImutavelException(String message) {
        super(message);
    }
}
