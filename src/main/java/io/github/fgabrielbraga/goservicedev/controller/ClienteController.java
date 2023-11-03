package io.github.fgabrielbraga.goservicedev.controller;

import io.github.fgabrielbraga.goservicedev.domain.Agendamento;
import io.github.fgabrielbraga.goservicedev.domain.Cliente;
import io.github.fgabrielbraga.goservicedev.domain.Prestador;
import io.github.fgabrielbraga.goservicedev.domain.Servico;
import io.github.fgabrielbraga.goservicedev.service.AgendamentoService;
import io.github.fgabrielbraga.goservicedev.service.ClienteService;
import io.github.fgabrielbraga.goservicedev.service.PrestadorService;
import io.github.fgabrielbraga.goservicedev.service.ServicoService;
import io.github.fgabrielbraga.goservicedev.service.exceptions.StatusAgendamentoImutavelException;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping(value = "/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping(value = "/dados")
    public ModelAndView client(Authentication authentication) {
        ModelAndView mv = new ModelAndView("dadosCliente");
        try {
            Cliente cliente = clienteService.findAuthenticated(authentication);
            mv.addObject("cliente", cliente);
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Não foi possível carregar os dados.");
        }
        return mv;
    }

    @PostMapping(value = "/dados")
    public String editarCliente(@Valid Cliente cliente, RedirectAttributes attributes) {
        try {
            clienteService.update(cliente);
            attributes.addFlashAttribute("successMessage", "Dados alterados.");
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados cadastrais.");
        }
        return "redirect:/cliente/dados";
    }

    @GetMapping(value = "/agendar")
    public ModelAndView agendar(@RequestParam(name = "especialidade", required = false) Long servicoId) {
        ModelAndView mv = new ModelAndView("agendarServico");
        try {
            List<Servico> servicos = servicoService.findAll();
            mv.addObject("servicos", servicos);

            if(servicoId != null) {
                List<Prestador> prestadors = prestadorService.findByServicoId(servicoId);
                mv.addObject("prestadors", prestadors);
                mv.addObject("servicoId", servicoId);
            }
        } catch (Exception wx) {
            mv.addObject("errorMessage", "Erro ao buscar dados de serviços.");
        }
        return mv;
    }

    @PostMapping(value = "/agendar")
    public String criarAgendamento(
            Authentication authentication,
            @RequestParam Long servicoId,
            @RequestParam Long prestadorId,
            @RequestParam LocalDate data,
            @RequestParam LocalTime hora,
            RedirectAttributes attributes) {
        try {
            agendamentoService.create(authentication, servicoId, prestadorId, data, hora);
            attributes.addFlashAttribute("successMessage", "Agendamento realizado com sucesso. Aguardando confirmação.");
        } catch (Exception e) {
            attributes.addFlashAttribute("errorMessage", "Erro ao finalizar agendamento.");
        }
        return "redirect:/cliente/historico";
    }

    @GetMapping(value = "/historico")
    public ModelAndView historico(Authentication authentication) {
        ModelAndView mv = new ModelAndView("historicoCliente");
        try {
            List<Agendamento> agendamentos = agendamentoService.findByCliente(authentication);
            mv.addObject("agendamentos", agendamentos);
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception e) {
            mv.addObject("errorMessage", "Erro ao carregar dados de agendamentos.");
        }
        return mv;
    }

    @PostMapping(value = "/historico/cancelar")
    public String cancel(Authentication authentication, @RequestParam(name = "agendamentoId") Long id, RedirectAttributes attributes) {
        try {
            agendamentoService.cancelAgendaCliente(authentication, id);
            attributes.addFlashAttribute("successMessage", "Agendamento cancelada.");;
        } catch (StatusAgendamentoImutavelException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception e) {
            attributes.addFlashAttribute("errorMessage", "Erro ao cancelar agendamento.");
        }
        return "redirect:/cliente/historico";
    }

    @PostMapping(value = "/historico/concluir")
    public String complete(Authentication authentication, @RequestParam(name = "agendamentoId") Long id, RedirectAttributes attributes) {
        try {
            agendamentoService.completeAgenda(authentication, id);
            attributes.addFlashAttribute("successMessage", "Agendamento concluido.");;
        } catch (StatusAgendamentoImutavelException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception e) {
            attributes.addFlashAttribute("errorMessage", "Erro ao concluir agendamento.");
        }
        return "redirect:/cliente/historico";
    }
}
