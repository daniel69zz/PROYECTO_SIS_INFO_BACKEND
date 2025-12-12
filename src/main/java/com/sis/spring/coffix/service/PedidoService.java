package com.sis.spring.coffix.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sis.spring.coffix.DTO.ItemPedidoDTO;
import com.sis.spring.coffix.DTO.PedidoCrearDTO;
import com.sis.spring.coffix.exceptions.PedidoNotFound;
import com.sis.spring.coffix.model.Detalle_Pedido;
import com.sis.spring.coffix.model.Pedido;
import com.sis.spring.coffix.model.PedidoPago;
import com.sis.spring.coffix.model.Producto;
import com.sis.spring.coffix.repository.PedidoPagoRepository;
import com.sis.spring.coffix.repository.PedidoRepository;
import com.sis.spring.coffix.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoPagoRepository pedidoPagoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            PedidoPagoRepository pedidoPagoRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoPagoRepository = pedidoPagoRepository;
        this.productoRepository = productoRepository;
    }

    public List<Pedido> obtenerTodos() {
        if (!esHoraPico()) {
            List<Pedido> pedidos = pedidoRepository.findAllWithDetalles();
            for (Pedido p : pedidos) {
                p.setPrioridad(null);
                p.setPrioridadLabel(null);
            }
            return pedidos;
        }

        List<Integer> idsOrdenados = pedidoRepository.findIdsOrdenHoraPico();
        if (idsOrdenados.isEmpty()) return List.of();

        List<Pedido> pedidos = pedidoRepository.findByIdInWithDetalles(idsOrdenados);

        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < idsOrdenados.size(); i++) {
            pos.put(idsOrdenados.get(i), i);
        }
        pedidos.sort(Comparator.comparingInt(p -> pos.getOrDefault(p.getId_pedido(), Integer.MAX_VALUE)));

        for (Pedido p : pedidos) {
            int pr = switch (p.getEstado()) {
                case "Listo" -> 1;
                case "En preparacion" -> 2; 
                case "Pendiente" -> 3;
                default -> 4;
            };

            p.setPrioridad(pr);

            String label = switch (pr) {
                case 1 -> "Alta";
                case 2 -> "Media";
                case 3 -> "Baja";
                default -> "Sin definir";
            };
            p.setPrioridadLabel(label);
        }

        return pedidos;
    }

    private boolean esHoraPico() {
        LocalTime ahora = LocalTime.now();
        LocalTime inicio = LocalTime.of(12, 0);
        LocalTime fin = LocalTime.of(14, 0);

        return !ahora.isBefore(inicio) && ahora.isBefore(fin);
    }

    public List<Pedido> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstadoWithDetalles(estado);
    }

    public List<Pedido> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del pedido no puede estar vacío");
        }
        List<Pedido> pedidos = pedidoRepository.findByCodigoPedidoContainingIgnoreCase(codigo);

        if (!esHoraPico()) {
            for (Pedido p : pedidos) {
                p.setPrioridad(null);
                p.setPrioridadLabel(null);
            }
            return pedidos;
        }

        for (Pedido p : pedidos) {
            int pr = switch (p.getEstado()) {
                case "Listo" -> 1;
                case "En preparacion" -> 2;
                case "Pendiente" -> 3;
                default -> 4;
            };

            p.setPrioridad(pr);

            String label = switch (pr) {
                case 1 -> "Alta";
                case 2 -> "Media";
                case 3 -> "Baja";
                default -> "Sin definir";
            };
            p.setPrioridadLabel(label);
        }

        return pedidos;
    }

    @Transactional
    public Pedido actualizarEstado(Integer idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findByIdWithDetalles(idPedido)
                .orElseThrow(() -> new PedidoNotFound(idPedido));

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido crearPedido(PedidoCrearDTO request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }

        Pedido pedido = new Pedido();

        String codigoGenerado = generarCodigoPedido(request.getNombreCliente());
        pedido.setCod_pedido(codigoGenerado);

        pedido.setEstado("Pendiente");
        pedido.setFc_hora(LocalDateTime.now());

        pedido.setId_usuario(2);

        List<Detalle_Pedido> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoDTO item : request.getItems()) {
            Producto prod = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getIdProducto()
                    ));
                    
            // VERIFICAR STOCK PRODUCTO PARA REGISTRAR PEDIDO        
            Integer stockActual = productoRepository.stockProducto(prod.getId_producto());
            if (stockActual == null || stockActual < item.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto " + prod.getNombre()
                );
            }

            Detalle_Pedido det = new Detalle_Pedido();
            det.setPedido(pedido);
            det.setProducto(prod);
            det.setCantidad(item.getCantidad());
            det.setPrecio_u_p(prod.getPrecio());

            detalles.add(det);

            BigDecimal precioUnit = prod.getPrecio();
            BigDecimal subTotal = precioUnit.multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subTotal);

            productoRepository.descontarStock(prod.getId_producto(), item.getCantidad());
        }

        pedido.setDetalles(detalles);

        // 3) Guardar pedido (genera id_pedido)
        Pedido guardado = pedidoRepository.save(pedido);

        // 4) Crear registro de pago
        PedidoPago pago = new PedidoPago();
        pago.setId_pedido(guardado.getId_pedido());
        pago.setFc_hora(LocalDateTime.now());

        BigDecimal montoPagado = request.getMontoPagado() != null
                ? request.getMontoPagado()
                : BigDecimal.ZERO;

        pago.setMonto_pagado(montoPagado);

        if (montoPagado.compareTo(total) <= 0) {
            throw new IllegalArgumentException(
                    "Monto pagado insuficiente. Debe ser mayor al total. Total: " + total + ", Pagado: " + montoPagado
            );
        }

        pago.setEstado("PAGADO");
        pedidoPagoRepository.save(pago);

        return pedidoRepository.save(guardado);
    }


    private String generarCodigoBase() {
        return "PED-" + System.currentTimeMillis();
    }

    private String generarCodigoPedido(String nombreCliente) {
        String base = generarCodigoBase();

        if (nombreCliente == null || nombreCliente.isBlank()) {
            return base;
        }

        return base + "-" + nombreCliente.trim();
    }
}
