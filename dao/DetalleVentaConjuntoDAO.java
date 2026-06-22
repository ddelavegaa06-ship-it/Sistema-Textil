package dao;

import database.Conexion;
import model.DetalleVentaConjunto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaConjuntoDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DetalleVentaConjunto detalle) throws SQLException {
        String sql = "INSERT INTO detalleventaconjunto (folioVenta, idConjunto, cantidad, total) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detalle.getFolioVenta());
            pstmt.setInt(2, detalle.getIdConjunto());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getTotal());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<DetalleVentaConjunto> getByVenta(int folioVenta) throws SQLException {
        List<DetalleVentaConjunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalleventaconjunto WHERE folioVenta = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folioVenta);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetalleVentaConjunto(
                        rs.getInt("folioVenta"),
                        rs.getInt("idConjunto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                    ));
                }
            }
        }
        return lista;
    }

    public List<DetalleVentaConjunto> getAll() throws SQLException {
        List<DetalleVentaConjunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalleventaconjunto";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DetalleVentaConjunto(
                    rs.getInt("folioVenta"),
                    rs.getInt("idConjunto"),
                    rs.getInt("cantidad"),
                    rs.getDouble("total")
                ));
            }
        }
        return lista;
    }
}
