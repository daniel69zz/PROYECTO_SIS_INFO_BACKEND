// src/main/java/com/sis/spring/coffix/DTO/VentaDiaDTO.java
package com.sis.spring.coffix.DTO;

import java.math.BigDecimal;

public class VentaDiaDTO {

    private String fecha;           // dd/MM/yyyy
    private String producto;        // nombre del producto
    private int cantidadVendida;    // unidades vendidas de ese producto en ese día
    private BigDecimal totalProducto; // total dinero de ese producto ese día

    public VentaDiaDTO() {
    }

    public VentaDiaDTO(String fecha,
                       String producto,
                       int cantidadVendida,
                       BigDecimal totalProducto) {
        this.fecha = fecha;
        this.producto = producto;
        this.cantidadVendida = cantidadVendida;
        this.totalProducto = totalProducto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public BigDecimal getTotalProducto() {
        return totalProducto;
    }

    public void setTotalProducto(BigDecimal totalProducto) {
        this.totalProducto = totalProducto;
    }
}
