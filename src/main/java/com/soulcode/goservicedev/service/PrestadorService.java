package com.soulcode.goservicedev.service;

import com.soulcode.goservicedev.domain.Prestador;
import com.soulcode.goservicedev.domain.Servico;
import com.soulcode.goservicedev.repository.PrestadorRepository;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestadorService {

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private ServicoService servicoService;

    public Prestador findAuthenticated(Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) {
            Optional<Prestador> prestador = prestadorRepository.findByEmail(authentication.getName());
            if(prestador.isPresent()) {
                return prestador.get();
            }
            else {
                throw new UsuarioNaoEncontradoException("Prestador não encontrado.");
            }
        }
        else {
            throw new UsuarioNaoAutenticadoException();
        }
    }

    public Prestador findById(Long id) {
        Optional<Prestador> prestador = prestadorRepository.findById(id);
        if(prestador.isPresent()) {
            return prestador.get();
        }
        else {
            throw new UsuarioNaoEncontradoException("Prestador não encontrado.");
        }
    }

    public Prestador update(Prestador prestador) {
        Prestador updatedPrestador = this.findById(prestador.getId());
        updatedPrestador.setNome(prestador.getNome());
        updatedPrestador.setEmail(prestador.getEmail());
        updatedPrestador.setDescricao(prestador.getDescricao());
        updatedPrestador.setTaxaPorHora(prestador.getTaxaPorHora());
        return prestadorRepository.save(updatedPrestador);
    }

    public List<Prestador> findByServicoId(Long id) {
        return prestadorRepository.findByServicoId(id);
    }

    public void addServicoPrestador(Authentication authentication, Long id) {
        Prestador prestador = findAuthenticated(authentication);
        Servico servico = servicoService.findById(id);
        prestador.addEspecialidade(servico);
        prestadorRepository.save(prestador);
    }

    public void removeServicoPrestador(Authentication authentication, Long id) {
        Prestador prestador = findAuthenticated(authentication);
        Servico servico = servicoService.findById(id);
        prestador.removeEspecialidade(servico);
        prestadorRepository.save(prestador);
    }
}
