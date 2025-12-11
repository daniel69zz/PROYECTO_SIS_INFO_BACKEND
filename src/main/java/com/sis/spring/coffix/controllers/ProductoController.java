package com.sis.spring.coffix.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sis.spring.coffix.exceptions.InvalidEntryInt;
import com.sis.spring.coffix.exceptions.ProductNotFound;
import com.sis.spring.coffix.model.Producto;
import com.sis.spring.coffix.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService prod_serv;

    @GetMapping
    public List<Producto> listarProductos() {
        return prod_serv.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener_por_id(@PathVariable Integer id){
        try{
            Producto prod = prod_serv.buscar_por_id(id);
            return ResponseEntity.ok(prod);
        }catch(ProductNotFound e){
            return ResponseEntity.status(404).body(
                Map.of(
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", 404
                )
            );
        }
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<?> getStockProducto(@PathVariable Integer id) {
        try{
            Integer stock = prod_serv.getStockProducto(id);
            return ResponseEntity.ok(stock);

        }catch(ProductNotFound e){
            return ResponseEntity.status(404).body(
                Map.of(
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", 404
                )
            );
        }catch(Exception e){
            return ResponseEntity.status(500).body(
                Map.of(
                    "error", "Error interno del servidor",
                    "timestamp", LocalDateTime.now(),
                    "status", 500
                )
            );
        }
    }

    @GetMapping("/tipo/{idTipoProd}")
    public ResponseEntity<?> listarPorTipo(@PathVariable Integer idTipoProd) {
        try {
            List<Producto> productos = prod_serv.buscar_por_tipo_prod(idTipoProd);
            return ResponseEntity.ok(productos);
        } catch (InvalidEntryInt e) {
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

    // GET /api/productos/buscar?nombre=cafe
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam("nombre") String nombre) {
        try {
            List<Producto> productos = prod_serv.buscar_por_nombre(nombre);
            return ResponseEntity.ok(productos);
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

    @PostMapping("/{id}/restock")
    public ResponseEntity<Void> restockProducto(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {

        prod_serv.agregarStock(id, cantidad, 1);

        return ResponseEntity.ok().build();
}



}
