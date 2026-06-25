package dao;

import database.Conexion;
import model.Insumo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public List<Insumo> getAll() throws SQLException {
        List<Insumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM insumo";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    public Insumo getById(String id) throws SQLException {
        String sql = "SELECT * FROM insumo WHERE id = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    public boolean insert(Insumo insumo) throws SQLException {
        String sql = "INSERT INTO insumo (id, numeroPartida, existencia, tipoExistencia, descripcion, nombre, color, medida, ancho, composicion, tipo, `no.`, tamanio, talla, material, tipoInsumo, idUbicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insumo.getId());
            pstmt.setString(2, insumo.getNumeroPartida());
            pstmt.setDouble(3, insumo.getExistencia());
            pstmt.setString(4, insumo.getTipoExistencia());
            pstmt.setString(5, insumo.getDescripcion());
            pstmt.setString(6, insumo.getNombre());
            pstmt.setString(7, insumo.getColor());
            pstmt.setDouble(8, insumo.getMedida());
            pstmt.setDouble(9, insumo.getAncho());
            pstmt.setString(10, insumo.getComposicion());
            pstmt.setString(11, insumo.getTipo());
            pstmt.setInt(12, insumo.getNo());
            pstmt.setString(13, insumo.getTamanio());
            pstmt.setDouble(14, insumo.getTalla());
            pstmt.setString(15, insumo.getMaterial());
            pstmt.setString(16, insumo.getTipoInsumo());
            pstmt.setInt(17, insumo.getIdUbicacion());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean update(Insumo insumo) throws SQLException {
        String sql = "UPDATE insumo SET numeroPartida = ?, existencia = ?, tipoExistencia = ?, descripcion = ?, nombre = ?, color = ?, medida = ?, ancho = ?, composicion = ?, tipo = ?, `no.` = ?, tamanio = ?, talla = ?, material = ?, tipoInsumo = ?, idUbicacion = ? WHERE id = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insumo.getNumeroPartida());
            pstmt.setDouble(2, insumo.getExistencia());
            pstmt.setString(3, insumo.getTipoExistencia());
            pstmt.setString(4, insumo.getDescripcion());
            pstmt.setString(5, insumo.getNombre());
            pstmt.setString(6, insumo.getColor());
            pstmt.setDouble(7, insumo.getMedida());
            pstmt.setDouble(8, insumo.getAncho());
            pstmt.setString(9, insumo.getComposicion());
            pstmt.setString(10, insumo.getTipo());
            pstmt.setInt(11, insumo.getNo());
            pstmt.setString(12, insumo.getTamanio());
            pstmt.setDouble(13, insumo.getTalla());
            pstmt.setString(14, insumo.getMaterial());
            pstmt.setString(15, insumo.getTipoInsumo());
            pstmt.setInt(16, insumo.getIdUbicacion());
            pstmt.setString(17, insumo.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM insumo WHERE id = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Insumo mapResultSet(ResultSet rs) throws SQLException {
        return new Insumo(
            rs.getString("id"),
            rs.getString("numeroPartida"),
            rs.getDouble("existencia"),
            rs.getString("tipoExistencia"),
            rs.getString("descripcion"),
            rs.getString("nombre"),
            rs.getString("color"),
            rs.getDouble("medida"),
            rs.getDouble("ancho"),
            rs.getString("composicion"),
            rs.getString("tipo"),
            rs.getInt("no."),
            rs.getString("tamanio"),
            rs.getDouble("talla"),
            rs.getString("material"),
            rs.getString("tipoInsumo"),
            rs.getInt("idUbicacion")
        );
    }
}
