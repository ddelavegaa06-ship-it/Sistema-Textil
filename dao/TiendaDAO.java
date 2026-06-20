package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.Tienda;

public class TiendaDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(Tienda tienda){
        String sql = "INSERT INTO tienda(tipo,nombre, idPadre) VALUES(?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,tienda.getTipo());
            ps.setString(2, tienda.getNombre());
            ps.setInt(3, tienda.getIdPadre());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Tienda tienda){
        String sql = "UPDATE tienda SET tipo = ?, nombre = ?, idPadre = ? WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setString(1, tienda.getTipo());
            ps.setString(2, tienda.getNombre());
            ps.setInt(3, tienda.getIdPadre());
            ps.setInt(4, tienda.getId());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM tienda WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Tienda> buscarPorId(int id){
        String sql = "SELECT * FROM tienda where id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Tienda tienda = new Tienda(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getString("nombre"),
                    rs.getInt("idPadre")
                );
                return Optional.of(tienda);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Tienda> encontrarTodo(){
        List<Tienda> tiendas = new ArrayList<>();
        String sql = "SELECT * FROM tienda";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    Tienda tienda = new Tienda(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getInt("idPadre")
                    );
                    tiendas.add(tienda);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tiendas;
    }
}
