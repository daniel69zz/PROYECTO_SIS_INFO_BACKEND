package com.sis.spring.coffix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sis.spring.coffix.exceptions.InvalidEntryInt;
import com.sis.spring.coffix.exceptions.ProductNotFound;
import com.sis.spring.coffix.exceptions.ProductStockInsufficient;
import com.sis.spring.coffix.model.Producto;
import com.sis.spring.coffix.repository.ProductoRepository;


@Service
public class ProductoService {

    @Autowired
    private ProductoRepository prod_repo;

    public Producto buscar_por_id(Integer id){
        return prod_repo.findById(id)
            .orElseThrow(() -> new ProductNotFound(id));
    }

    public List<Producto> listAll() {
        return prod_repo.findAll();
    }

    public List<Producto> buscar_por_tipo_prod(Integer idTipoProd) {
        if (idTipoProd == null || idTipoProd <= 0) {
            throw new InvalidEntryInt(idTipoProd);
        }
        return prod_repo.findByIdTipoProd(idTipoProd);
    }

    public List<Producto> buscar_por_nombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        return prod_repo.findByNombreContainingIgnoreCase(nombre.trim());
    }

    public Integer getStockProducto(Integer id){
        try{
            Integer stock = prod_repo.stockProducto(id);

            if (stock == null){
                throw new ProductNotFound(id);
            } 

            return stock;
        }catch(ProductNotFound e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Error del servidor", e);
        }
    }

    public Boolean hayStock(Integer id){
        Integer stock = getStockProducto(id);

        return stock > 0;
    }

    public void descontarStock(Integer id, Integer cantidad){
        try{
            if (cantidad == null || cantidad <= 0) {
                throw new InvalidEntryInt(cantidad);
            }
            
            Integer stockActual = getStockProducto(id);
            
            if (stockActual < cantidad) {
                throw new ProductStockInsufficient(id, cantidad);
            }
            
            prod_repo.descontarStock(id, cantidad);
        }catch(ProductNotFound | ProductStockInsufficient | InvalidEntryInt e){
            throw e;
        }catch(Exception f){
            throw new RuntimeException("Error del servidor", f);
        }
    }

     public void agregarStock(Integer idProducto, Integer cantidad, Integer idUsuario) {
        try {
            if (cantidad == null || cantidad <= 0) {
                throw new InvalidEntryInt(cantidad);
            }

            if (idUsuario == null || idUsuario <= 0) {
                throw new InvalidEntryInt(idUsuario);
            }

            if (!prod_repo.existsById(idProducto)) {
                throw new ProductNotFound(idProducto);
            }

            prod_repo.registrarRestock(idProducto, cantidad, idUsuario);

        } catch (ProductNotFound | InvalidEntryInt e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error del servidor", e);
        }
    }
}
