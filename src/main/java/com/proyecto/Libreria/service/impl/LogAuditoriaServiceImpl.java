package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.entidad.LogAuditoria;
import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.repositorio.LogAuditoriaRepositorio;
import com.proyecto.Libreria.service.LogAuditoriaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogAuditoriaServiceImpl implements LogAuditoriaService {

    private final LogAuditoriaRepositorio logAuditoriaRepositorio;

    public LogAuditoriaServiceImpl(LogAuditoriaRepositorio logAuditoriaRepositorio) {
        this.logAuditoriaRepositorio = logAuditoriaRepositorio;
    }

    @Override
    public void registrarLog(Usuario usuario, String accion, String entidad, Long entidadId, String descripcion) {
        if (usuario != null) {
            registrarLog(usuario.getId(), usuario.getNombreCompleto(), accion, entidad, entidadId, descripcion);
        }
    }

    @Override
    public void registrarLog(Long usuarioId, String usuarioNombre, String accion, String entidad, Long entidadId,
            String descripcion) {
        LogAuditoria log = new LogAuditoria(usuarioId, usuarioNombre, accion, entidad, entidadId, descripcion);
        logAuditoriaRepositorio.save(log);
    }

    @Override
    public List<LogAuditoria> obtenerTodosLosLogs() {
        return logAuditoriaRepositorio.findAll();
    }

    @Override
    public List<LogAuditoria> obtenerLogsPorUsuario(Long usuarioId) {
        return logAuditoriaRepositorio.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
    }

    @Override
    public List<LogAuditoria> obtenerLogsPorAccion(String accion) {
        return logAuditoriaRepositorio.findByAccionOrderByFechaHoraDesc(accion);
    }

    @Override
    public List<LogAuditoria> obtenerLogsPorEntidad(String entidad) {
        return logAuditoriaRepositorio.findByEntidadOrderByFechaHoraDesc(entidad);
    }

    @Override
    public List<LogAuditoria> obtenerLogsPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return logAuditoriaRepositorio.findByFechaHoraBetween(inicio, fin);
    }

    @Override
    public List<LogAuditoria> obtenerUltimosLogs(int cantidad) {
        return logAuditoriaRepositorio.findTop100ByOrderByFechaHoraDesc();
    }
}
