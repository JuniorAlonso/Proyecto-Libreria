package com.proyecto.Libreria.repositorio;

import com.proyecto.Libreria.entidad.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LogAuditoriaRepositorio extends JpaRepository<LogAuditoria, Long> {

    List<LogAuditoria> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    List<LogAuditoria> findByAccionOrderByFechaHoraDesc(String accion);

    List<LogAuditoria> findByEntidadOrderByFechaHoraDesc(String entidad);

    @Query("SELECT l FROM LogAuditoria l WHERE l.fechaHora BETWEEN :inicio AND :fin ORDER BY l.fechaHora DESC")
    List<LogAuditoria> findByFechaHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    List<LogAuditoria> findTop100ByOrderByFechaHoraDesc();
}
