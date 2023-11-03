package io.github.fgabrielbraga.goservicedev.service;

import io.github.fgabrielbraga.goservicedev.domain.Prestador;
import io.github.fgabrielbraga.goservicedev.domain.Usuario;
import io.github.fgabrielbraga.goservicedev.repository.UsuarioRepository;
import io.github.fgabrielbraga.goservicedev.service.exceptions.UsuarioNaoEncontradoException;
import io.github.fgabrielbraga.goservicedev.domain.Administrador;
import io.github.fgabrielbraga.goservicedev.domain.Cliente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        Optional<Usuario> result = usuarioRepository.findById(id);
        if(result.isPresent()) {
            return result.get();
        }
        throw new UsuarioNaoEncontradoException();
    }

    public Usuario findByEmail(String email) {
        Optional<Usuario> result = usuarioRepository.findByEmail(email);
        if(result.isPresent()) {
            return result.get();
        }
        throw new UsuarioNaoEncontradoException();
    }

    @Transactional
    public void disableUser(Long id) {
        Optional<Usuario> result = usuarioRepository.findById(id);
        if(result.isPresent()) {
            usuarioRepository.updateEnableById(false, id);
            return;
        }
        throw new UsuarioNaoEncontradoException();
    }

    @Transactional
    public void enableUser(Long id) {
        Optional<Usuario> result = usuarioRepository.findById(id);
        if(result.isPresent()) {
            usuarioRepository.updateEnableById(true, id);
            return;
        }
        throw new UsuarioNaoEncontradoException();
    }

    public Usuario createUser(Usuario usuario) {
        String passwordEncoded = encoder.encode(usuario.getPassword());
        usuario.setSenha(passwordEncoded);
        usuario.setId(null);

        switch (usuario.getPerfil()) {
            case ADMIN:
                return createAndSaveAdministrador(usuario);
            case PRESTADOR:
                return createAndSavePrestador(usuario);
            case CLIENTE:
            default:
                return createAndSaveCliente(usuario);
        }
    }

    private Administrador createAndSaveAdministrador(Usuario u) {
        Administrador admin = new Administrador(u.getId(), u.getNome(), u.getEmail(), u.getSenha(), u.getPerfil(), u.getHabilitado());
        return usuarioRepository.save(admin);
    }

    private Prestador createAndSavePrestador(Usuario u) {
        Prestador prestador = new Prestador(u.getId(), u.getNome(), u.getEmail(), u.getSenha(), u.getPerfil(), u.getHabilitado());
        return usuarioRepository.save(prestador);
    }

    private Cliente createAndSaveCliente(Usuario u) {
        Cliente cliente = new Cliente(u.getId(), u.getNome(), u.getEmail(), u.getSenha(), u.getPerfil(), u.getHabilitado());
        return usuarioRepository.save(cliente);
    }
}
