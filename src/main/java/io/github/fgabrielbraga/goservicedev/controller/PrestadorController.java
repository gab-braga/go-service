package io.github.fgabrielbraga.goservicedev.controller;

import io.github.fgabrielbraga.goservicedev.domain.Agendamento;
import io.github.fgabrielbraga.goservicedev.domain.Prestador;
import io.github.fgabrielbraga.goservicedev.domain.Servico;
import io.github.fgabrielbraga.goservicedev.service.AgendamentoService;
import io.github.fgabrielbraga.goservicedev.service.PrestadorService;
import io.github.fgabrielbraga.goservicedev.service.ServicoService;
import io.github.fgabrielbraga.goservicedev.service.exceptions.UsuarioNaoAutenticadoException;
import io.github.fgabrielbraga.goservicedev.service.exceptions.UsuarioNaoEncontradoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/prestador")
public class PrestadorController {

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping(value = "/dados")
    public ModelAndView provider(Authentication authentication) {
        ModelAndView mv = new ModelAndView("dadosPrestador");
        try {
            Prestador prestador = prestadorService.findAuthenticated(authentication);
            mv.addObject("prestador", prestador);
            List<Servico> servicos = servicoService.findAll();
            mv.addObject("servicos", servicos);
            List<Servico> especialidades = servicoService.findByPrestador(authentication);
            mv.addObject("especialidades", especialidades);
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Não foi possível carregar os dados.");
        }
        return mv;
    }

    @PostMapping(value = "/dados")
    public String editarPrestador(@Valid Prestador prestador, RedirectAttributes attributes) {
        try {
            prestadorService.update(prestador);
            attributes.addFlashAttribute("successMessage", "Dados alterados.");
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados cadastrais.");
        }
        return "redirect:/prestador/dados";
    }

    @PostMapping(value = "/dados/adicionar/especialidade")
    public String adicionarEspecialidade(Authentication authentication, @RequestParam(name = "servicoId") Long id, RedirectAttributes attributes) {
        try {
            prestadorService.addServicoPrestador(authentication, id);
            attributes.addFlashAttribute("successMessage", "Especialidade adicionada.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao adicionar especialidade.");
        }
        return "redirect:/prestador/dados";
    }

    @PostMapping(value = "/dados/remover/especialidade")
    public String removerEspecialidade(Authentication authentication, @RequestParam(name = "servicoId") Long id, RedirectAttributes attributes) {
        try {
            prestadorService.removeServicoPrestador(authentication, id);
            attributes.addFlashAttribute("successMessage", "Especialidade removida.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao remover especialidade.");
        }
        return "redirect:/prestador/dados";
    }

    @GetMapping(value = "/agenda")
    public ModelAndView agenda(Authentication authentication) {
        ModelAndView mv = new ModelAndView("agendaPrestador");
        try {
            List<Agendamento> agendamentos = agendamentoService.findByPrestador(authentication);
            mv.addObject("agendamentos", agendamentos);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao carregar dados de agendamentos.");
        }
        return mv;
    }

    @PostMapping(value = "/agenda/cancelar")
    public String cancel(Authentication authentication, @RequestParam(name = "agendamentoId") Long id, RedirectAttributes attributes) {
        try {
            agendamentoService.cancelAgendaPrestador(authentication, id);
            attributes.addFlashAttribute("successMessage", "Agendamento cancelada.");;
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao cancelar agendamento.");
        }
        return "redirect:/prestador/agenda";
    }

    @PostMapping(value = "/agenda/confirmar")
    public String confirm(Authentication authentication, @RequestParam(name = "agendamentoId") Long id, RedirectAttributes attributes) {
        try {
            agendamentoService.confirmAgenda(authentication, id);
            attributes.addFlashAttribute("successMessage", "Agendamento confirmado.");;
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao confirmar agendamento.");
        }
        return "redirect:/prestador/agenda";
    }
}
