package com.proyecto.Libreria.repositorio;

import com.proyecto.Libreria.entidad.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioId(Long usuarioId);

    Pago findByMercadoPagoId(String mercadoPagoId);
}
