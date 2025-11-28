package com.proyecto.Libreria.service;

import com.proyecto.Libreria.entidad.LogAuditoria;
import com.proyecto.Libreria.entidad.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public interface LogAuditoriaService {

    void registrarLog(Usuario usuario, String accion, String entidad, Long entidadId, String descripcion);

    void registrarLog(Long usuarioId, String usuarioNombre, String accion, String entidad, Long entidadId,
            String descripcion);

    List<LogAuditoria> obtenerTodosLosLogs();

    List<LogAuditoria> obtenerLogsPorUsuario(Long usuarioId);

    List<LogAuditoria> obtenerLogsPorAccion(String accion);

    List<LogAuditoria> obtenerLogsPorEntidad(String entidad);

    List<LogAuditoria> obtenerLogsPorFecha(LocalDateTime inicio, LocalDateTime fin);

    List<LogAuditoria> obtenerUltimosLogs(int cantidad);
}
