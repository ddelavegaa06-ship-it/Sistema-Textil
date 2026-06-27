package dao;

import database.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DevolucionVista;
import model.DevolucionPrenda;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DevolucionPrendaDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public void insertar(DevolucionPrenda devolucion) throws SQLException {
        insertar(getConnection(), devolucion);
    }

    public void insertar(Connection conn, DevolucionPrenda devolucion) throws SQLException {
        String sql = "INSERT INTO devolucionprenda (folioVenta, idPrenda, fecha) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, devolucion.getFolioVenta());
            pstmt.setInt(2, devolucion.getIdPrenda());
            LocalDate fecha = devolucion.getFecha() != null ? devolucion.getFecha() : LocalDate.now();
            pstmt.setDate(3, Date.valueOf(fecha));
            pstmt.executeUpdate();
        }
    }

    public boolean existeDevolucion(int folioVenta, int idPrenda) throws SQLException {
        return existeDevolucion(getConnection(), folioVenta, idPrenda);
    }

    public boolean existeDevolucion(Connection conn, int folioVenta, int idPrenda) throws SQLException {
        String sql = "SELECT 1 FROM devolucionprenda WHERE folioVenta = ? AND idPrenda = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folioVenta);
            pstmt.setInt(2, idPrenda);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public ObservableList<DevolucionVista> obtenerTodasConDetalles() throws SQLException {
        ObservableList<DevolucionVista> lista = FXCollections.observableArrayList();
        String sql = """
            SELECT dp.folioVenta, p.nombre, p.talla, dp.fecha AS fechaDevolucion, v.fecha AS fechaVenta
            FROM devolucionprenda dp
            JOIN venta v ON v.folio = dp.folioVenta
            JOIN prenda p ON p.id = dp.idPrenda
            ORDER BY dp.fecha DESC, dp.folioVenta DESC
            """;
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new DevolucionVista(
                    rs.getInt("folioVenta"),
                    rs.getString("nombre"),
                    rs.getString("talla"),
                    rs.getDate("fechaDevolucion").toLocalDate(),
                    rs.getDate("fechaVenta").toLocalDate()
                ));
            }
        }
        return lista;
    }

    public List<DevolucionPrenda> obtenerPorFolioVenta(int folio) throws SQLException {
        List<DevolucionPrenda> lista = new ArrayList<>();
        String sql = "SELECT folioVenta, idPrenda, fecha FROM devolucionprenda WHERE folioVenta = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DevolucionPrenda(
                        rs.getInt("folioVenta"),
                        rs.getInt("idPrenda"),
                        rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null
                    ));
                }
            }
        }
        return lista;
    }

    public List<DevolucionPrenda> getAll() throws SQLException {
        List<DevolucionPrenda> lista = new ArrayList<>();
        String sql = "SELECT folioVenta, idPrenda, fecha FROM devolucionprenda";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new DevolucionPrenda(
                    rs.getInt("folioVenta"),
                    rs.getInt("idPrenda"),
                    rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null
                ));
            }
        }
        return lista;
    }

    public boolean insert(DevolucionPrenda devolucion) throws SQLException {
        insertar(devolucion);
        return true;
    }
}
