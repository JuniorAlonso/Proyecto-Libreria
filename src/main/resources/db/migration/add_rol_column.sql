-- Agregar columna rol a la tabla usuarios
ALTER TABLE usuarios ADD COLUMN rol VARCHAR(20) DEFAULT 'USUARIO';

-- Actualizar usuarios existentes con rol por defecto
UPDATE usuarios SET rol = 'USUARIO' WHERE rol IS NULL;

-- Crear un usuario administrador de ejemplo (opcional)
-- UPDATE usuarios SET rol = 'ADMIN' WHERE correo = 'admin@library.com';
