package dao;

import database.Conexion;
import model.DevolucionConjunto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DevolucionConjuntoDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DevolucionConjunto devolucion) throws SQLException {
        int id = devolucion.getId() > 0 ? devolucion.getId() : getNextId();
        String sql = "INSERT INTO devolucionconjunto (id, folio, idConjunto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, devolucion.getFolio());
            pstmt.setInt(3, devolucion.getIdConjunto());
            LocalDate fecha = devolucion.getFecha() != null ? devolucion.getFecha() : LocalDate.now();
            pstmt.setDate(4, Date.valueOf(fecha));
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<DevolucionConjunto> getAll() throws SQLException {
        List<DevolucionConjunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM devolucionconjunto";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DevolucionConjunto(
                    rs.getInt("id"),
                    rs.getInt("folio"),
                    rs.getInt("idConjunto"),
                    rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null
                ));
            }
        }
        return lista;
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM devolucionconjunto";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("nextId");
            }
        }
        return 1;
    }
}
