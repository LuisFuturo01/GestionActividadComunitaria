-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-07-2025 a las 06:52:35
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `actividadescomunitarias_25`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `actividadescomunitarias`
--

CREATE TABLE `actividadescomunitarias` (
  `id_actividad` int(11) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `cupo_maximo` int(11) NOT NULL,
  `lugar` varchar(255) DEFAULT NULL,
  `id_organizador` varchar(20) DEFAULT NULL,
  `tipo_actividad` varchar(50) NOT NULL,
  `num_sesiones` int(11) DEFAULT NULL,
  `es_abierto_al_publico` tinyint(1) DEFAULT NULL,
  `requiere_materiales` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `actividadescomunitarias`
--

INSERT INTO `actividadescomunitarias` (`id_actividad`, `titulo`, `descripcion`, `fecha_inicio`, `fecha_fin`, `cupo_maximo`, `lugar`, `id_organizador`, `tipo_actividad`, `num_sesiones`, `es_abierto_al_publico`, `requiere_materiales`) VALUES
(101, 'Curso de Introduccion a la Programacion', 'Un curso basico para aprender los fundamentos de la programacion.', '2025-08-01', '2025-08-15', 20, 'Laboratorio de Computacion A', 'ORG-001', 'curso', 10, NULL, NULL),
(102, 'Feria de Salud Comunitaria', 'Evento de un dia con chequeos medicos gratuitos y charlas de bienestar.', '2025-09-10', '2025-09-10', 100, 'Plaza Central', 'ORG-002', 'evento', NULL, 1, NULL),
(103, 'Taller de Reciclaje Creativo', 'Aprende a transformar materiales reciclables en objetos de arte.', '2025-10-05', '2025-10-05', 15, 'Centro Comunitario', 'ORG-001', 'taller', NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inscripciones`
--

CREATE TABLE `inscripciones` (
  `id_inscripcion` varchar(20) NOT NULL,
  `id_participante` varchar(20) NOT NULL,
  `id_actividad` int(11) NOT NULL,
  `fecha_inscripcion` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `inscripciones`
--

INSERT INTO `inscripciones` (`id_inscripcion`, `id_participante`, `id_actividad`, `fecha_inscripcion`) VALUES
('INS-001', 'PART-002', 103, '2025-07-23 00:44:38'),
('INS-002', 'PART-002', 102, '2025-07-21 14:30:00'),
('INS-003', 'PART-001', 103, '2025-07-22 09:15:00'),
('INS-004', 'PART-002', 101, '2025-07-23 00:50:23');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personas`
--

CREATE TABLE `personas` (
  `id_persona` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `tipo_persona` varchar(20) NOT NULL,
  `edad` int(11) DEFAULT NULL,
  `genero` varchar(50) DEFAULT NULL,
  `institucion` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `personas`
--

INSERT INTO `personas` (`id_persona`, `nombre`, `email`, `tipo_persona`, `edad`, `genero`, `institucion`) VALUES
('ORG-001', 'Maria Garcia', 'maria.garcia@example.com', 'organizador', NULL, NULL, 'Universidad Nacional'),
('ORG-002', 'Juan Perez', 'juan.perez@example.com', 'organizador', NULL, NULL, 'ONG Ayuda Comunitaria'),
('PART-001', 'Ana Lopez', 'ana.lopez@example.com', 'participante', 25, 'Femenino', NULL),
('PART-002', 'Carlos Sanchez', 'carlos.sanchez@example.com', 'participante', 30, 'Masculino', NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `actividadescomunitarias`
--
ALTER TABLE `actividadescomunitarias`
  ADD PRIMARY KEY (`id_actividad`);

--
-- Indices de la tabla `inscripciones`
--
ALTER TABLE `inscripciones`
  ADD PRIMARY KEY (`id_inscripcion`);

--
-- Indices de la tabla `personas`
--
ALTER TABLE `personas`
  ADD PRIMARY KEY (`id_persona`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
