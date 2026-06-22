package dao;

import database.Conexion;
import model.DevolucionPrenda;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DevolucionPrendaDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DevolucionPrenda devolucion) throws SQLException {
        int id = devolucion.getId() > 0 ? devolucion.getId() : getNextId();
        String sql = "INSERT INTO devolucionprenda (id, folioVenta, idPrenda, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, devolucion.getFolioVenta());
            pstmt.setInt(3, devolucion.getIdPrenda());
            LocalDate fecha = devolucion.getFecha() != null ? devolucion.getFecha() : LocalDate.now();
            pstmt.setDate(4, Date.valueOf(fecha));
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<DevolucionPrenda> getAll() throws SQLException {
        List<DevolucionPrenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM devolucionprenda";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DevolucionPrenda(
                    rs.getInt("id"),
                    rs.getInt("folioVenta"),
                    rs.getInt("idPrenda"),
                    rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null
                ));
            }
        }
        return lista;
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM devolucionprenda";
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
