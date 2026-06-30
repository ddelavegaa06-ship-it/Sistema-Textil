package dao;

import database.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DetalleVentaConjunto;
import model.DetalleVentaPrenda;
import model.PrendaVendida;
import model.Venta;
import model.VentaCompleta;
import model.VentaResumen;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private static final int DIAS_DEVOLUCION = 30;

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

    // Obtiene las ventas de prendas desde la BD con su detalle para mostrarlas en UI.
    public ObservableList<PrendaVendida> obtenerTodasLasVentas() throws SQLException {
        ObservableList<PrendaVendida> ventas = FXCollections.observableArrayList();
        String sql = """
            SELECT v.folio, v.fecha, d.idPrenda, d.cantidad, d.total,
                   p.nombre, p.talla, p.precioMayoreo, p.precioMenudeo
            FROM venta v
            INNER JOIN detalleventaprenda d ON d.folio = v.folio
            INNER JOIN prenda p ON p.id = d.idPrenda
            ORDER BY v.folio DESC, d.idPrenda ASC
            """;

        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int cantidad = rs.getInt("cantidad");
                double total = rs.getDouble("total");
                double precioUnitario = cantidad > 0 ? total / cantidad : 0.0;
                double precioMayoreo = rs.getDouble("precioMayoreo");
                String tipoVenta = Math.abs(precioUnitario - precioMayoreo) < 0.01 ? "Mayoreo" : "Menudeo";
                LocalDate fechaVenta = rs.getDate("fecha").toLocalDate();

                ventas.add(new PrendaVendida(
                    String.valueOf(rs.getInt("folio")),
                    rs.getString("nombre"),
                    rs.getString("talla"),
                    cantidad,
                    tipoVenta,
                    precioUnitario,
                    fechaVenta,
                    fechaVenta.plusDays(DIAS_DEVOLUCION),
                    "Folio " + rs.getInt("folio")
                ));
            }
        }
        return ventas;
    }

    public List<DetalleVentaPrenda> obtenerDetallesPorFolio(int folio) throws SQLException {
        List<DetalleVentaPrenda> detalles = new ArrayList<>();
        String sql = "SELECT folio, idPrenda, cantidad, total FROM detalleventaprenda WHERE folio = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new DetalleVentaPrenda(
                        rs.getInt("folio"),
                        rs.getInt("idPrenda"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                    ));
                }
            }
        }
        return detalles;
    }

    public int crearVenta(LocalDate fecha) throws SQLException {
        String sql = "INSERT INTO venta (fecha) VALUES (?)";
        LocalDate fechaVenta = fecha != null ? fecha : LocalDate.now();
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, Date.valueOf(fechaVenta));
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo generar el folio de la venta");
    }

    public boolean existeVenta(int folio) throws SQLException {
        String sql = "SELECT 1 FROM venta WHERE folio = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public VentaCompleta obtenerVentaCompleta(int folio) throws SQLException {
        Venta venta = getById(folio);
        if (venta == null) {
            return null;
        }

        List<DetalleVentaPrenda> detallesPrenda = new ArrayList<>();
        String sqlPrenda = "SELECT folio, idPrenda, cantidad, total FROM detalleventaprenda WHERE folio = ?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sqlPrenda)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    detallesPrenda.add(new DetalleVentaPrenda(
                        rs.getInt("folio"),
                        rs.getInt("idPrenda"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                    ));
                }
            }
        }

        List<DetalleVentaConjunto> detallesConjunto = new ArrayList<>();
        String sqlConjunto = "SELECT folioVenta, idConjunto, cantidad, total FROM detalleventaconjunto WHERE folioVenta = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlConjunto)) {
            pstmt.setInt(1, folio);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    detallesConjunto.add(new DetalleVentaConjunto(
                        rs.getInt("folioVenta"),
                        rs.getInt("idConjunto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                    ));
                }
            }
        }

        return new VentaCompleta(venta, detallesPrenda, detallesConjunto);
    }

    public ObservableList<VentaResumen> obtenerTodasVentasResumen() throws SQLException {
        return buscarVentasResumen("");
    }

    public ObservableList<VentaResumen> buscarVentasResumen(String busqueda) throws SQLException {
        ObservableList<VentaResumen> lista = FXCollections.observableArrayList();
        String valorBusqueda = busqueda != null ? busqueda.trim() : "";
        int folioBuscado = -1;
        try {
            folioBuscado = Integer.parseInt(valorBusqueda);
        } catch (NumberFormatException ignored) {
        }

        String likeParam = "%" + valorBusqueda + "%";

        String sqlPrendas = """
            SELECT
                v.folio,
                v.fecha AS fechaVenta,
                p.id AS idPrenda,
                p.nombre AS nombrePrenda,
                p.talla,
                dvp.cantidad AS cantidadVendida,
                dvp.total,
                CASE WHEN dp.folioVenta IS NOT NULL THEN 'DEVUELTO' ELSE 'ACTIVO' END AS estado
            FROM venta v
            JOIN detalleventaprenda dvp ON v.folio = dvp.folio
            JOIN prenda p ON dvp.idPrenda = p.id
            LEFT JOIN devolucionprenda dp ON v.folio = dp.folioVenta AND p.id = dp.idPrenda
            WHERE (? = -1 OR v.folio = ?) OR p.nombre LIKE ?
            """;

        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sqlPrendas)) {
            pstmt.setInt(1, folioBuscado);
            pstmt.setInt(2, folioBuscado);
            pstmt.setString(3, likeParam);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new VentaResumen(
                        rs.getInt("folio"),
                        rs.getDate("fechaVenta").toLocalDate(),
                        "PRENDA",
                        rs.getInt("idPrenda"),
                        rs.getString("nombrePrenda"),
                        rs.getString("talla"),
                        rs.getInt("cantidadVendida"),
                        rs.getDouble("total"),
                        rs.getString("estado")
                    ));
                }
            }
        }

        String sqlConjuntos = """
            SELECT
                v.folio,
                v.fecha AS fechaVenta,
                c.id AS idConjunto,
                c.nombre AS nombreConjunto,
                dvc.cantidad AS cantidadVendida,
                dvc.total,
                CASE WHEN dc.folio IS NOT NULL THEN 'DEVUELTO' ELSE 'ACTIVO' END AS estado
            FROM venta v
            JOIN detalleventaconjunto dvc ON v.folio = dvc.folioVenta
            JOIN conjunto c ON dvc.idConjunto = c.id
            LEFT JOIN devolucionconjunto dc ON v.folio = dc.folio AND c.id = dc.idConjunto
            WHERE (? = -1 OR v.folio = ?) OR c.nombre LIKE ?
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sqlConjuntos)) {
            pstmt.setInt(1, folioBuscado);
            pstmt.setInt(2, folioBuscado);
            pstmt.setString(3, likeParam);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new VentaResumen(
                        rs.getInt("folio"),
                        rs.getDate("fechaVenta").toLocalDate(),
                        "CONJUNTO",
                        rs.getInt("idConjunto"),
                        rs.getString("nombreConjunto"),
                        "",
                        rs.getInt("cantidadVendida"),
                        rs.getDouble("total"),
                        rs.getString("estado")
                    ));
                }
            }
        }

        lista.sort((a, b) -> {
            int byDate = b.getFechaVenta().compareTo(a.getFechaVenta());
            if (byDate != 0) return byDate;
            return Integer.compare(b.getFolioVenta(), a.getFolioVenta());
        });

        return lista;
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
