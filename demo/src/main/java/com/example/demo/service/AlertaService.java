package com.example.demo.service;

import com.example.demo.entity.AlertaPesquisa;
import com.example.demo.entity.Jurisprudencia;
import com.example.demo.repository.AlertaPesquisaRepository;
import com.example.demo.repository.JurisprudenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    @Autowired private AlertaPesquisaRepository alertaRepo;
    @Autowired private JurisprudenciaRepository jurisRepo;
    @Autowired private EmailService emailService;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void processarAlertas() {
        List<AlertaPesquisa> alertas = alertaRepo.findAll();

        for (AlertaPesquisa alerta : alertas) {


            if (!alerta.deveProcessar()) {

                if (!alerta.getAtivo()) {
                    alertaRepo.save(alerta);
                }
                continue;
            }

            System.out.println("Processando alerta vencido/agendado: " + alerta.getEmailUsuario());

            try {

                LocalDateTime dataInicioBusca = alerta.getUltimaVerificacao();

                List<Jurisprudencia> novas = jurisRepo.encontrarNovasDesde(
                        alerta.getTermoBusca(),
                        dataInicioBusca
                );

                if (!novas.isEmpty()) {
                    emailService.enviarRelatorio(alerta.getEmailUsuario(), novas);

                    // atualiza o ponteiro

                    alerta.setUltimaVerificacao(LocalDateTime.now());
                    alertaRepo.save(alerta);
                } else {
                    // aqui estou att a data mesmo se nao achar novas

                    alerta.setUltimaVerificacao(LocalDateTime.now());
                    alertaRepo.save(alerta);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // exclui alertas
    @Scheduled(cron = "0 0 3 * * SUN")
    @Transactional
    public void limparAlertasAntigos() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(180); // 6 meses inativos

        List<AlertaPesquisa> alertasParaExcluir = alertaRepo.findByAtivoFalseAndDataCriacaoBefore(dataLimite);

        if (!alertasParaExcluir.isEmpty()) {
            alertaRepo.deleteAll(alertasParaExcluir);
            System.out.println("Excluídos " + alertasParaExcluir.size() + " alertas antigos.");
        }
    }

    // alerta personalizado
    public AlertaPesquisa criarAlerta(String email, String termo, Integer diasFrequencia, Integer diasExpiracao) {
        AlertaPesquisa alerta = new AlertaPesquisa(email, termo, diasFrequencia);
        if (diasExpiracao != null) {
            alerta.setDiasExpiracao(diasExpiracao);
        }
        return alertaRepo.save(alerta);
    }

    // renovar alerta
    public AlertaPesquisa renovarAlerta(Long id) {
        AlertaPesquisa alerta = alertaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta não encontrado"));
        alerta.renovar();
        return alertaRepo.save(alerta);
    }

    // listar todos os alertas
    public List<AlertaPesquisa> listarTodos() {
        return alertaRepo.findAll();
    }


    public List<AlertaPesquisa> buscarPorEmail(String email) {
        return alertaRepo.findByEmailUsuario(email);
    }

    // deletar alerta
    public void deletarAlerta(Long id) {
        alertaRepo.deleteById(id);
    }

    // ativar/desativar alerta
    public AlertaPesquisa toggleAtivo(Long id) {
        AlertaPesquisa alerta = alertaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta não encontrado"));
        alerta.setAtivo(!alerta.getAtivo());
        return alertaRepo.save(alerta);
    }
}