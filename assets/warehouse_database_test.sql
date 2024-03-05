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

-- Dumping data for table warehouse_database_uuid_de.auftrag: ~2 rows (approximately)
INSERT INTO `auftrag` (`auftragsnummer`, `datum`, `status`) VALUES
	('8f70a526-f694-4ddc-a929-4572a64311ee', '2024-03-05', 'offen'),
	('e738cce7-e4db-4cec-9fa8-9d60002b7d81', '2024-03-05', 'offen');

-- Dumping data for table warehouse_database_uuid_de.lager: ~3 rows (approximately)
INSERT INTO `lager` (`lagernummer`, `menge`, `aktiv`) VALUES
	(1, 434, 1),
	(2, 19779, 1),
	(3, 890, 1);

-- Dumping data for table warehouse_database_uuid_de.lagert: ~3 rows (approximately)
INSERT INTO `lagert` (`produkt_fk`, `lager_fk`) VALUES
	('a1c4da13-047e-4f5d-a505-5b2204ebf87e', 1),
	('c9541966-eb9b-42fe-b782-74840b724a56', 3),
	('f19f4ae4-eccc-4ffb-973b-de91cb085e16', 2);

-- Dumping data for table warehouse_database_uuid_de.position: ~0 rows (approximately)

-- Dumping data for table warehouse_database_uuid_de.produkt: ~3 rows (approximately)
INSERT INTO `produkt` (`produktnummer`, `name`, `einheit`, `preis`) VALUES
	('a1c4da13-047e-4f5d-a505-5b2204ebf87e', 'Bubblegum', 'Paper', 0.80),
	('c9541966-eb9b-42fe-b782-74840b724a56', 'Fish', 'Tin', 11.00),
	('f19f4ae4-eccc-4ffb-973b-de91cb085e16', 'Pizza', 'Package', 3.78);

-- Dumping data for table warehouse_database_uuid_de.produktlagermenge: ~3 rows (approximately)
INSERT INTO `produktlagermenge` (`produkt_fk`, `lager_fk`, `menge`) VALUES
	('a1c4da13-047e-4f5d-a505-5b2204ebf87e', 1, 434),
	('c9541966-eb9b-42fe-b782-74840b724a56', 3, 890),
	('f19f4ae4-eccc-4ffb-973b-de91cb085e16', 2, 19779);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
