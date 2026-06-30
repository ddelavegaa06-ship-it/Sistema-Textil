package dao;

import database.Conexion;
import model.PrendaConjunto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrendaConjuntoDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public List<PrendaConjunto> getByConjunto(int idConjunto) throws SQLException {
        List<PrendaConjunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM prendaconjunto WHERE idConjunto = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idConjunto);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        }
        return lista;
    }

    public boolean insert(PrendaConjunto pc) throws SQLException {
        String sql = "INSERT INTO prendaconjunto (id, idPrenda, idConjunto) VALUES (?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int id = pc.getId();
            if (id <= 0) {
                id = getNextId();
            }
            pstmt.setInt(1, id);
            pstmt.setInt(2, pc.getIdPrenda());
            pstmt.setInt(3, pc.getIdConjunto());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM prendaconjunto WHERE id = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteByConjunto(int idConjunto) throws SQLException {
        String sql = "DELETE FROM prendaconjunto WHERE idConjunto = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idConjunto);
            return pstmt.executeUpdate() > 0;
        }
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM prendaconjunto";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("nextId");
            }
        }
        return 1;
    }

    private PrendaConjunto mapResultSet(ResultSet rs) throws SQLException {
        return new PrendaConjunto(
            rs.getInt("id"),
            rs.getInt("idPrenda"),
            rs.getInt("idConjunto")
        );
    }
}
