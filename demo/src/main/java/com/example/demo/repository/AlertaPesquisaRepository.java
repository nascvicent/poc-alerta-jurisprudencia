package com.example.demo.repository;

import com.example.demo.entity.AlertaPesquisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertaPesquisaRepository extends JpaRepository<AlertaPesquisa, Long> {
    List<AlertaPesquisa> findByAtivoFalseAndDataCriacaoBefore(LocalDateTime dataLimite);

    List<AlertaPesquisa> findByEmailUsuario(String email);
}