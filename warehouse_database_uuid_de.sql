-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.10.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for warehouse_database_uuid_de
CREATE DATABASE IF NOT EXISTS `warehouse_database_uuid_de` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `warehouse_database_uuid_de`;

-- Dumping structure for table warehouse_database_uuid_de.auftrag
CREATE TABLE IF NOT EXISTS `auftrag` (
  `auftragsnummer` varchar(36) NOT NULL,
  `datum` date NOT NULL,
  `status` enum('offen','geschlossen') NOT NULL DEFAULT 'offen',
  PRIMARY KEY (`auftragsnummer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table warehouse_database_uuid_de.auftrag: ~2 rows (approximately)
INSERT INTO `auftrag` (`auftragsnummer`, `datum`, `status`) VALUES
	('822c4883-362a-49a1-bf43-10770f91ea7a', '2024-03-08', 'geschlossen'),
	('c4b4d8dc-926c-444c-9456-2d2f18fa6e40', '2024-03-08', 'offen');

-- Dumping structure for table warehouse_database_uuid_de.lager
CREATE TABLE IF NOT EXISTS `lager` (
  `lagernummer` int(11) NOT NULL AUTO_INCREMENT,
  `menge` int(11) NOT NULL DEFAULT 0,
  `aktiv` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`lagernummer`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table warehouse_database_uuid_de.lager: ~3 rows (approximately)
INSERT INTO `lager` (`lagernummer`, `menge`, `aktiv`) VALUES
	(1, 1281, 1),
	(2, 500, 1),
	(3, 61, 1);

-- Dumping structure for table warehouse_database_uuid_de.lagert
CREATE TABLE IF NOT EXISTS `lagert` (
  `produkt_fk` varchar(36) NOT NULL,
  `lager_fk` int(11) NOT NULL,
  PRIMARY KEY (`produkt_fk`,`lager_fk`),
  KEY `lager_fk` (`lager_fk`),
  CONSTRAINT `lagert_ibfk_1` FOREIGN KEY (`produkt_fk`) REFERENCES `produkt` (`produktnummer`),
  CONSTRAINT `lagert_ibfk_2` FOREIGN KEY (`lager_fk`) REFERENCES `lager` (`lagernummer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table warehouse_database_uuid_de.lagert: ~4 rows (approximately)
INSERT INTO `lagert` (`produkt_fk`, `lager_fk`) VALUES
	('797d760b-bd7d-4444-bbf4-1c97e1d737e4', 1),
	('797d760b-bd7d-4444-bbf4-1c97e1d737e4', 2),
	('a1c4da13-047e-4f5d-a505-5b2204ebf87e', 3),
	('c9541966-eb9b-42fe-b782-74840b724a56', 1);

-- Dumping structure for table warehouse_database_uuid_de.position
CREATE TABLE IF NOT EXISTS `position` (
  `positionsnummer` varchar(36) NOT NULL,
  `produktnummer` varchar(36) NOT NULL,
  `auftragsnummer` varchar(36) NOT NULL,
  `menge` int(11) NOT NULL,
  PRIMARY KEY (`positionsnummer`),
  KEY `produktnummer` (`produktnummer`),
  KEY `auftragsnummer` (`auftragsnummer`),
  CONSTRAINT `position_ibfk_1` FOREIGN KEY (`produktnummer`) REFERENCES `produkt` (`produktnummer`),
  CONSTRAINT `position_ibfk_2` FOREIGN KEY (`auftragsnummer`) REFERENCES `auftrag` (`auftragsnummer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table warehouse_database_uuid_de.position: ~3 rows (approximately)
INSERT INTO `position` (`positionsnummer`, `produktnummer`, `auftragsnummer`, `menge`) VALUES
	('187bf419-3ebe-40f4-8ac5-3638ac0a1e61', 'a1c4da13-047e-4f5d-a505-5b2204ebf87e', 'c4b4d8dc-926c-444c-9456-2d2f18fa6e40', 100),
	('52d08e16-9e28-416e-89ed-e3c0ab37144b', 'a1c4da13-047e-4f5d-a505-5b2204ebf87e', '822c4883-362a-49a1-bf43-10770f91ea7a', 89),
	('8b2eb6fb-c1b3-4363-a07f-d87089fd8416', 'c9541966-eb9b-42fe-b782-74840b724a56', 'c4b4d8dc-926c-444c-9456-2d2f18fa6e40', 29);

-- Dumping structure for table warehouse_database_uuid_de.produkt
CREATE TABLE IF NOT EXISTS `produkt` (
  `produktnummer` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `einheit` varchar(50) NOT NULL,
  `preis` decimal(10,2) NOT NULL,
  PRIMARY KEY (`produktnummer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table warehouse_database_uuid_de.produkt: ~3 rows (approximately)
INSERT INTO `produkt` (`produktnummer`, `name`, `einheit`, `preis`) VALUES
	('797d760b-bd7d-4444-bbf4-1c97e1d737e4', 'Eis', 'Package', 1.99),
	('a1c4da13-047e-4f5d-a505-5b2204ebf87e', 'Bubblegum', 'Paper', 0.80),
	('c9541966-eb9b-42fe-b782-74840b724a56', 'Fish', 'Tin', 11.00);

-- Dumping structure for table warehouse_database_uuid_de.produktlagermenge
CREATE TABLE IF NOT EXISTS `produktlagermenge` (
  `produkt_fk` varchar(36) NOT NULL,
  `lager_fk` int(11) NOT NULL,
  `menge` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`produkt_fk`,`lager_fk`),
  KEY `lager_fk` (`lager_fk`),
  CONSTRAINT `produktlagermenge_ibfk_1` FOREIGN KEY (`produkt_fk`) REFERENCES `produkt` (`produktnummer`),
  CONSTRAINT `produktlagermenge_ibfk_2` FOREIGN KEY (`lager_fk`) REFERENCES `lager` (`lagernummer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table warehouse_database_uuid_de.produktlagermenge: ~4 rows (approximately)
INSERT INTO `produktlagermenge` (`produkt_fk`, `lager_fk`, `menge`) VALUES
	('797d760b-bd7d-4444-bbf4-1c97e1d737e4', 1, 899),
	('797d760b-bd7d-4444-bbf4-1c97e1d737e4', 2, 500),
	('a1c4da13-047e-4f5d-a505-5b2204ebf87e', 3, 61),
	('c9541966-eb9b-42fe-b782-74840b724a56', 1, 382);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
