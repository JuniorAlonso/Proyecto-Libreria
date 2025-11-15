// src/main/resources/static/js/script.js

document.addEventListener('DOMContentLoaded', function() {
    // Este script asume que existirá un formulario con id="loginForm" en tu vista,
    // y campos de entrada con id="usuario" e id="contrasena".
    const loginForm = document.getElementById('loginForm'); 
    
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            const usuarioInput = document.getElementById('usuario'); 
            const contrasenaInput = document.getElementById('contrasena'); 

            let isValid = true;

            // Validar usuario
            if (!usuarioInput || usuarioInput.value.trim() === '') {
                alert('Por favor, ingresa tu usuario.');
                isValid = false;
                usuarioInput.focus();
            }

            // Validar contraseña
            if (isValid && (!contrasenaInput || contrasenaInput.value.trim() === '')) {
                alert('Por favor, ingresa tu contraseña.');
                isValid = false;
                contrasenaInput.focus();
            }

            if (!isValid) {
                event.preventDefault(); // Detener el envío del formulario si hay errores
            }
        });
    }

    console.log("Script.js cargado y listo para validaciones.");
});