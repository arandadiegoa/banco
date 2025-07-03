-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 03-07-2025 a las 17:56:50
-- Versión del servidor: 8.4.3
-- Versión de PHP: 8.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `banco`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta`
--

CREATE TABLE `cuenta` (
  `id` int NOT NULL,
  `saldo` double NOT NULL,
  `user_id` int DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'activa'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cuenta`
--

INSERT INTO `cuenta` (`id`, `saldo`, `user_id`, `estado`) VALUES
(3, 50000, 3, 'activa'),
(5, 405000, 13, 'activa'),
(6, 440000, 13, 'activa'),
(7, 65000, 4, 'cerrada'),
(10, 60000, 15, 'activa'),
(11, 36000, 7, 'activa'),
(12, 25000, 16, 'activa'),
(13, 3000, 15, 'activa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientos`
--

CREATE TABLE `movimientos` (
  `id` int NOT NULL,
  `cuenta_id` int NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `monto` double NOT NULL,
  `fecha` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `movimientos`
--

INSERT INTO `movimientos` (`id`, `cuenta_id`, `tipo`, `monto`, `fecha`, `descripcion`) VALUES
(1, 12, 'Deposito', 35000, '2025-07-03 00:49:06', 'Deposito realizado en cuenta'),
(2, 12, 'Retiro', 10000, '2025-07-03 00:51:22', 'Retiro realizado en cuenta'),
(3, 3, 'Deposito', 10000, '2025-07-03 12:21:39', 'Deposito realizado en cuenta'),
(4, 3, 'Retiro', 50000, '2025-07-03 12:21:51', 'Retiro realizado en cuenta'),
(5, 13, 'TRANSFERENCIA_ENVIADA', 25000, '2025-07-03 16:18:54', 'Transferencia a cuenta 10'),
(6, 13, 'TRANSFERENCIA_ENVIADA', 20000, '2025-07-03 16:20:23', 'Transferencia a cuenta 10'),
(7, 13, 'TRANSFERENCIA_ENVIADA', 20000, '2025-07-03 16:21:58', 'Transferencia a cuenta 10'),
(8, 13, 'TRANSFERENCIA_ENVIADA', 20000, '2025-07-03 16:27:05', 'Transferencia a cuenta 10'),
(9, 10, 'TRANSFERENCIA_ENVIADA', 20000, '2025-07-03 16:27:05', 'Transferencia a cuenta 13'),
(10, 10, 'Retiro', 5000, '2025-07-03 16:27:27', 'Retiro realizado en cuenta'),
(11, 13, 'TRANSFERENCIA_ENVIADA', 10000, '2025-07-03 16:33:45', 'Transferencia a cuenta 10'),
(12, 10, 'TRANSFERENCIA_ENVIADA', 10000, '2025-07-03 16:33:45', 'Transferencia a cuenta 13');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL DEFAULT 'cliente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `pass`, `rol`) VALUES
(3, 'Prueba', 'prueba@prueba.com', '1234', 'cliente'),
(4, 'juan', 'juan@juan.com', '12345', 'cliente'),
(5, 'pedro', 'pedro@pedro.com', '123456', 'cliente'),
(6, 'Diego', 'diego@cuervo1.com', 'dieCuervo1', 'cliente'),
(7, 'Juan p', 'juan2@juan2.com', '12345', 'cliente'),
(8, 'Preba 2', 'prueba2@prueba.com', '1234', 'cliente'),
(9, 'Diego Casla', 'casla@sl.com', '1234', 'cliente'),
(10, 'Prueba3', 'prueba3@gmail.com', '12345', 'cliente'),
(11, 'prueba4', 'prueba4@prueba.com', '12345', 'cliente'),
(12, 'Prueba5', 'prueba5@gmail.com', '1234', 'cliente'),
(13, 'Cuervo', 'elcuervo@gmail.com', '1234', 'empleado'),
(15, 'Vicentico', 'elConde@conde.com', '1234', 'cliente'),
(16, 'El peppppppp', 'peeeeeeeeee@peee.com', '12345', 'cliente');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cuenta`
--
ALTER TABLE `cuenta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user` (`user_id`);

--
-- Indices de la tabla `movimientos`
--
ALTER TABLE `movimientos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cuenta_id` (`cuenta_id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cuenta`
--
ALTER TABLE `cuenta`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `movimientos`
--
ALTER TABLE `movimientos`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cuenta`
--
ALTER TABLE `cuenta`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `movimientos`
--
ALTER TABLE `movimientos`
  ADD CONSTRAINT `movimientos_ibfk_1` FOREIGN KEY (`cuenta_id`) REFERENCES `cuenta` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
