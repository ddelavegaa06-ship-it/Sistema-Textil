package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.DetalleVentaPrenda;

public class DetalleVentaPrendaDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DetalleVentaPrenda detalle){
        String sql = "INSERT INTO detalleVentaPrenda(folioVenta, idPrenda, cantidad,total) VALUES(?,?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, detalle.getFolioVenta());
            ps.setInt(2, detalle.getIdPrenda());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getTotal());
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(DetalleVentaPrenda detalle){
        String sql = "UPDATE detalleVentaPrenda SET folioVenta = ?, idPrenda = ?, cantidad = ?, total = ? WHERE folio = ? AND idPrenda = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setInt(1, detalle.getFolioVenta());
            ps.setInt(2, detalle.getIdPrenda());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getTotal());
            ps.setInt(5, detalle.getFolioVenta());
            ps.setInt(6, detalle.getIdPrenda());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM detalleVentaPrenda WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<DetalleVentaPrenda> buscarPorId(int id){
        String sql = "SELECT * FROM detalleVentaPrenda where folio = ? ";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                DetalleVentaPrenda detalle = new DetalleVentaPrenda(
                    rs.getInt("folio"),
                    rs.getInt("idPrenda"),
                    rs.getInt("cantidad"),
                    rs.getDouble("total")
                );
                return Optional.of(detalle);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<DetalleVentaPrenda> encontrarTodo(){
        List<DetalleVentaPrenda> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalleVentaPrenda";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    DetalleVentaPrenda detalle = new DetalleVentaPrenda(
                        rs.getInt("folio"),
                        rs.getInt("idPrenda"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                    );
                    detalles.add(detalle);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return detalles;
    }
}
