package dao;

import database.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DevolucionConjunto;
import model.DevolucionConjuntoVista;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DevolucionConjuntoDAO {

    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public void insertar(DevolucionConjunto devolucion) throws SQLException {
        insertar(getConnection(), devolucion);
    }

    public void insertar(Connection conn, DevolucionConjunto devolucion) throws SQLException {
        String sql = "INSERT INTO devolucionconjunto (folio, idConjunto, fecha) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, devolucion.getFolio());
            pstmt.setInt(2, devolucion.getIdConjunto());
            LocalDate fecha = devolucion.getFecha() != null ? devolucion.getFecha() : LocalDate.now();
            pstmt.setDate(3, Date.valueOf(fecha));
            pstmt.executeUpdate();
        }
    }

    public boolean existeDevolucion(int folio, int idConjunto) throws SQLException {
        return existeDevolucion(getConnection(), folio, idConjunto);
    }

    public boolean existeDevolucion(Connection conn, int folio, int idConjunto) throws SQLException {
        String sql = "SELECT 1 FROM devolucionconjunto WHERE folio = ? AND idConjunto = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            pstmt.setInt(2, idConjunto);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public ObservableList<DevolucionConjuntoVista> obtenerTodasConDetalles() throws SQLException {
        ObservableList<DevolucionConjuntoVista> lista = FXCollections.observableArrayList();
        String sql = """
            SELECT dc.folio, c.nombre, dc.fecha AS fechaDevolucion, v.fecha AS fechaVenta
            FROM devolucionconjunto dc
            JOIN venta v ON v.folio = dc.folio
            JOIN conjunto c ON c.id = dc.idConjunto
            ORDER BY dc.fecha DESC, dc.folio DESC
            """;
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new DevolucionConjuntoVista(
                    rs.getInt("folio"),
                    rs.getString("nombre"),
                    rs.getDate("fechaDevolucion").toLocalDate(),
                    rs.getDate("fechaVenta").toLocalDate()
                ));
            }
        }
        return lista;
    }

    public List<DevolucionConjunto> getAll() throws SQLException {
        List<DevolucionConjunto> lista = new ArrayList<>();
        String sql = "SELECT folio, idConjunto, fecha FROM devolucionconjunto";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DevolucionConjunto(
                    rs.getInt("folio"),
                    rs.getInt("idConjunto"),
                    rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null
                ));
            }
        }
        return lista;
    }

    public boolean insert(DevolucionConjunto devolucion) throws SQLException {
        insertar(devolucion);
        return true;
    }
}
