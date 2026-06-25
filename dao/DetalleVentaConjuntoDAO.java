package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.DetalleVentaConjunto;

public class DetalleVentaConjuntoDAO {
      private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DetalleVentaConjunto detalle){
        String sql = "INSERT INTO detalleVentaConjunto(folioVenta, idConjunto, cantidad,total) VALUES(?,?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, detalle.getFolioVenta());
            ps.setInt(2, detalle.getIdConjunto());
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

    public boolean update(DetalleVentaConjunto detalle){
        String sql = "UPDATE detalleVentaConjunto SET folioVenta = ?, idConjunto = ?, cantidad = ?, total = ? WHERE folioVenta = ? AND idConjunto = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setInt(1, detalle.getFolioVenta());
            ps.setInt(2, detalle.getIdConjunto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getTotal());
            ps.setInt(5, detalle.getFolioVenta());
            ps.setInt(6, detalle.getIdConjunto());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM detalleVentaConjunto WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<DetalleVentaConjunto> buscarPorId(int id){
        String sql = "SELECT * FROM detalleVentaConjunto where folioVenta = ? ";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                DetalleVentaConjunto detalle = new DetalleVentaConjunto(
                    rs.getInt("folioVenta"),
                    rs.getInt("idConjunto"),
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

    public List<DetalleVentaConjunto> encontrarTodo(){
        List<DetalleVentaConjunto> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalleVentaConjunto";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    DetalleVentaConjunto detalle = new DetalleVentaConjunto(
                        rs.getInt("folioVenta"),
                        rs.getInt("idConjunto"),
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
