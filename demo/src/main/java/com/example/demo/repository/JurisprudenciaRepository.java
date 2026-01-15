package com.example.demo.repository;

import com.example.demo.entity.Jurisprudencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JurisprudenciaRepository extends JpaRepository<Jurisprudencia, Long> {

    @Query(value = "SELECT * FROM jurisprudencia j " +
            "WHERE j.data_publicacao > :ultimaChecagem " +
            "AND (" +
            "   LOWER(COALESCE(j.texto_completo, '')) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "   OR " +
            "   LOWER(COALESCE(j.ementa, '')) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            ")",
            nativeQuery = true)
    List<Jurisprudencia> encontrarNovasDesde(
            @Param("termo") String termo,
            @Param("ultimaChecagem") LocalDateTime ultimaChecagem
    );
}