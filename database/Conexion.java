package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Cambia estos datos según tu configuración
    private static final String URL = "jdbc:mysql://localhost:3306/textilera?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "&5Dejulio";
    
    private static Connection connection = null;
    
    private Conexion() {} // Constructor privado
    
    public static synchronized Connection getConnection() {
        try {
            // Reabre si no existe, está cerrada o se volvió inválida.
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                // Registrar el driver (opcional en versiones modernas)
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a MySQL");
            }
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalStateException("No se pudo obtener la conexión a MySQL", e);
        }
    }
    
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                System.out.println("🔒 Conexión cerrada");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }
    
    // ✅ Método para verificar si la conexión está activa
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}