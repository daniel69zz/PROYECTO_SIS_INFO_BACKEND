package com.sis.spring.coffix.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sis.spring.coffix.DTO.PedidoCrearDTO;
import com.sis.spring.coffix.exceptions.PedidoNotFound;
import com.sis.spring.coffix.model.Pedido;
import com.sis.spring.coffix.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // GET /api/pedidos  -> todos
    // GET /api/pedidos?estado=Listo  
    @GetMapping
    public List<Pedido> listarPedidos(@RequestParam(required = false) String estado) {
        if (estado != null && !estado.isBlank()) {
            return pedidoService.obtenerPorEstado(estado);
        }
        return pedidoService.obtenerTodos();
    }

    // GET /api/pedidos/buscar?codigo=PED-123
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorCodigo(@RequestParam("codigo") String codigo) {
        try {
            List<Pedido> pedidos = pedidoService.buscarPorCodigo(codigo);
            return ResponseEntity.ok(pedidos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(
                Map.of(
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", 400
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                Map.of(
                    "error", "Error interno del servidor",
                    "timestamp", LocalDateTime.now(),
                    "status", 500
                )
            );
        }
    }

    // PATCH /api/pedidos/5/estado/LISTO
    @PatchMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<?> act_estado(@PathVariable Integer id, @PathVariable String nuevoEstado) {
        try{
            Pedido actualizado = pedidoService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);
        }catch(PedidoNotFound e){
            return ResponseEntity.status(404).body(
                Map.of(
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime. now(),
                    "status", 404
                )
            );
        }
    }


    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoCrearDTO request) {
        try {
            pedidoService.crearPedido(request);

            return ResponseEntity.ok(
                    Map.of(
                            "status", 201,
                            "message", "Pedido registrado exitosamente"
                    )
            );


        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", "ERROR",
                            "message", e.getMessage()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    Map.of(
                            "status", "ERROR",
                            "message", "Error interno del servidor"
                    )
            );
        }
    }

}
