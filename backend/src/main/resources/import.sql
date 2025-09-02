-- Archivo para cargar datos iniciales en la base de datos
-- Este archivo es utilizado por ImportSqlDataSeeder al iniciar la aplicación
-- No borrar datos existentes: este script asume base vacía y solo inserta si las tablas están vacías

-- Insertar datos base para pruebas (colegios)
INSERT INTO colegios (id, nombre, ciudad, tipo) VALUES
  (1, 'Técnico Ecuador'  , 'Cuenca'    , 'Fiscal'),
  (2, 'Técnico Ecuador'  , 'Quito'     , 'Fiscal'),
  (3, 'Benigno Malo'     , 'Cuenca'    , 'Fiscal'),
  (4, 'Camilo Gallegos'  , 'Gualaquiza', 'Fiscal'),
  (5, 'Camilo Gallegos'  , 'Quito'     , 'Fiscal'),
  (6, 'Borja'            , 'Cuenca'    , 'Particular'),
  (7, 'Catalinas'        , 'Cuenca'    , 'Particular'),
  (8, 'Técnico Salesiano', 'Cuenca'    , 'Fiscomisional');

-- Insertar datos base para pruebas (carreras)
INSERT INTO carreras (id, nombre, numero_estudiante) VALUES
  (1, 'Medicina', 0),
  (2, 'Ingenieria Civil', 0),
  (3, 'Software', 0),
  (4, 'Administracion de empresas', 0);

