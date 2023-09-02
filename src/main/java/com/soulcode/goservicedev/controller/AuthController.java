package com.soulcode.goservicedev.controller;

import com.soulcode.goservicedev.domain.Cliente;
import com.soulcode.goservicedev.service.AuthService;
import com.soulcode.goservicedev.service.exceptions.SenhaIncorretaException;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoEncontradoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String login(
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "logout", required = false) String logout,
            RedirectAttributes attributes) {
        if (error != null) {
            attributes.addFlashAttribute("errorMessage", "Erro ao entrar. Verifique suas credenciais.");
            return "redirect:/auth/login";
        }
        else if(logout != null) {
            attributes.addFlashAttribute("successMessage", "Até mais.");
            return "redirect:/auth/login";
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "cadastroCliente";
    }

    @PostMapping("/signup")
    public String createCliente(@Valid Cliente cliente, RedirectAttributes attributes) {
        try {
            authService.createCliente(cliente);
            attributes.addFlashAttribute("successMessage", "Conta criada com sucesso. Faça login para continuar.");
            return "redirect:/auth/login";
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Ocorreu um erro ao criar a conta.");
            return "redirect:/auth/signup";
        }
    }

    @GetMapping("/password/new")
    public String changePassword() {
        return "alterarSenha";
    }

    @PostMapping("/password/new")
    public String updateSenha(@RequestParam String senhaAtual, @RequestParam String senhaNova, Authentication authentication, RedirectAttributes attributes) {
        try {
            authService.updatePassword(authentication, senhaAtual, senhaNova);
            attributes.addFlashAttribute("successMessage", "Senha alterada. Faça o login novamente.");
        }
        catch (SenhaIncorretaException | UsuarioNaoEncontradoException | UsuarioNaoAutenticadoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Ocorreu um erro ao tentar alterar a senha.");
        }
        return "redirect:/auth/password/new";
    }
}
