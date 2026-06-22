package dao;

import database.Conexion;
import model.Prenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrendaDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public List<Prenda> getAll() throws SQLException {
        List<Prenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM prenda";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    public Prenda getById(int id) throws SQLException {
        String sql = "SELECT * FROM prenda WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    public int insert(Prenda p) throws SQLException {
        String sql = "INSERT INTO prenda (nombre, talla, existencia, precioMayoreo, precioMenudeo, idTienda, codigoBarras) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getTalla());
            pstmt.setInt(3, p.getExistencia());
            pstmt.setDouble(4, p.getPrecioMayoreo());
            pstmt.setDouble(5, p.getPrecioMenudeo());
            if (p.getIdTienda() > 0) pstmt.setInt(6, p.getIdTienda());
            else pstmt.setNull(6, Types.INTEGER);
            pstmt.setString(7, p.getCodigoBarras());
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    public boolean update(Prenda p) throws SQLException {
        String sql = "UPDATE prenda SET nombre = ?, talla = ?, existencia = ?, precioMayoreo = ?, precioMenudeo = ?, idTienda = ?, codigoBarras = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getTalla());
            pstmt.setInt(3, p.getExistencia());
            pstmt.setDouble(4, p.getPrecioMayoreo());
            pstmt.setDouble(5, p.getPrecioMenudeo());
            if (p.getIdTienda() > 0) pstmt.setInt(6, p.getIdTienda());
            else pstmt.setNull(6, Types.INTEGER);
            pstmt.setString(7, p.getCodigoBarras());
            pstmt.setInt(8, p.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM prenda WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Prenda> getByNombre(String nombre) throws SQLException {
        List<Prenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM prenda WHERE nombre LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        }
        return lista;
    }

    private Prenda mapResultSet(ResultSet rs) throws SQLException {
        Prenda p = new Prenda(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("talla"),
            rs.getInt("existencia"),
            rs.getDouble("precioMayoreo"),
            rs.getDouble("precioMenudeo"),
            rs.getObject("idTienda") != null ? rs.getInt("idTienda") : null,
            rs.getString("codigoBarras")
        );
        return p;
    }
}
