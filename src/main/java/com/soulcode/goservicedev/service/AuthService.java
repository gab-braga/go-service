package com.soulcode.goservicedev.service;

import com.soulcode.goservicedev.domain.Cliente;
import com.soulcode.goservicedev.domain.Usuario;
import com.soulcode.goservicedev.repository.UsuarioRepository;
import com.soulcode.goservicedev.service.exceptions.SenhaIncorretaException;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    public Cliente createCliente(Cliente cliente) {
        String passwordEncoded = encoder.encode(cliente.getPassword());
        cliente.setSenha(passwordEncoded);
        cliente.setId(null);
        return usuarioRepository.save(cliente);
    }

    @Transactional
    public void updatePassword(Authentication authentication, String senhaAtual, String senhaNova) {
        if(authentication != null && authentication.isAuthenticated()) {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(authentication.getName());
            if(usuario.isPresent()) {
                boolean passwordVerified = encoder.matches(senhaAtual, usuario.get().getPassword());
                if(passwordVerified) {
                    String passwordEncoded = encoder.encode(senhaNova);
                    usuarioRepository.updatePasswordById(passwordEncoded, authentication.getName());
                }
                else {
                    throw new SenhaIncorretaException();
                }
            }
            else {
                throw new UsuarioNaoEncontradoException();
            }
        }
        else {
            throw new UsuarioNaoAutenticadoException();
        }
    }
}
