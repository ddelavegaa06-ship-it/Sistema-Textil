package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.Ubicacion;

public class UbicacionDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(Ubicacion ubicacion){
        String sql = "INSERT INTO ubicacion(tipo, nombre, idPadre) VALUES(?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,ubicacion.getTipo());
            ps.setString(2, ubicacion.getNombre());
            ps.setInt(3, ubicacion.getIdPadre());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Ubicacion ubicacion){
        String sql = "UPDATE ubicacion SET tipo = ?, nombre = ?, idPadre = ? WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setString(1, ubicacion.getTipo());
            ps.setString(2, ubicacion.getNombre());
            ps.setInt(3, ubicacion.getIdPadre());
            ps.setInt(4, ubicacion.getId());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM ubicacion WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Ubicacion> buscarPorId(int id){
        String sql = "SELECT * FROM ubicacion where id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Ubicacion ubicacion = new Ubicacion(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("nombre"),
                    rs.getInt("idPadre")
                );
                return Optional.of(ubicacion);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Ubicacion> encontrarTodo(){
        List<Ubicacion> ubicaciones = new ArrayList<>();
        String sql = "SELECT * FROM ubicacion";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    Ubicacion ubicacion = new Ubicacion(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getInt("idPadre")
                    );
                    ubicaciones.add(ubicacion);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ubicaciones;
    }
}
