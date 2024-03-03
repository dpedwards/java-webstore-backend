-- MySQL: Table for Products
CREATE TABLE product (
    product_number INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    unit VARCHAR(50),
    price DECIMAL(10, 2)
);

-- MySQL: Table for Warehouse
CREATE TABLE warehouse (
    warehouse_number INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT
);

-- MySQL: Table for Orders
CREATE TABLE order_ (
    order_number INT AUTO_INCREMENT PRIMARY KEY,
    date_ DATE
);

-- MySQL: Table for Positions
CREATE TABLE position (
    position_number INT AUTO_INCREMENT PRIMARY KEY,
    product_number INT,
    order_number INT,
    quantity INT,
    FOREIGN KEY (product_number) REFERENCES product(product_number),
    FOREIGN KEY (order_number) REFERENCES order_(order_number)
);

-- Insert test data
-- Products
INSERT INTO product (name, unit, price) VALUES ('Product A', 'Amount', 9.99);
INSERT INTO product (name, unit, price) VALUES ('Product B', 'Kilogram', 19.99);

-- Warehouse
INSERT INTO warehouse (quantity) VALUES (100);
INSERT INTO warehouse (quantity) VALUES (150);

-- Orders
INSERT INTO order_ (date_) VALUES ('2024-02-27');
INSERT INTO order_ (date_) VALUES ('2024-03-01');

-- Positions
INSERT INTO position (product_number, order_number, quantity) VALUES (1, 1, 10);
INSERT INTO position (product_number, order_number, quantity) VALUES (2, 1, 5);
