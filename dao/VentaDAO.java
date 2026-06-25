package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.Venta;

public class VentaDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(Venta venta){
        String sql = "INSERT INTO venta() VALUES()";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public boolean delete(int id){
        String sql = "DELETE FROM prendaConjunto WHERE folio = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Venta> buscarPorId(int id){
        String sql = "SELECT * FROM venta where folio = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Venta venta = new Venta(
                    rs.getInt("folio"),
                    rs.getDate("fecha").toLocalDate()
                );
                return Optional.of(venta);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Venta> encontrarTodo(){
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    Venta venta = new Venta(
                        rs.getInt("folio"),
                        rs.getDate("fecha").toLocalDate()
                    );
                    ventas.add(venta);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ventas;
    }
}
