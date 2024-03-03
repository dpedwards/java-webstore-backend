-- MySQL: Tabelle für Produkte
CREATE TABLE produkt (
    produktnummer INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    einheit VARCHAR(50),
    preis DECIMAL(10, 2)
);

-- MySQL: Tabelle für Lager
CREATE TABLE lager (
    lagernummer INT AUTO_INCREMENT PRIMARY KEY,
    menge INT
);

-- MySQL: Tabelle für Aufträge
CREATE TABLE auftrag (
    auftragsnummer INT AUTO_INCREMENT PRIMARY KEY,
    datum DATE
);

-- MySQL: Tabelle für Positionen
CREATE TABLE position (
    positionsnummer INT AUTO_INCREMENT PRIMARY KEY,
    produktnummer INT,
    auftragsnummer INT,
    menge INT,
    FOREIGN KEY (produktnummer) REFERENCES produkt(produktnummer),
    FOREIGN KEY (auftragsnummer) REFERENCES auftrag(auftragsnummer)
);

-- Hinzufügen von Testdaten
-- Produkte
INSERT INTO produkt (name, einheit, preis) VALUES ('Produkt A', 'Stück', 9.99);
INSERT INTO produkt (name, einheit, preis) VALUES ('Produkt B', 'Kilogramm', 19.99);

-- Lager
INSERT INTO lager (menge) VALUES (100);
INSERT INTO lager (menge) VALUES (150);

-- Aufträge
INSERT INTO auftrag (datum) VALUES ('2024-02-27');
INSERT INTO auftrag (datum) VALUES ('2024-03-01');

-- Positionen
INSERT INTO position (produktnummer, auftragsnummer, menge) VALUES (1, 1, 10);
INSERT INTO position (produktnummer, auftragsnummer, menge) VALUES (2, 1, 5);
