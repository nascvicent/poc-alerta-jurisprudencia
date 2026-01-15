package com.example.demo.controller;

import com.example.demo.entity.AlertaPesquisa;
import com.example.demo.entity.Jurisprudencia;
import com.example.demo.service.AlertaService;
import com.example.demo.repository.JurisprudenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private AlertaService alertaService;

    @Autowired
    private JurisprudenciaRepository jurisRepo;

    // 1. Criar alerta
    @PostMapping("/alertas")
    public AlertaPesquisa criarAlerta(
            @RequestParam String email,
            @RequestParam String termo,
            @RequestParam(defaultValue = "7") Integer diasFrequencia,
            @RequestParam(defaultValue = "90") Integer diasExpiracao) {

        return alertaService.criarAlerta(email, termo, diasFrequencia, diasExpiracao);
    }

    // 2. Listar todos os alertas
    @GetMapping("/alertas")
    public List<AlertaPesquisa> listarAlertas() {
        return alertaService.listarTodos();
    }

    // 3. Inserir jurisprudência de teste
    @PostMapping("/jurisprudencias")
    public Jurisprudencia inserirJurisprudencia(
            @RequestParam String numero,
            @RequestParam String ementa) {

        Jurisprudencia jurisprudencia = new Jurisprudencia();
        jurisprudencia.setNumero(numero);
        jurisprudencia.setEmenta(ementa);
        jurisprudencia.setDataPublicacao(LocalDateTime.now());

        return jurisRepo.save(jurisprudencia);
    }

    // 4. FORÇAR processamento de alertas (sem esperar o agendamento)
    @PostMapping("/processar-alertas")
    public String processarAgora() {
        alertaService.processarAlertas();
        return "Alertas processados! Verifique seu email.";
    }

    // 5. Listar jurisprudências
    @GetMapping("/jurisprudencias")
    public List<Jurisprudencia> listarJurisprudencias() {
        return jurisRepo.findAll();
    }
}