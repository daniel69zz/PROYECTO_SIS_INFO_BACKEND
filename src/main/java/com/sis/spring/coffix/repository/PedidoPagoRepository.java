package com.sis.spring.coffix.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sis.spring.coffix.model.PedidoPago;

public interface PedidoPagoRepository extends JpaRepository<PedidoPago, Integer> {
    @Query("""
           SELECT p
           FROM PedidoPago p
           WHERE p.fc_hora BETWEEN :inicio AND :fin
             AND p.estado = :estado
           """)
    List<PedidoPago> findPagosBetweenFechasAndEstado(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("estado") String estado
    );
}
