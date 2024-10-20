package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión con la base de datos
 */
public class JdbcUtils {
    private static final String url = "jdbc:mysql://localhost:3307/Reto Conjunto";
    private static final String user = "root";
    private static final String password = "pwd";

    /**
     * Permite obtener una nueva conexión cada vez que se llame
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
