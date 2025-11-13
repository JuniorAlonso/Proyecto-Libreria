@echo off
del /F /Q target\classes\static\js\admin-libros.js 2>nul
echo F | xcopy /Y src\main\resources\static\js\admin-usuarios.js target\classes\static\js\admin-libros.js
echo Archivo copiado exitosamente
dir target\classes\static\js\admin-libros.js
