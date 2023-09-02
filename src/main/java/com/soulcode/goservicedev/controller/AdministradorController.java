package com.soulcode.goservicedev.controller;

import com.soulcode.goservicedev.domain.Servico;
import com.soulcode.goservicedev.domain.Usuario;
import com.soulcode.goservicedev.domain.UsuarioLog;
import com.soulcode.goservicedev.service.ServicoService;
import com.soulcode.goservicedev.service.UsuarioLogService;
import com.soulcode.goservicedev.service.UsuarioService;
import com.soulcode.goservicedev.service.exceptions.ServicoNaoEncontradoException;
import com.soulcode.goservicedev.service.exceptions.UsuarioNaoEncontradoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private UsuarioLogService usuarioLogService;

    @GetMapping("/usuarios")
    public ModelAndView users() {
        ModelAndView mv = new ModelAndView("usuariosAdmin");
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            mv.addObject("usuarios", usuarios);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de usuários.");
        }
        return mv;
    }

    @PostMapping("/usuarios")
    public String createUser(@Valid Usuario usuario, RedirectAttributes attributes) {
        try {
            usuarioService.createUser(usuario);
            attributes.addFlashAttribute("successMessage", "Novo usuário adicionado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao adicionar novo usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/disable")
    public String disableUser(@RequestParam Long usuarioId, RedirectAttributes attributes) {
        try {
            usuarioService.disableUser(usuarioId);
            attributes.addFlashAttribute("successMessage", "Usuário desabilitado.");
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao desabilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/enable")
    public String enableUser(@RequestParam Long usuarioId, RedirectAttributes attributes) {
        try {
            usuarioService.enableUser(usuarioId);
            attributes.addFlashAttribute("successMessage", "Usuário habilitado.");
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao habilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/servicos")
    public ModelAndView services() {
        ModelAndView mv = new ModelAndView("servicosAdmin");
        List<Servico> servicos = servicoService.findAll();
        mv.addObject("servicos", servicos);
        return mv;
    }

    @PostMapping("/servicos")
    public String createService(@Valid Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.createService(servico);
            attributes.addFlashAttribute("successMessage", "Novo serviço adicionado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao adicionar novo serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping("/servicos/editar/{id}")
    public ModelAndView editarServicoPage(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("editarServico");
        try {
            Servico servico = servicoService.findById(id);
            mv.addObject("servico", servico);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de serviços.");
        }
        return mv;
    }

    @PostMapping("/servicos/editar")
    public String editarServico(@Valid Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.update(servico);
            attributes.addFlashAttribute("successMessage", "Dados do serviço alterado.");
        } catch (ServicoNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados do serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @PostMapping("/servicos/remover")
    public String removeService(@RequestParam Long servicoId, RedirectAttributes attributes) {
        try {
            servicoService.removeServiceById(servicoId);
            attributes.addFlashAttribute("successMessage", "Serviço removido.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao remover serviço. Este serviço, possivelmente, está ligado a um prestador.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping(value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("dashboard");
        try {
            List<UsuarioLog> logs = usuarioLogService.findAll();
            mv.addObject("logs", logs);
        }
        catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados referentes a logs.");
        }
        return mv;
    }
}