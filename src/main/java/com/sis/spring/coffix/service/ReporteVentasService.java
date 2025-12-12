package com.sis.spring.coffix.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sis.spring.coffix.DTO.ReporteVentasDTO;
import com.sis.spring.coffix.DTO.VentaDiaDTO;
import com.sis.spring.coffix.model.Detalle_Pedido;
import com.sis.spring.coffix.model.Pedido;
import com.sis.spring.coffix.model.PedidoPago;
import com.sis.spring.coffix.repository.PedidoPagoRepository;
import com.sis.spring.coffix.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class ReporteVentasService {

    private final PedidoPagoRepository pedidoPagoRepository;
    private final PedidoRepository pedidoRepository;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReporteVentasService(PedidoPagoRepository pedidoPagoRepository, PedidoRepository pedidoRepository) {
        this.pedidoPagoRepository = pedidoPagoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public ReporteVentasDTO obtenerReporteVentas(LocalDate fechaInicio, LocalDate fechaFin) {

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        List<PedidoPago> pagos = pedidoPagoRepository.findPagosBetweenFechasAndEstado(
                inicio, fin, "PAGADO"
        );

        if (pagos.isEmpty()) {
            return new ReporteVentasDTO(BigDecimal.ZERO, Collections.emptyList());
        }

        List<Integer> idsPedidos = pagos.stream()
                .map(PedidoPago::getId_pedido)
                .distinct()
                .collect(Collectors.toList());

        List<Pedido> pedidos = pedidoRepository.findByIdInWithDetalles(idsPedidos);
        Map<Integer, Pedido> pedidoPorId = pedidos.stream()
                .collect(Collectors.toMap(Pedido::getId_pedido, p -> p));

        BigDecimal totalIngresos = BigDecimal.ZERO;

        Map<String, AcumuladorProducto> mapa = new HashMap<>();

        for (PedidoPago pago : pagos) {
            BigDecimal monto = pago.getMonto_pagado() != null
                    ? pago.getMonto_pagado()
                    : BigDecimal.ZERO;
            totalIngresos = totalIngresos.add(monto);

            LocalDate fechaDia = pago.getFc_hora().toLocalDate();

            Pedido pedido = pedidoPorId.get(pago.getId_pedido());
            if (pedido == null || pedido.getDetalles() == null) continue;

            for (Detalle_Pedido det : pedido.getDetalles()) {
                if (det.getProducto() == null || det.getProducto().getNombre() == null) {
                    continue;
                }

                String nombreProd = det.getProducto().getNombre();
                int cant = det.getCantidad() != null ? det.getCantidad() : 0;

                BigDecimal precioUnit = det.getPrecio_u_p() != null
                        ? det.getPrecio_u_p()
                        : BigDecimal.ZERO;

                BigDecimal subtotal = precioUnit.multiply(BigDecimal.valueOf(cant));

                String clave = fechaDia.toString() + "|" + nombreProd;

                AcumuladorProducto acc = mapa.computeIfAbsent(
                        clave,
                        k -> new AcumuladorProducto(fechaDia, nombreProd)
                );

                acc.cantidadVendida += cant;
                acc.totalProducto = acc.totalProducto.add(subtotal);
            }
        }

        List<AcumuladorProducto> acumuladores = new ArrayList<>(mapa.values());
        acumuladores.sort(Comparator
                .comparing(AcumuladorProducto::getFecha)
                .thenComparing(AcumuladorProducto::getProducto));

        List<VentaDiaDTO> ventasPorDia = new ArrayList<>();
        for (AcumuladorProducto acc : acumuladores) {
            ventasPorDia.add(
                    new VentaDiaDTO(
                            acc.getFecha().format(FORMATO_FECHA),
                            acc.getProducto(),
                            acc.getCantidadVendida(),
                            acc.getTotalProducto()
                    )
            );
        }

        return new ReporteVentasDTO(totalIngresos, ventasPorDia);
    }

    private static class AcumuladorProducto {
        private final LocalDate fecha;
        private final String producto;
        private int cantidadVendida = 0;
        private BigDecimal totalProducto = BigDecimal.ZERO;

        public AcumuladorProducto(LocalDate fecha, String producto) {
            this.fecha = fecha;
            this.producto = producto;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public String getProducto() {
            return producto;
        }

        public int getCantidadVendida() {
            return cantidadVendida;
        }

        public BigDecimal getTotalProducto() {
            return totalProducto;
        }
    }
}
