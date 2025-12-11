package com.sis.spring.coffix.exceptions;

public class ProductStockInsufficient extends RuntimeException {
    public ProductStockInsufficient(Integer id, Integer cant){
        super("Stock insuficiente:  Disponible: " + id + " Solicitado: " + cant);
    }
}
