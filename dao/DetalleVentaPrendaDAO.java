package dao;

import database.Conexion;
import model.DetalleVentaPrenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaPrendaDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DetalleVentaPrenda detalle) throws SQLException {
        String sql = "INSERT INTO detalleventaprenda (folio, idPrenda, cantidad, total) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detalle.getFolioVenta());
            pstmt.setInt(2, detalle.getIdPrenda());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getTotal());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<DetalleVentaPrenda> getByVenta(int folio) throws SQLException {
        List<DetalleVentaPrenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalleventaprenda WHERE folio = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetalleVentaPrenda(
                        rs.getInt("folio"),
                        rs.getInt("idPrenda"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                    ));
                }
            }
        }
        return lista;
    }

    public List<DetalleVentaPrenda> getAll() throws SQLException {
        List<DetalleVentaPrenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalleventaprenda";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DetalleVentaPrenda(
                    rs.getInt("folio"),
                    rs.getInt("idPrenda"),
                    rs.getInt("cantidad"),
                    rs.getDouble("total")
                ));
            }
        }
        return lista;
    }
}
