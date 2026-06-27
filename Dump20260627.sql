-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: textilera
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `conjunto`
--

DROP TABLE IF EXISTS `conjunto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conjunto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `piezas` int NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conjunto`
--

LOCK TABLES `conjunto` WRITE;
/*!40000 ALTER TABLE `conjunto` DISABLE KEYS */;
INSERT INTO `conjunto` VALUES (1,'uniforme escolar',2,135.00);
/*!40000 ALTER TABLE `conjunto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalleventaconjunto`
--

DROP TABLE IF EXISTS `detalleventaconjunto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalleventaconjunto` (
  `folioVenta` int NOT NULL,
  `idConjunto` int NOT NULL,
  `cantidad` int DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`folioVenta`,`idConjunto`),
  KEY `conjDetaVen_idx` (`idConjunto`),
  CONSTRAINT `conjDetaVen` FOREIGN KEY (`idConjunto`) REFERENCES `conjunto` (`id`),
  CONSTRAINT `detaVenC` FOREIGN KEY (`folioVenta`) REFERENCES `venta` (`folio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalleventaconjunto`
--

LOCK TABLES `detalleventaconjunto` WRITE;
/*!40000 ALTER TABLE `detalleventaconjunto` DISABLE KEYS */;
INSERT INTO `detalleventaconjunto` VALUES (2,1,2,384.00);
/*!40000 ALTER TABLE `detalleventaconjunto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalleventaprenda`
--

DROP TABLE IF EXISTS `detalleventaprenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalleventaprenda` (
  `folio` int NOT NULL,
  `idPrenda` int NOT NULL,
  `cantidad` int DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`folio`,`idPrenda`),
  KEY `prendaDetalle_idx` (`idPrenda`),
  CONSTRAINT `detaVenP` FOREIGN KEY (`folio`) REFERENCES `venta` (`folio`),
  CONSTRAINT `prendaDetalle` FOREIGN KEY (`idPrenda`) REFERENCES `prenda` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalleventaprenda`
--

LOCK TABLES `detalleventaprenda` WRITE;
/*!40000 ALTER TABLE `detalleventaprenda` DISABLE KEYS */;
INSERT INTO `detalleventaprenda` VALUES (1,5,1,180.00);
/*!40000 ALTER TABLE `detalleventaprenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devolucionconjunto`
--

DROP TABLE IF EXISTS `devolucionconjunto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `devolucionconjunto` (
  `folio` int NOT NULL,
  `idConjunto` int NOT NULL,
  `fecha` date DEFAULT (curdate()),
  PRIMARY KEY (`folio`,`idConjunto`),
  KEY `ventaDetalleCon_idx` (`folio`),
  KEY `conjuntoDevConj_idx` (`idConjunto`),
  CONSTRAINT `conjuntoDevConj` FOREIGN KEY (`idConjunto`) REFERENCES `conjunto` (`id`),
  CONSTRAINT `devoC` FOREIGN KEY (`folio`) REFERENCES `venta` (`folio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devolucionconjunto`
--

LOCK TABLES `devolucionconjunto` WRITE;
/*!40000 ALTER TABLE `devolucionconjunto` DISABLE KEYS */;
/*!40000 ALTER TABLE `devolucionconjunto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devolucionprenda`
--

DROP TABLE IF EXISTS `devolucionprenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `devolucionprenda` (
  `folioVenta` int NOT NULL,
  `idPrenda` int NOT NULL,
  `fecha` date DEFAULT (curdate()),
  PRIMARY KEY (`folioVenta`,`idPrenda`),
  KEY `ventaDevolucion_idx` (`folioVenta`),
  KEY `prendaDevolucion_idx` (`idPrenda`),
  CONSTRAINT `devoP` FOREIGN KEY (`folioVenta`) REFERENCES `venta` (`folio`),
  CONSTRAINT `prendaDevolucion` FOREIGN KEY (`idPrenda`) REFERENCES `prenda` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devolucionprenda`
--

LOCK TABLES `devolucionprenda` WRITE;
/*!40000 ALTER TABLE `devolucionprenda` DISABLE KEYS */;
INSERT INTO `devolucionprenda` VALUES (1,5,'2026-06-27');
/*!40000 ALTER TABLE `devolucionprenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insumo`
--

DROP TABLE IF EXISTS `insumo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insumo` (
  `id` varchar(45) NOT NULL,
  `numeroPartida` varchar(45) DEFAULT NULL,
  `existencia` decimal(10,2) NOT NULL,
  `tipoExistencia` varchar(45) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `color` varchar(45) DEFAULT NULL,
  `medida` decimal(10,2) DEFAULT NULL,
  `ancho` decimal(10,2) DEFAULT NULL,
  `composicion` varchar(45) DEFAULT NULL,
  `tipo` varchar(45) DEFAULT NULL,
  `no.` int DEFAULT NULL,
  `tamanio` varchar(45) DEFAULT NULL,
  `talla` decimal(10,2) DEFAULT NULL,
  `material` varchar(45) DEFAULT NULL,
  `tipoInsumo` varchar(45) NOT NULL,
  `idUbicacion` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `numeroPartida_UNIQUE` (`numeroPartida`),
  KEY `ubicacionInsumo_idx` (`idUbicacion`),
  CONSTRAINT `ubicacionInsumo` FOREIGN KEY (`idUbicacion`) REFERENCES `ubicacion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insumo`
--

LOCK TABLES `insumo` WRITE;
/*!40000 ALTER TABLE `insumo` DISABLE KEYS */;
INSERT INTO `insumo` VALUES ('I-1782242507429','bot_1521',9.00,'pieza','','boton','',0.00,0.00,'','',0,'',0.00,'','Boton',1),('I-1782406540069','bot-12',11.00,'metro','','cierre',NULL,0.00,0.00,NULL,NULL,0,NULL,0.00,NULL,'Cierre',1),('I-1782408625774','cie-1234',10.00,'pieza','','cierre','',0.00,0.00,'','',0,'12',0.00,'','Boton',1);
/*!40000 ALTER TABLE `insumo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insumoprenda`
--

DROP TABLE IF EXISTS `insumoprenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insumoprenda` (
  `folio` int NOT NULL AUTO_INCREMENT,
  `idInsumo` varchar(45) DEFAULT NULL,
  `idPrenda` int DEFAULT NULL,
  `fecha` date DEFAULT (curdate()),
  `cantidadInsumo` decimal(10,2) NOT NULL,
  PRIMARY KEY (`folio`),
  KEY `insumoIP_idx` (`idInsumo`),
  KEY `prendaIP_idx` (`idPrenda`),
  CONSTRAINT `fk_insumoIP` FOREIGN KEY (`idInsumo`) REFERENCES `insumo` (`id`),
  CONSTRAINT `prendaIP` FOREIGN KEY (`idPrenda`) REFERENCES `prenda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insumoprenda`
--

LOCK TABLES `insumoprenda` WRITE;
/*!40000 ALTER TABLE `insumoprenda` DISABLE KEYS */;
INSERT INTO `insumoprenda` VALUES (1,'I-1782242507429',5,'2026-06-25',3.00),(2,'I-1782242507429',7,'2026-06-27',2.00),(3,'I-1782408625774',7,'2026-06-27',2.00),(4,'I-1782406540069',5,'2026-06-27',1.00);
/*!40000 ALTER TABLE `insumoprenda` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `decrementoInsumo` AFTER INSERT ON `insumoprenda` FOR EACH ROW begin
	update insumo set existencia = existencia-new.cantidadInsumo
    where id = new.idInsumo;
  end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `prenda`
--

DROP TABLE IF EXISTS `prenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenda` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `talla` varchar(45) NOT NULL,
  `existencia` int NOT NULL,
  `precioMayoreo` decimal(10,2) NOT NULL,
  `precioMenudeo` decimal(10,2) NOT NULL,
  `idTienda` int DEFAULT NULL,
  `codigoBarras` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`,`talla`),
  UNIQUE KEY `codigoBarras` (`codigoBarras`),
  KEY `tiendaPren_idx` (`idTienda`),
  CONSTRAINT `prendaTienda` FOREIGN KEY (`idTienda`) REFERENCES `tienda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenda`
--

LOCK TABLES `prenda` WRITE;
/*!40000 ALTER TABLE `prenda` DISABLE KEYS */;
INSERT INTO `prenda` VALUES (5,'pantalon','M',14,123.00,180.00,1,''),(7,'pantalon','Ch',11,12.00,12.00,1,'3');
/*!40000 ALTER TABLE `prenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prendaconjunto`
--

DROP TABLE IF EXISTS `prendaconjunto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prendaconjunto` (
  `id` int NOT NULL,
  `idPrenda` int DEFAULT NULL,
  `idConjunto` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prendaPC_idx` (`idPrenda`),
  KEY `conjuntoPC_idx` (`idConjunto`),
  CONSTRAINT `conjuntoPC` FOREIGN KEY (`idConjunto`) REFERENCES `conjunto` (`id`),
  CONSTRAINT `prendaPC` FOREIGN KEY (`idPrenda`) REFERENCES `prenda` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prendaconjunto`
--

LOCK TABLES `prendaconjunto` WRITE;
/*!40000 ALTER TABLE `prendaconjunto` DISABLE KEYS */;
INSERT INTO `prendaconjunto` VALUES (1,5,1),(2,7,1);
/*!40000 ALTER TABLE `prendaconjunto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tienda`
--

DROP TABLE IF EXISTS `tienda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tienda` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `idPadre` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tiendaPadre_idx` (`idPadre`),
  CONSTRAINT `tiendaPadre` FOREIGN KEY (`idPadre`) REFERENCES `tienda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tienda`
--

LOCK TABLES `tienda` WRITE;
/*!40000 ALTER TABLE `tienda` DISABLE KEYS */;
INSERT INTO `tienda` VALUES (1,'rack','rack 1',NULL);
/*!40000 ALTER TABLE `tienda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ubicacion`
--

DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ubicacion` (
  `id` int NOT NULL,
  `tipo` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `idPadre` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `padreUbicacion_idx` (`idPadre`),
  CONSTRAINT `padreUbicacion` FOREIGN KEY (`idPadre`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ubicacion`
--

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` VALUES (1,'pasillo','pasillo 1',NULL);
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('administrador','encargado','vendedor') NOT NULL DEFAULT 'encargado',
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT '1',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Administrador Principal','admin','1234','administrador','admin@sistema.com',NULL,1,'2026-06-23 00:53:02','2026-06-23 00:53:02'),(2,'Encargado de Tienda','encargado','5678','encargado','encargado@tienda.com',NULL,1,'2026-06-23 00:53:02','2026-06-23 00:53:02');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venta`
--

DROP TABLE IF EXISTS `venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venta` (
  `folio` int NOT NULL AUTO_INCREMENT,
  `fecha` date DEFAULT (curdate()),
  PRIMARY KEY (`folio`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
INSERT INTO `venta` VALUES (1,'2026-06-25'),(2,'2026-06-27');
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-27 11:00:33
