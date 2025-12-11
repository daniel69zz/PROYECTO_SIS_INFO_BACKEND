package com.sis.spring.coffix.DTO;


import java.math.BigDecimal;
import java.util.List;


public class PedidoCrearDTO {

    private String nombreCliente;
    private BigDecimal montoPagado;
    private List<ItemPedidoDTO> items;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public List<ItemPedidoDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoDTO> items) {
        this.items = items;
    }
}
