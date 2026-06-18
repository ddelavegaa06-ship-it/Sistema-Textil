package dao;
import java.lang.StackWalker.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.naming.spi.DirStateFactory.Result;

import model.Prenda;
import database.Conexion;

public class PrendaDAO {
    
      private Connection getConnection() {
        return Conexion.getConnection();
    }
    public boolean insert(Prenda prenda){
        String sql = "INSERT INTO prenda(nombre, talla, existencia,precioMayoreo,precioMenudeo,idTienda,codigoBarras) VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,prenda.getNombre());
            ps.setString(2, prenda.getTalla());
            ps.setInt(3, prenda.getExistencia());
            ps.setDouble(4, prenda.getPrecioMayoreo());
            ps.setDouble(5, prenda.getPrecioMenudeo());
            ps.setInt(6, prenda.getIdTienda());
            ps.setString(7, prenda.getCodigoBarras());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Prenda prenda){
        String sql = "UPDATE prenda SET nombre = ?,talla = ? existencia = ?,precioMayoreo = ?, precioMenudeo = ?, idTienda = ? WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setString(1, prenda.getNombre());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM prenda WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Prenda> buscarPorId(int id){
        String sql = "SELECT * FROM prenda where id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Prenda prenda = new Prenda(rs.getInt("id"), rs.getString("nombre"), rs.getString("talla"), rs.getInt("existencia"), rs.getDouble("precioMayoreo"), rs.getDouble("precioMenudeo"), rs.getInt("idTienda"), rs.getString("codigoBarras"));
                return Optional.of(prenda);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Prenda> encontrarTodo(){
        List<Prenda> prendas = new ArrayList<>();
        String sql = "SELECT * FROM prenda";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    Prenda prenda = new Prenda(rs.getInt("id"), rs.getString("nombre"), rs.getString("talla"), rs.getInt("existencia"), rs.getDouble("precioMayoreo"), rs.getDouble("precioMenudeo"), rs.getInt("idTienda"), rs.getString("codigoBarras"));
                    prendas.add(prenda);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return prendas;
    }


}
