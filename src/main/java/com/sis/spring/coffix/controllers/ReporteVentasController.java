package com.sis.spring.coffix.controllers;

import com.sis.spring.coffix.DTO.ReporteVentasDTO;
import com.sis.spring.coffix.service.ReporteVentasService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
public class ReporteVentasController {

    private final ReporteVentasService reporteVentasService;

    public ReporteVentasController(ReporteVentasService reporteVentasService) {
        this.reporteVentasService = reporteVentasService;
    }

    // GET /api/reportes/ventas?inicio=2025-02-10&fin=2025-02-13
    @GetMapping("/ventas")
    public ReporteVentasDTO getReporteVentas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin
    ) {
        return reporteVentasService.obtenerReporteVentas(inicio, fin);
    }
}
