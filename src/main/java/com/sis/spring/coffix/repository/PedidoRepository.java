package com.sis.spring.coffix.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sis.spring.coffix.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Query("""
       SELECT p.id_pedido
       FROM Pedido p
       ORDER BY
       CASE p.estado
              WHEN 'Listo' THEN 1
              WHEN 'En preparacion' THEN 2
              WHEN 'Pendiente' THEN 3
              ELSE 4
       END,
       p.fc_hora ASC
    """)
    List<Integer> findIdsOrdenHoraPico();


    @Query("""
           SELECT DISTINCT p
           FROM Pedido p
           LEFT JOIN FETCH p.detalles d
           LEFT JOIN FETCH d.producto prod
           WHERE p.id_pedido = :id
           """)
    Optional<Pedido> findByIdWithDetalles(@Param("id") Integer id);

    @Query("""
           SELECT DISTINCT p
           FROM Pedido p
           LEFT JOIN FETCH p.detalles d
           LEFT JOIN FETCH d.producto prod
           """)
    List<Pedido> findAllWithDetalles();

    @Query("""
           SELECT DISTINCT p
           FROM Pedido p
           LEFT JOIN FETCH p.detalles d
           LEFT JOIN FETCH d.producto prod
           WHERE p.estado = :estado
           """)
    List<Pedido> findByEstadoWithDetalles(@Param("estado") String estado);

    List<Pedido> findByEstado(String estado);

    @Query("""
           SELECT DISTINCT p
           FROM Pedido p
           LEFT JOIN FETCH p.detalles d
           LEFT JOIN FETCH d.producto prod
           WHERE p.id_pedido IN :ids
           """)
    List<Pedido> findByIdInWithDetalles(@Param("ids") List<Integer> ids);

    @Query("""
           SELECT DISTINCT p
           FROM Pedido p
           LEFT JOIN FETCH p.detalles d
           LEFT JOIN FETCH d.producto prod
           WHERE LOWER(p.cod_pedido) LIKE LOWER(CONCAT('%', :codigo, '%'))
           """)
    List<Pedido> findByCodigoPedidoContainingIgnoreCase(@Param("codigo") String codigo);
}
