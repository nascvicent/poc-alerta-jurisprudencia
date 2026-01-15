package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.Data; // Do Lombok
import java.time.LocalDateTime;

// exemplo
@Entity
@Data
public class Jurisprudencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String ementa;

    @Column(columnDefinition = "TEXT")
    private String textoCompleto;

    private LocalDateTime dataPublicacao;

    private String numero;
}