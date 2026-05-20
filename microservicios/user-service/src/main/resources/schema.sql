-- schema.sql: Script para inicializar la base de datos en Supabase con todas las columnas de Usuario

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255), -- Puede ser nulo si inicia sesión con Google
    email VARCHAR(150) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    rol VARCHAR(50) DEFAULT 'USER',
    activo BOOLEAN DEFAULT TRUE,
    tiempo_acumulado BIGINT DEFAULT 0,
    bio VARCHAR(300),
    phone_number VARCHAR(20),
    birth_date DATE,
    grado_academico VARCHAR(50),
    institucion VARCHAR(100),
    ciudad VARCHAR(100),
    profile_image_url VARCHAR(500),
    banner_image_url VARCHAR(500),
    provider VARCHAR(20) DEFAULT 'LOCAL', -- 'LOCAL' o 'GOOGLE'
    provider_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Crear función para actualizar el updated_at automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
   RETURN NEW;
END;
$$ language 'plpgsql';

-- Crear el trigger para la tabla usuarios
DROP TRIGGER IF EXISTS update_usuarios_updated_at ON usuarios;
CREATE TRIGGER update_usuarios_updated_at
BEFORE UPDATE ON usuarios
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Índices adicionales para mejorar las búsquedas
CREATE INDEX IF NOT EXISTS idx_usuarios_username ON usuarios(username);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);

-- Crear tabla de voluntarios (relación OneToOne con usuarios)
CREATE TABLE IF NOT EXISTS voluntarios (
    id BIGINT PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE,
    institution VARCHAR(255) NOT NULL,
    skills VARCHAR(255) NOT NULL,
    materia_especializada VARCHAR(255)
);

