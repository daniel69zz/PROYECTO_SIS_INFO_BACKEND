package com.sis.spring.coffix.repository;

import com.sis.spring.coffix.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

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

    // este lo puedes dejar para otros usos donde NO necesites detalles/producto
    List<Pedido> findByEstado(String estado);

    // NUEVO: obtener varios pedidos (con detalles + productos) por lista de IDs
    @Query("""
           SELECT DISTINCT p
           FROM Pedido p
           LEFT JOIN FETCH p.detalles d
           LEFT JOIN FETCH d.producto prod
           WHERE p.id_pedido IN :ids
           """)
    List<Pedido> findByIdInWithDetalles(@Param("ids") List<Integer> ids);
}
