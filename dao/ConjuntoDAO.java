package dao;

import database.Conexion;
import model.Conjunto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConjuntoDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public List<Conjunto> getAll() throws SQLException {
        List<Conjunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM conjunto";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    public Conjunto getById(int id) throws SQLException {
        String sql = "SELECT * FROM conjunto WHERE id = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    public boolean insert(Conjunto conjunto) throws SQLException {
        String sql = "INSERT INTO conjunto (id, nombre, piezas, precio) VALUES (?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, conjunto.getId());
            pstmt.setString(2, conjunto.getNombre());
            pstmt.setInt(3, conjunto.getPiezas());
            pstmt.setDouble(4, conjunto.getPrecio());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean update(Conjunto conjunto) throws SQLException {
        String sql = "UPDATE conjunto SET nombre = ?, piezas = ?, precio = ? WHERE id = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, conjunto.getNombre());
            pstmt.setInt(2, conjunto.getPiezas());
            pstmt.setDouble(3, conjunto.getPrecio());
            pstmt.setInt(4, conjunto.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM conjunto WHERE id = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM conjunto";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("nextId");
            }
        }
        return 1;
    }

    private Conjunto mapResultSet(ResultSet rs) throws SQLException {
        return new Conjunto(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getInt("piezas"),
            rs.getDouble("precio")
        );
    }
}
