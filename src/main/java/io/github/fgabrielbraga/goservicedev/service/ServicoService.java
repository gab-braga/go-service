package io.github.fgabrielbraga.goservicedev.service;

import io.github.fgabrielbraga.goservicedev.domain.Servico;
import io.github.fgabrielbraga.goservicedev.repository.ServicoRepository;
import io.github.fgabrielbraga.goservicedev.service.exceptions.ServicoNaoEncontradoException;
import io.github.fgabrielbraga.goservicedev.service.exceptions.UsuarioNaoAutenticadoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> findAll() {
        System.err.println("BUSCANDO NO BANCO DE DADOS...");
        return servicoRepository.findAll();
    }

    public Servico createService(Servico servico) {
        servico.setId(null);
        return servicoRepository.save(servico);
    }

    public Servico findById(Long id) {
        Optional<Servico> servico = servicoRepository.findById(id);
        if (servico.isPresent()) {
            return servico.get();
        } else {
            throw new ServicoNaoEncontradoException();
        }
    }

    public List<Servico> findByPrestador(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return servicoRepository.findByPrestador(authentication.getName());
        } else {
            throw new UsuarioNaoAutenticadoException();
        }
    }

    public void removeServiceById(Long servicoId) {
        servicoRepository.deleteById(servicoId);
    }

    public Servico update(Servico servico) {
        Optional<Servico> servicoResult = servicoRepository.findById(servico.getId());
        if (servicoResult.isPresent()) {
            return servicoRepository.save(servico);
        }
        throw new ServicoNaoEncontradoException();
    }
}
