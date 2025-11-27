let libroEditandoId = null;

function openModal() {
    libroEditandoId = null;
    document.getElementById('libroModal').classList.add('active');
    document.getElementById('modalTitle').textContent = 'Agregar Libro';
    document.getElementById('libroForm').reset();
    document.getElementById('libroId').value = '';
}

function closeModal() {
    document.getElementById('libroModal').classList.remove('active');
    libroEditandoId = null;
}

async function cargarLibros() {
    try {
        const response = await fetch('/api/libros');
        const libros = await response.json();

        const tbody = document.querySelector('#librosTable tbody');
        tbody.innerHTML = '';

        libros.forEach(libro => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${libro.id}</td>
                <td><strong>${libro.titulo}</strong></td>
                <td>${libro.autor}</td>
                <td>${libro.stock}</td>
                <td>${libro.formato || 'Físico'}</td>
                <td class="action-links">
                    <button class="btn btn-primary" style="padding: 0.4rem 0.8rem; font-size: 0.9rem;" onclick="editLibro(${libro.id})">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>
                    <button class="btn btn-danger" style="padding: 0.4rem 0.8rem; font-size: 0.9rem;" onclick="deleteLibro(${libro.id})">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error al cargar libros:', error);
    }
}

async function editLibro(id) {
    try {
        const response = await fetch(`/api/libros/${id}`);
        const libro = await response.json();

        libroEditandoId = id;
        document.getElementById('libroModal').classList.add('active');
        document.getElementById('modalTitle').textContent = 'Editar Libro';

        document.getElementById('libroId').value = libro.id;
        document.getElementById('titulo').value = libro.titulo || '';
        document.getElementById('autor').value = libro.autor || '';
        document.getElementById('isbn').value = libro.isbn || '';
        document.getElementById('stock').value = libro.stock || 0;
        document.getElementById('imagenUrl').value = libro.imagenUrl || '';
        document.getElementById('archivoUrl').value = libro.archivoUrl || '';
        document.getElementById('formato').value = libro.formato || '';

    } catch (error) {
        console.error('Error al cargar libro:', error);
        alert('Error al cargar el libro');
    }
}

async function deleteLibro(id) {
    if (confirm('¿Estás seguro de eliminar este libro?')) {
        try {
            const response = await fetch(`/api/libros/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Libro eliminado exitosamente');
                cargarLibros();
            } else {
                const errorMsg = await response.text();
                alert(errorMsg || 'Error al eliminar el libro');
            }
        } catch (error) {
            console.error('Error al eliminar libro:', error);
            alert('Error al eliminar el libro');
        }
    }
}

document.getElementById('libroForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const libro = {
        titulo: document.getElementById('titulo').value,
        autor: document.getElementById('autor').value,
        isbn: document.getElementById('isbn').value,
        stock: parseInt(document.getElementById('stock').value) || 0,
        imagenUrl: document.getElementById('imagenUrl').value,
        archivoUrl: document.getElementById('archivoUrl').value,
        formato: document.getElementById('formato').value
    };

    if (libroEditandoId) {
        libro.id = libroEditandoId;
    }

    try {
        const url = libroEditandoId ? `/api/libros/${libroEditandoId}` : '/api/libros';
        const method = libroEditandoId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(libro)
        });

        if (response.ok) {
            alert('Libro guardado exitosamente');
            closeModal();
            cargarLibros();
        } else {
            alert('Error al guardar el libro');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar el libro');
    }
});

document.getElementById('libroModal').addEventListener('click', function (e) {
    if (e.target === this) {
        closeModal();
    }
});

// Cargar libros al iniciar la página
document.addEventListener('DOMContentLoaded', cargarLibros);
