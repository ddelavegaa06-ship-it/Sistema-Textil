package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Cambia estos datos según tu configuración
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "&5Dejulio";
    
    private static Connection connection = null;
    
    private Conexion() {} // Constructor privado
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Registrar el driver (opcional en versiones modernas)
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a MySQL");
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("❌ Error de conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔒 Conexión cerrada");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
