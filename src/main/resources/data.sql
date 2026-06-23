-- Provincias
INSERT IGNORE INTO provincia (id, nombre) VALUES
(1, 'Buenos Aires'),
(2, 'Córdoba'),
(3, 'Santa Fe');

-- Ciudades
INSERT IGNORE INTO ciudad (id, nombre, provincia_id) VALUES
(1, 'La Plata', 1),
(2, 'Mar del Plata', 1),
(3, 'Córdoba', 2),
(4, 'Rosario', 3);

-- Personas (propietarios e inquilinos)
INSERT IGNORE INTO persona (id, nombre, apellido, dni_cuit, telefono, email, domicilio, eliminada) VALUES
(1, 'Carlos', 'Gomez', '25111222', '2214001122', 'cgomez@mail.com', 'Calle 13 N°456, La Plata', false),
(2, 'Maria', 'Lopez', '30222333', '2214003344', 'mlopez@mail.com', 'Av. 7 N°890, La Plata', false),
(3, 'Juan', 'Perez', '27333444', '2234005566', 'jperez@mail.com', 'San Martin 123, Mar del Plata', false),
(4, 'Ana', 'Torres', '33444555', '3514007788', 'atorres@mail.com', 'Colón 500, Córdoba', false);

-- Propiedades (estado DISPONIBLE para poder crear publicaciones)
INSERT IGNORE INTO propiedad (id, direccion, tipo, cantidad_ambientes, metros_cuadrados, descripcion, comodidades, estado_disponibilidad, eliminada, ciudad_id, propietario_id) VALUES
(1, 'Calle 50 N°123', 'DEPARTAMENTO', 3, 75.0, 'Departamento luminoso en planta baja', 'Cochera, balcón, lavadero', 'DISPONIBLE', false, 1, 1),
(2, 'Av. Corrientes 800', 'CASA', 5, 150.0, 'Casa amplia con jardín', 'Jardín, garage doble, quincho', 'DISPONIBLE', false, 1, 2),
(3, 'San Martin 200', 'DEPARTAMENTO', 2, 55.0, 'Monoambiente bien ubicado', 'Ascensor, portero eléctrico', 'DISPONIBLE', false, 2, 3),
(4, 'Belgrano 450', 'LOCAL', 1, 40.0, 'Local comercial en zona céntrica', 'Vidriera, depósito', 'ALQUILADA', false, 3, 4);

-- Publicaciones (variadas para probar los filtros: distintos estados, ciudades y precios)
INSERT IGNORE INTO publicacion (id, precio_mensual, condiciones, fecha_publicacion, estado, eliminada, descripcion, propiedad_id) VALUES
(1, 80000.00, 'Depósito de un mes. Garantía propietaria. Acepta mascotas.', '2026-06-01', 'ACTIVA', false, 'Depto luminoso ideal para familia', 1),
(2, 150000.00, 'Depósito de dos meses. Garantía propietaria. No acepta mascotas.', '2026-06-05', 'PAUSADA', false, 'Casa amplia con jardín y quincho', 2),
(3, 50000.00, 'Depósito de un mes. Sin garantía.', '2026-05-20', 'FINALIZADA', false, 'Monoambiente céntrico', 3),
(4, 95000.00, 'Depósito de un mes. Garantía propietaria.', '2026-06-10', 'FINALIZADA', false, 'Local comercial sobre avenida', 4),
(5, 72000.00, 'Depósito de un mes. Acepta mascotas pequeñas.', '2026-04-15', 'FINALIZADA', false, 'Publicación anterior del mismo depto', 1);

-- Historial de estados de las publicaciones
INSERT IGNORE INTO historial_estado_publicacion (id, estado, fecha_hora, publicacion_id) VALUES
(1, 'ACTIVA', '2026-06-01 10:00:00', 1),
(2, 'PAUSADA', '2026-06-05 12:30:00', 2),
(3, 'FINALIZADA', '2026-05-20 09:15:00', 3),
(4, 'FINALIZADA', '2026-06-10 16:45:00', 4),
(5, 'FINALIZADA', '2026-04-15 11:00:00', 5);
