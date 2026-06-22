package dao;

import database.Conexion;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public List<Usuario> getAll() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("rol")
                ));
            }
        }
        return lista;
    }

    public Usuario getByUsuario(String usuario) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("rol")
                    );
                }
            }
        }
        return null;
    }

    public boolean insert(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, usuario, password, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getUsuario());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setString(4, usuario.getRol());
            return pstmt.executeUpdate() > 0;
        }
    }
}
