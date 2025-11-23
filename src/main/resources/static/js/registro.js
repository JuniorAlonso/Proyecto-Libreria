document.addEventListener('DOMContentLoaded', () => {
    // Referencias a los campos
    const dniInput = document.getElementById('dni-input');
    const nombreCompletoInput = document.getElementById('nombre-completo-input');
    const primerApellidoInput = document.getElementById('primer-apellido-input');
    const segundoApellidoInput = document.getElementById('segundo-apellido-input');
    
    // Referencias a los botones y mensajes
    const btnVerificar = document.getElementById('btn-verificar');
    const btnConfirmar = document.getElementById('btn-confirmar');
    const dniStatus = document.getElementById('dni-status'); // Mensaje de estado

    // Estado de la verificación
    let datosVerificados = false;

    // Inicialmente, deshabilitar el botón Confirmar
    if (btnConfirmar) {
        btnConfirmar.disabled = true;
    }

    // --- Lógica del Botón Verificar ---
    if (btnVerificar) {
        btnVerificar.addEventListener('click', () => {
            const dni = dniInput.value.trim();
            if (dni.length === 8 && /^\d+$/.test(dni)) {
                consultarDatosDNI(dni);
            } else {
                mostrarMensaje('Ingrese un DNI válido de 8 dígitos.', 'error');
                habilitarCamposPersona();
            }
        });
    }

    // --- Funciones de Utilidad ---
    function mostrarMensaje(mensaje, tipo) {
        dniStatus.textContent = mensaje;
        dniStatus.className = 'status-message ' + tipo;
    }

    function habilitarCamposPersona(habilitar = true) {
        if (nombreCompletoInput) nombreCompletoInput.disabled = !habilitar;
        if (primerApellidoInput) primerApellidoInput.disabled = !habilitar;
        if (segundoApellidoInput) segundoApellidoInput.disabled = !habilitar;
    }

    function limpiarCamposPersona() {
        if (nombreCompletoInput) nombreCompletoInput.value = '';
        if (primerApellidoInput) primerApellidoInput.value = '';
        if (segundoApellidoInput) segundoApellidoInput.value = '';
    }

    function actualizarEstadoConfirmar() {
        if (btnConfirmar) {
            btnConfirmar.disabled = !datosVerificados;
        }
    }

    // --- Función Principal de Consulta y Comparación ---
    function consultarDatosDNI(dni) {
        mostrarMensaje('Verificando datos...', 'info');
        btnVerificar.disabled = true;
        datosVerificados = false;
        actualizarEstadoConfirmar();
        
        const url = `/api/dni/consultar/${dni}`;
        
        fetch(url)
            .then(response => {
                if (response.status === 404) {
                    throw new Error('DNI no encontrado en el registro.');
                }
                if (!response.ok) {
                    throw new Error('Error de servicio en el servidor.');
                }
                return response.json();
            })
            .then(data => {
                // Obtener datos ingresados por el usuario
                const userNombres = nombreCompletoInput.value.trim().toUpperCase();
                const userPrimerA = primerApellidoInput.value.trim().toUpperCase();
                const userSegundoA = segundoApellidoInput.value.trim().toUpperCase();

                // Obtener datos de la API (convertidos a mayúsculas para comparación)
                const apiNombres = (data.nombres || '').toUpperCase();
                const apiPrimerA = (data.apellidoPaterno || '').toUpperCase();
                const apiSegundoA = (data.apellidoMaterno || '').toUpperCase();

                // 3. COMPARACIÓN
                if (userNombres === apiNombres && 
                    userPrimerA === apiPrimerA && 
                    userSegundoA === apiSegundoA) 
                {
                    mostrarMensaje('¡Verificación CORRECTA! Datos coinciden.', 'success');
                    datosVerificados = true;
                    // Opcional: Bloquear los campos para edición si son correctos
                    habilitarCamposPersona(false); 
                } else {
                    mostrarMensaje('Error: Los datos ingresados NO COINCIDEN con el DNI verificado.', 'error');
                    datosVerificados = false;
                    // Sugerir rellenar con los datos de la API (opcional)
                    // limpiarCamposPersona();
                    // nombreCompletoInput.value = data.nombres || '';
                    // ...
                }
                actualizarEstadoConfirmar();
            })
            .catch(error => {
                console.error('Error de validación:', error);
                mostrarMensaje(`Error: ${error.message}. Verifique e intente de nuevo.`, 'error');
                datosVerificados = false;
                actualizarEstadoConfirmar();
            })
            .finally(() => {
                btnVerificar.disabled = false;
            });
    }
});