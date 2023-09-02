package com.soulcode.goservicedev.event;

import com.soulcode.goservicedev.domain.Usuario;
import com.soulcode.goservicedev.domain.UsuarioLog;
import com.soulcode.goservicedev.service.UsuarioLogService;
import com.soulcode.goservicedev.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioLogService usuarioLogService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        try {
            String email = event.getAuthentication().getName();
            Usuario usuario = usuarioService.findByEmail(email);
            UsuarioLog usuarioLog = new UsuarioLog(usuario);
            usuarioLogService.create(usuarioLog);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
