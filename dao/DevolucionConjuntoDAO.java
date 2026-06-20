package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.DetalleVentaConjunto;
import model.DevolucionConjunto;

public class DevolucionConjuntoDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DevolucionConjunto devolucion){
        String sql = "INSERT INTO devolucionConjunto(id, folio, idConjunto) VALUES(?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, devolucion.getId());
            ps.setInt(2, devolucion.getFolio());
            ps.setInt(3, devolucion.getIdConjunto());
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(DevolucionConjunto devolucion){
        String sql = "UPDATE devolucionConjunto SET idConjunto = ?, fecha = ? WHERE folio = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setInt(1, devolucion.getIdConjunto());
            ps.setDate(2, java.sql.Date.valueOf(devolucion.getFecha()));
            ps.setInt(3, devolucion.getFolio());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM devolucionConjunto WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<DevolucionConjunto> buscarPorId(int id){
        String sql = "SELECT * FROM devolucionConjunto where folio = ? ";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                DevolucionConjunto devolucion = new DevolucionConjunto(
                    rs.getInt("id"),
                    rs.getInt("folio"),
                    rs.getInt("idConjunto"),
                    rs.getDate("fecha").toLocalDate()
                );
                return Optional.of(devolucion);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public List<DevolucionConjunto> encontrarTodo(){
        List<DevolucionConjunto> devoluciones = new ArrayList<>();
        String sql = "SELECT * FROM devolucionConjunto";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    DevolucionConjunto devolucion = new DevolucionConjunto(
                        rs.getInt("id"),
                        rs.getInt("folio"),
                        rs.getInt("idConjunto"),
                        rs.getDate("fecha").toLocalDate()
                    );
                    devoluciones.add(devolucion);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return devoluciones;
    }
}
