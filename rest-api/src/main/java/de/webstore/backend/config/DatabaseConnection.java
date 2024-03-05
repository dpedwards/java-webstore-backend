/* 
 * Copyright Davain Pablo Edwards core8@gmx.net.
 * Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en
 */
package de.webstore.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Component for managing database connections.
 * <p>
 * Utilizes configuration properties defined in application.properties to establish connections to the database.
 */
@Component
public class DatabaseConnection {

    // URL of the database, loaded from application.properties
    @Value("${spring.datasource.url}")
    private String url;

    // Username for the database, loaded from application.properties
    @Value("${spring.datasource.username}")
    private String username;

    // Password for the database, loaded from application.properties
    @Value("${spring.datasource.password}")
    private String password;

    // JDBC Driver class name, loaded from application.properties
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * Obtains a new database connection using the configured parameters.
     * <p>
     * This method leverages the DriverManager to create a connection to the database using the URL, username, and password provided.
     * It explicitly loads the driver class to ensure compatibility with older JDBC drivers.
     *
     * @return a Connection object that represents a connection to the database.
     * @throws SQLException if a database access error occurs or the URL is null.
     */
    public Connection getConnection() throws SQLException {
        try {
            // Explicitly load the driver class
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver class not found: " + driverClassName, e);
        }
        return DriverManager.getConnection(url, username, password);
    }
}