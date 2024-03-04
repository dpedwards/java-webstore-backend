-- MySQL: Tabelle für Produkte
CREATE TABLE produkt (
    produktnummer INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    einheit VARCHAR(50) NOT NULL,
    preis DECIMAL(10, 2) NOT NULL
);

-- MySQL: Tabelle für Lager
CREATE TABLE lager (
    lagernummer INT AUTO_INCREMENT PRIMARY KEY,
    menge INT NOT NULL DEFAULT 0
);

-- MySQL: Tabelle für Aufträge
CREATE TABLE auftrag (
    auftragsnummer INT AUTO_INCREMENT PRIMARY KEY,
    datum DATE NOT NULL,
    status ENUM('offen', 'geschlossen') NOT NULL DEFAULT 'offen'
);

-- MySQL: Tabelle für Positionen
CREATE TABLE position (
    positionsnummer INT AUTO_INCREMENT PRIMARY KEY,
    produktnummer INT NOT NULL,
    auftragsnummer INT NOT NULL,
    menge INT NOT NULL,
    FOREIGN KEY (produktnummer) REFERENCES produkt(produktnummer),
    FOREIGN KEY (auftragsnummer) REFERENCES auftrag(auftragsnummer)
);

-- MySQL: Tabelle für die Zuordnung von Produkten zu Lagern (lagert)
CREATE TABLE lagert (
    produkt_fk INT NOT NULL,
    lager_fk INT NOT NULL,
    FOREIGN KEY (produkt_fk) REFERENCES produkt(produktnummer),
    FOREIGN KEY (lager_fk) REFERENCES lager(lagernummer),
    PRIMARY KEY (produkt_fk, lager_fk)
);

-- Optional: Tabelle für Produktlagermengen, falls benötigt
-- Dies ist nützlich, wenn Sie die Lagermengen für jedes Produkt einzeln verfolgen möchten.
CREATE TABLE produktlagermenge (
    produkt_fk INT NOT NULL,
    lager_fk INT NOT NULL,
    menge INT NOT NULL DEFAULT 0,
    FOREIGN KEY (produkt_fk) REFERENCES produkt(produktnummer),
    FOREIGN KEY (lager_fk) REFERENCES lager(lagernummer),
    PRIMARY KEY (produkt_fk, lager_fk)
);

-- Einfügen von Testdaten für Produkte
INSERT INTO produkt (name, einheit, preis) VALUES 
('Produkt 1', 'Stück', 10.00),
('Produkt 2', 'Kilogramm', 20.00),
('Produkt 3', 'Liter', 30.00),
('Produkt 4', 'Packung', 40.00),
('Produkt 5', 'Flasche', 50.00),
('Produkt 6', 'Dose', 60.00),
('Produkt 7', 'Meter', 70.00),
('Produkt 8', 'Box', 80.00),
('Produkt 9', 'Beutel', 90.00),
('Produkt 10', 'Stange', 100.00);

-- Einfügen von Testdaten für Lager
INSERT INTO lager (menge) VALUES 
(100),
(200),
(300),
(400),
(500);

-- Einfügen von Testdaten für Aufträge
INSERT INTO auftrag (datum, status) VALUES 
('2024-03-01', 'offen'),
('2024-03-02', 'offen'),
('2024-03-03', 'geschlossen'),
('2024-03-04', 'offen');

-- Angenommen, die Auftragsnummern werden automatisch generiert und starten bei 1,
-- und die Produktnummern starten ebenfalls bei 1.
-- Einfügen von Testdaten für Auftragspositionen
INSERT INTO position (produktnummer, auftragsnummer, menge) VALUES 
(1, 1, 10),
(2, 1, 15),
(3, 2, 5),
(4, 2, 10),
(5, 3, 8),
(6, 3, 16),
(7, 4, 4),
(8, 4, 12),
(9, 4, 20),
(10, 4, 24);

-- Einfügen von Testdaten für die Zuordnung von Produkten zu Lagern
INSERT INTO lagert (produkt_fk, lager_fk) VALUES 
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 1),
(7, 2),
(8, 3),
(9, 4),
(10, 5);

-- Optional: Einfügen von Testdaten für Produktlagermengen
INSERT INTO produktlagermenge (produkt_fk, lager_fk, menge) VALUES 
(1, 1, 50),
(2, 2, 60),
(3, 3, 70),
(4, 4, 80),
(5, 5, 90),
(6, 1, 100),
(7, 2, 110),
(8, 3, 120),
(9, 4, 130),
(10, 5, 140);
