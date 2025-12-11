package com.sis.spring.coffix.DTO;

import java.math.BigDecimal;
import java.util.List;

public class ReporteVentasDTO {

    private BigDecimal totalIngresos;        // suma de todos los días
    private List<VentaDiaDTO> ventasPorDia;  // lista de días

    public ReporteVentasDTO() {
    }

    public ReporteVentasDTO(BigDecimal totalIngresos, List<VentaDiaDTO> ventasPorDia) {
        this.totalIngresos = totalIngresos;
        this.ventasPorDia = ventasPorDia;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public List<VentaDiaDTO> getVentasPorDia() {
        return ventasPorDia;
    }

    public void setVentasPorDia(List<VentaDiaDTO> ventasPorDia) {
        this.ventasPorDia = ventasPorDia;
    }
}
