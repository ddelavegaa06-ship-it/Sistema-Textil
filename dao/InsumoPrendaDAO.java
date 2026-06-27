package dao;

import database.Conexion;
import model.InsumoPrenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoPrendaDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public List<InsumoPrenda> getAll() throws SQLException {
        List<InsumoPrenda> lista = new ArrayList<>();
        String sql = "SELECT folio, idInsumo, idPrenda, fecha, cantidadInsumo FROM insumoprenda ORDER BY folio";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    public InsumoPrenda getById(int folio) throws SQLException {
        String sql = "SELECT folio, idInsumo, idPrenda, fecha, cantidadInsumo FROM insumoprenda WHERE folio = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<InsumoPrenda> getByPrenda(int idPrenda) throws SQLException {
        List<InsumoPrenda> lista = new ArrayList<>();
        String sql = "SELECT folio, idInsumo, idPrenda, fecha, cantidadInsumo FROM insumoprenda WHERE idPrenda = ? ORDER BY folio";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPrenda);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        }
        return lista;
    }

    public boolean insert(InsumoPrenda ip) throws SQLException {
        String sql = "INSERT INTO insumoprenda (idInsumo, idPrenda, cantidadInsumo) VALUES (?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ip.getIdInsumo());
            pstmt.setInt(2, ip.getIdPrenda());
            pstmt.setDouble(3, ip.getCantidadInsumo());
            return pstmt.executeUpdate() > 0;
        }
    }

    public int insertAll(List<InsumoPrenda> lista) throws SQLException {
        int insertados = 0;
        for (InsumoPrenda ip : lista) {
            if (insert(ip)) {
                insertados++;
            }
        }
        return insertados;
    }

    public boolean update(InsumoPrenda ip) throws SQLException {
        String sql = "UPDATE insumoprenda SET idInsumo = ?, idPrenda = ?, cantidadInsumo = ?, fecha = ? WHERE folio = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ip.getIdInsumo());
            pstmt.setInt(2, ip.getIdPrenda());
            pstmt.setDouble(3, ip.getCantidadInsumo());
            if (ip.getFecha() != null) {
                pstmt.setDate(4, Date.valueOf(ip.getFecha()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            pstmt.setInt(5, ip.getFolio());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int folio) throws SQLException {
        String sql = "DELETE FROM insumoprenda WHERE folio = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            return pstmt.executeUpdate() > 0;
        }
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(folio), 0) + 1 AS nextId FROM insumoprenda";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("nextId");
            }
        }
        return 1;
    }

    private InsumoPrenda mapResultSet(ResultSet rs) throws SQLException {
        return new InsumoPrenda(
            rs.getInt("folio"),
            rs.getString("idInsumo"),
            rs.getInt("idPrenda"),
            rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null,
            rs.getDouble("cantidadInsumo")
        );
    }
}
