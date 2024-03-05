/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
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

    /**
     * Obtains a new database connection using the configured parameters.
     * <p>
     * This method leverages the DriverManager to create a connection to the database using the URL, username, and password provided.
     * The driver class is automatically loaded, making this method ready to use without additional setup for the driver.
     *
     * @return a Connection object that represents a connection to the database
     * @throws SQLException if a database access error occurs or the URL is null
     */
    public Connection getConnection() throws SQLException {
        // Load the driver class only once
        return DriverManager.getConnection(url, username, password);
    }
}