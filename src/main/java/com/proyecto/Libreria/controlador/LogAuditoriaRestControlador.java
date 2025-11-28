package com.proyecto.Libreria.controlador;

import com.proyecto.Libreria.entidad.LogAuditoria;
import com.proyecto.Libreria.service.LogAuditoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogAuditoriaRestControlador {

    private final LogAuditoriaService logAuditoriaService;

    public LogAuditoriaRestControlador(LogAuditoriaService logAuditoriaService) {
        this.logAuditoriaService = logAuditoriaService;
    }

    @GetMapping
    public ResponseEntity<List<LogAuditoria>> obtenerTodosLosLogs() {
        return ResponseEntity.ok(logAuditoriaService.obtenerTodosLosLogs());
    }

    @GetMapping("/ultimos")
    public ResponseEntity<List<LogAuditoria>> obtenerUltimosLogs(@RequestParam(defaultValue = "100") int cantidad) {
        return ResponseEntity.ok(logAuditoriaService.obtenerUltimosLogs(cantidad));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<LogAuditoria>> obtenerLogsPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(logAuditoriaService.obtenerLogsPorUsuario(usuarioId));
    }

    @GetMapping("/accion/{accion}")
    public ResponseEntity<List<LogAuditoria>> obtenerLogsPorAccion(@PathVariable String accion) {
        return ResponseEntity.ok(logAuditoriaService.obtenerLogsPorAccion(accion));
    }

    @GetMapping("/entidad/{entidad}")
    public ResponseEntity<List<LogAuditoria>> obtenerLogsPorEntidad(@PathVariable String entidad) {
        return ResponseEntity.ok(logAuditoriaService.obtenerLogsPorEntidad(entidad));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<LogAuditoria>> obtenerLogsPorFecha(
            @RequestParam String inicio,
            @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return ResponseEntity.ok(logAuditoriaService.obtenerLogsPorFecha(fechaInicio, fechaFin));
    }
}
