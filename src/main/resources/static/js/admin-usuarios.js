let usuarioEditandoId = null;

function openModal() {
    usuarioEditandoId = null;
    document.getElementById('usuarioModal').classList.add('active');
    document.getElementById('modalTitle').textContent = 'Agregar Usuario';
    document.getElementById('usuarioForm').reset();
    // Mostrar campo de contraseña en nuevo usuario
    document.getElementById('contrasenaGroup').style.display = 'block';
}

function closeModal() {
    document.getElementById('usuarioModal').classList.remove('active');
    usuarioEditandoId = null;
}

async function cargarUsuarios() {
    try {
        const response = await fetch('/api/usuarios');
        const usuarios = await response.json();

        const tbody = document.querySelector('#usuariosTable tbody');
        tbody.innerHTML = '';

        usuarios.forEach(usuario => {
            const tr = document.createElement('tr');
            const estadoBadge = usuario.activo 
                ? '<span class="badge-estado activo">Activo</span>' 
                : '<span class="badge-estado inactivo">Inactivo</span>';
            
            const botonEstado = usuario.activo
                ? `<button class="btn-warning" onclick="desactivarUsuario(${usuario.id})">
                       <i class="fa-solid fa-ban"></i> Desactivar
                   </button>`
                : `<button class="btn-success" onclick="activarUsuario(${usuario.id})">
                       <i class="fa-solid fa-check"></i> Activar
                   </button>`;
            
            tr.innerHTML = `
                <td>${usuario.id}</td>
                <td><strong>${usuario.nombreCompleto || 'N/A'}</strong></td>
                <td>${usuario.correo}</td>
                <td>${usuario.telefono || 'N/A'}</td>
                <td>
                    <span class="badge-rol ${getBadgeClass(usuario.rol)}">
                        ${getBadgeText(usuario.rol)}
                    </span>
                </td>
                <td>${estadoBadge}</td>
                <td>
                    <button class="btn-edit" onclick="editUsuario(${usuario.id})">
                        <i class="fa-solid fa-edit"></i> Editar
                    </button>
                    ${botonEstado}
                    <button class="btn-delete" onclick="deleteUsuario(${usuario.id})">
                        <i class="fa-solid fa-trash"></i> Eliminar
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
    }
}

async function editUsuario(id) {
    try {
        const response = await fetch(`/api/usuarios/${id}`);
        const usuario = await response.json();

        usuarioEditandoId = id;
        document.getElementById('usuarioModal').classList.add('active');
        document.getElementById('modalTitle').textContent = 'Editar Usuario';

        document.getElementById('nombre').value = usuario.nombreCompleto || '';
        document.getElementById('correo').value = usuario.correo || '';
        document.getElementById('telefono').value = usuario.telefono || '';
        document.getElementById('rol').value = usuario.rol || 'USUARIO';

        // Ocultar campo de contraseña en edición
        document.getElementById('contrasenaGroup').style.display = 'none';
    } catch (error) {
        console.error('Error al cargar usuario:', error);
        alert('Error al cargar el usuario');
    }
}

async function deleteUsuario(id) {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
        try {
            const response = await fetch(`/api/usuarios/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Usuario eliminado exitosamente');
                cargarUsuarios();
            } else {
                const errorMsg = await response.text();
                alert(errorMsg || 'Error al eliminar el usuario');
            }
        } catch (error) {
            console.error('Error al eliminar usuario:', error);
            alert('Error al eliminar el usuario');
        }
    }
}

document.getElementById('usuarioForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const usuario = {
        nombreCompleto: document.getElementById('nombre').value,
        correo: document.getElementById('correo').value,
        rol: document.getElementById('rol').value,
        telefono: document.getElementById('telefono').value
    };

    // Solo incluir contraseña si no está vacía
    const contrasena = document.getElementById('contrasena').value;
    if (contrasena) {
        usuario.contrasena = contrasena;
    }

    try {
        const url = usuarioEditandoId ? `/api/usuarios/${usuarioEditandoId}` : '/api/usuarios';
        const method = usuarioEditandoId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuario)
        });

        if (response.ok) {
            alert('Usuario guardado exitosamente');
            closeModal();
            cargarUsuarios();
        } else {
            alert('Error al guardar el usuario');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar el usuario');
    }
});

document.getElementById('usuarioModal').addEventListener('click', function (e) {
    if (e.target === this) {
        closeModal();
    }
});

// Cargar usuarios al iniciar la página
document.addEventListener('DOMContentLoaded', cargarUsuarios);

function getBadgeClass(rol) {
    if (!rol) return 'usuario';
    const r = rol.toUpperCase();
    if (r === 'ADMIN') return 'admin';
    if (r === 'BIBLIOTECARIO') return 'bibliotecario';
    return 'usuario';
}

function getBadgeText(rol) {
    if (!rol) return 'Usuario';
    const r = rol.toUpperCase();
    if (r === 'ADMIN') return 'Admin';
    if (r === 'BIBLIOTECARIO') return 'Bibliotecario';
    if (r === 'TESORERO') return 'Tesorero';
    return 'Usuario';
}

async function desactivarUsuario(id) {
    if (confirm('¿Estas seguro de desactivar este usuario?')) {
        try {
            const response = await fetch(`/api/usuarios/${id}/desactivar`, {
                method: 'PATCH'
            });
            if (response.ok) {
                alert('Usuario desactivado exitosamente');
                cargarUsuarios();
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }
}

async function activarUsuario(id) {
    if (confirm('¿Estas seguro de activar este usuario?')) {
        try {
            const response = await fetch(`/api/usuarios/${id}/activar`, {
                method: 'PATCH'
            });
            if (response.ok) {
                alert('Usuario activado exitosamente');
                cargarUsuarios();
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }
}
