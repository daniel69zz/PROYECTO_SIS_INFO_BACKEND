package com.sis.spring.coffix.model;

import java.math.BigDecimal;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    @Column(name = "nombre", length = 50) 
    private String nombre;

    @Column(name = "id_tipo_prod")
    private Integer id_tipo_prod;

    @Formula("(SELECT tp.nombre FROM tipos_producto tp WHERE tp.id_tipo_prod = id_tipo_prod)")
    private String nombre_tipo;

    @Column(name = "precio_actual")
    private BigDecimal precio;

    @Column(name = "stock_actual")
    private Integer stock;

    @Column(name = "path_img")
    private String path_img;

    public Producto() {
    }

    public Producto(
            Integer id_producto,
            Integer id_tipo_prod,
            String nombre,
            BigDecimal precio,
            Integer stock,
            String nombre_tipo,
            String path_img
    ) {
        this.id_producto = id_producto;
        this.id_tipo_prod = id_tipo_prod;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.nombre_tipo = nombre_tipo;
        this.path_img = path_img;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId_tipo_prod() {
        return id_tipo_prod;
    }

    public void setId_tipo_prod(Integer id_tipo_prod) {
        this.id_tipo_prod = id_tipo_prod;
    }

    // 👇 nuevo getter/setter para el nombre del tipo
    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPath_img() {
        return path_img;
    }

    public void setPath_img(String path_img) {
        this.path_img = path_img;
    }
}
