package com.example.demo.service;

import com.example.demo.entity.Jurisprudencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarRelatorio(String destinatario, List<Jurisprudencia> novidades) {
        // montando o corpo do e-mail (Simples texto por enquanto)
        StringBuilder textoEmail = new StringBuilder();
        textoEmail.append("Olá! Encontramos novas jurisprudências para sua pesquisa:\n\n");

        for (Jurisprudencia jur : novidades) {
            textoEmail.append("Data: ").append(jur.getDataPublicacao()).append("\n");
            textoEmail.append("Resumo: ").append(jur.getEmenta().substring(0, Math.min(jur.getEmenta().length(), 100))).append("...\n");
        }

        // criando a mensagem
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("apenasteste@com");
        message.setTo(destinatario);
        message.setSubject("Alerta de Jurisprudência: " + novidades.size() + " novos resultados");
        message.setText(textoEmail.toString());

        
        mailSender.send(message);
        System.out.println("E-mail enviado para: " + destinatario);
    }
}
