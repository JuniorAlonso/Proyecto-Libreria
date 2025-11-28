let todosLosLogs = [];

async function cargarLogs() {
    try {
        const response = await fetch('/api/logs/ultimos?cantidad=100');
        if (!response.ok) {
            todosLosLogs = [];
            mostrarLogs(todosLosLogs);
            return;
        }
        const data = await response.json();
        todosLosLogs = Array.isArray(data) ? data : [];
        mostrarLogs(todosLosLogs);
    } catch (error) {
        console.log('Logs no disponibles aun');
        todosLosLogs = [];
        const tbody = document.querySelector('#logsTable tbody');
        if (tbody) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 2rem; color: #666;"><i class="fa-solid fa-info-circle"></i><br><br>No hay registros de auditoria disponibles.<br>Los logs se generaran automaticamente cuando se realicen acciones en el sistema.</td></tr>';
        }
    }
}

function mostrarLogs(logs) {
    const tbody = document.querySelector('#logsTable tbody');
    if (!tbody) return;
    
    tbody.innerHTML = '';

    if (!logs || logs.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 2rem; color: #666;"><i class="fa-solid fa-info-circle"></i><br><br>No hay registros disponibles.<br>Los logs se generaran cuando se realicen acciones en el sistema.</td></tr>';
        return;
    }

    logs.forEach(log => {
        const fecha = new Date(log.fechaHora).toLocaleString('es-ES');
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td style="font-size: 0.85rem;">${fecha}</td>
            <td><strong>${log.usuarioNombre || 'Sistema'}</strong></td>
            <td><span class="badge-accion">${log.accion}</span></td>
            <td><span class="badge-entidad">${log.entidad}</span></td>
            <td style="max-width: 300px;">${log.descripcion || 'N/A'}</td>
        `;
        tbody.appendChild(tr);
    });
}

function filtrarLogs() {
    const accionSeleccionada = document.getElementById('filtroAccion').value;
    const entidadSeleccionada = document.getElementById('filtroEntidad').value;
    const fechaInicio = document.getElementById('fechaInicio').value;
    const fechaFin = document.getElementById('fechaFin').value;

    let logsFiltrados = [...todosLosLogs];

    if (accionSeleccionada) {
        logsFiltrados = logsFiltrados.filter(log => log.accion === accionSeleccionada);
    }
    if (entidadSeleccionada) {
        logsFiltrados = logsFiltrados.filter(log => log.entidad === entidadSeleccionada);
    }
    if (fechaInicio) {
        const inicio = new Date(fechaInicio);
        logsFiltrados = logsFiltrados.filter(log => new Date(log.fechaHora) >= inicio);
    }
    if (fechaFin) {
        const fin = new Date(fechaFin);
        fin.setHours(23, 59, 59, 999);
        logsFiltrados = logsFiltrados.filter(log => new Date(log.fechaHora) <= fin);
    }

    mostrarLogs(logsFiltrados);
}

function limpiarFiltros() {
    document.getElementById('filtroAccion').value = '';
    document.getElementById('filtroEntidad').value = '';
    document.getElementById('fechaInicio').value = '';
    document.getElementById('fechaFin').value = '';
    mostrarLogs(todosLosLogs);
}

function exportarLogs() {
    if (!todosLosLogs || todosLosLogs.length === 0) {
        alert('No hay logs para exportar');
        return;
    }
    
    let csv = 'ID,Fecha/Hora,Usuario,Accion,Entidad,Descripcion\n';
    todosLosLogs.forEach(log => {
        const fecha = new Date(log.fechaHora).toLocaleString('es-ES');
        csv += `${log.id},"${fecha}","${log.usuarioNombre || 'Sistema'}","${log.accion}","${log.entidad}","${(log.descripcion || '').replace(/"/g, '""')}"\n`;
    });
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `logs_auditoria_${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
}

document.addEventListener('DOMContentLoaded', cargarLogs);
setInterval(cargarLogs, 30000);
