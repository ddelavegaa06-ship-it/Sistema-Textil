package dao;

import database.Conexion;
import model.DetalleVentaConjunto;
import model.DetalleVentaPrenda;
import model.Venta;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public int registrarVenta(List<DetalleVentaPrenda> detallesPrenda, List<DetalleVentaConjunto> detallesConjunto) throws SQLException {
        Connection conn = getConnection();
        boolean initialAutoCommit = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);
            int folio = insertarVenta(conn);
            for (DetalleVentaPrenda detalle : detallesPrenda) {
                detalle.setFolioVenta(folio);
                insertarDetallePrenda(conn, detalle);
                actualizarExistenciaPrenda(conn, detalle.getIdPrenda(), detalle.getCantidad());
            }
            for (DetalleVentaConjunto detalle : detallesConjunto) {
                detalle.setFolioVenta(folio);
                insertarDetalleConjunto(conn, detalle);
                actualizarExistenciaConjunto(conn, detalle.getIdConjunto(), detalle.getCantidad());
            }
            conn.commit();
            return folio;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            conn.setAutoCommit(initialAutoCommit);
        }
    }

    public List<Venta> getAll() throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM venta";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Venta(rs.getInt("folio"), rs.getDate("fecha").toLocalDate()));
            }
        }
        return lista;
    }

    public Venta getById(int folio) throws SQLException {
        String sql = "SELECT * FROM venta WHERE folio = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Venta(rs.getInt("folio"), rs.getDate("fecha").toLocalDate());
                }
            }
        }
        return null;
    }

    private int insertarVenta(Connection conn) throws SQLException {
        String sql = "INSERT INTO venta (fecha) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el folio de venta");
    }

    private void insertarDetallePrenda(Connection conn, DetalleVentaPrenda detalle) throws SQLException {
        String sql = "INSERT INTO detalleventaprenda (folio, idPrenda, cantidad, total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detalle.getFolioVenta());
            pstmt.setInt(2, detalle.getIdPrenda());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getTotal());
            pstmt.executeUpdate();
        }
    }

    private void insertarDetalleConjunto(Connection conn, DetalleVentaConjunto detalle) throws SQLException {
        String sql = "INSERT INTO detalleventaconjunto (folioVenta, idConjunto, cantidad, total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detalle.getFolioVenta());
            pstmt.setInt(2, detalle.getIdConjunto());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getTotal());
            pstmt.executeUpdate();
        }
    }

    private void actualizarExistenciaPrenda(Connection conn, int idPrenda, int cantidadVendida) throws SQLException {
        String sql = "UPDATE prenda SET existencia = existencia - ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cantidadVendida);
            pstmt.setInt(2, idPrenda);
            pstmt.executeUpdate();
        }
    }

    private void actualizarExistenciaConjunto(Connection conn, int idConjunto, int cantidadVendida) throws SQLException {
        String sql = "SELECT idPrenda FROM prendaconjunto WHERE idConjunto = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idConjunto);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idPrenda = rs.getInt("idPrenda");
                    actualizarExistenciaPrenda(conn, idPrenda, cantidadVendida);
                }
            }
        }
    }
}
