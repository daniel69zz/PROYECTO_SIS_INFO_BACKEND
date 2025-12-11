package com.sis.spring.coffix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sis.spring.coffix.model.Producto;

import jakarta.transaction.Transactional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query(value = "SELECT stock_actual FROM productos WHERE id_producto = :id", nativeQuery = true)
    Integer stockProducto(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE productos SET stock_actual = stock_actual - :cant WHERE id_producto = :id", nativeQuery = true)
    void descontarStock(@Param("id") Integer id, @Param("cant") Integer cant);

    @Query("SELECT p FROM Producto p WHERE p.id_tipo_prod = :idTipoProd")
    List<Producto> findByIdTipoProd(@Param("idTipoProd") Integer idTipoProd);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO restock_productos (id_producto, cantidad, fc_hora, id_usuario)
        VALUES (:idProducto, :cantidad, NOW(), :idUsuario)
        """, nativeQuery = true)
    void registrarRestock(@Param("idProducto") Integer idProducto,
                        @Param("cantidad") Integer cantidad,
                        @Param("idUsuario") Integer idUsuario);


}
