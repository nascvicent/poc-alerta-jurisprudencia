package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerta_pesquisa")
public class AlertaPesquisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String emailUsuario;

    @Column(nullable = false)
    private String termoBusca;

    @Column(nullable = false)
    private LocalDateTime ultimaVerificacao;

    @Column(nullable = false)
    private Integer frequenciaDias = 7;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Integer diasExpiracao = 90; // para expirar após 90 dias sem uso

    // Construtores
    public AlertaPesquisa() {
        this.ultimaVerificacao = LocalDateTime.now();
        this.dataCriacao = LocalDateTime.now();
    }

    public AlertaPesquisa(String emailUsuario, String termoBusca, Integer frequenciaDias) {
        this.emailUsuario = emailUsuario;
        this.termoBusca = termoBusca;
        this.frequenciaDias = frequenciaDias != null ? frequenciaDias : 7;
        this.ultimaVerificacao = LocalDateTime.now();
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }

    public boolean deveProcessar() {
        if (!ativo) return false;

        // Verifica se expirou
        if (estaExpirado()) {
            this.ativo = false;
            return false;
        }

        LocalDateTime proximaVerificacao = ultimaVerificacao.plusDays(frequenciaDias);
        return LocalDateTime.now().isAfter(proximaVerificacao);
    }

    // Verifica se passou o tempo de expiração
    public boolean estaExpirado() {
        LocalDateTime dataExpiracao = dataCriacao.plusDays(diasExpiracao);
        return LocalDateTime.now().isAfter(dataExpiracao);
    }

    // Renova o alerta (reseta o contador de expiração)
    public void renovar() {
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTermoBusca() {
        return termoBusca;
    }

    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }

    public LocalDateTime getUltimaVerificacao() {
        return ultimaVerificacao;
    }

    public void setUltimaVerificacao(LocalDateTime ultimaVerificacao) {
        this.ultimaVerificacao = ultimaVerificacao;
    }

    public Integer getFrequenciaDias() {
        return frequenciaDias;
    }

    public void setFrequenciaDias(Integer frequenciaDias) {
        this.frequenciaDias = frequenciaDias;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getDiasExpiracao() {
        return diasExpiracao;
    }

    public void setDiasExpiracao(Integer diasExpiracao) {
        this.diasExpiracao = diasExpiracao;
    }
}