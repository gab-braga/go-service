package com.soulcode.goservicedev.service;

import com.soulcode.goservicedev.domain.Cliente;
import com.soulcode.goservicedev.repository.ClienteRepository;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findAuthenticated(Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) {
            Optional<Cliente> cliente = clienteRepository.findByEmail(authentication.getName());
            if(cliente.isPresent()) {
                return cliente.get();
            }
            else {
                throw new UsuarioNaoEncontradoException("Cliente não encontrado");
            }
        }
        else {
            throw new UsuarioNaoAutenticadoException();
        }
    }

    public Cliente findById(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if(cliente.isPresent()) {
            return cliente.get();
        }
        else {
            throw new UsuarioNaoEncontradoException("Cliente não encontrado");
        }
    }

    public Cliente update(Cliente cliente) {
        Cliente updatedCliente = this.findById(cliente.getId());
        updatedCliente.setNome(cliente.getNome());
        updatedCliente.setEmail(cliente.getEmail());
        updatedCliente.setTelefone(cliente.getTelefone());
        updatedCliente.setCpf(cliente.getCpf());
        updatedCliente.setDataNascimento(cliente.getDataNascimento());
        return clienteRepository.save(updatedCliente);
    }
}
