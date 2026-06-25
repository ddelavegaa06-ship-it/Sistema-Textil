package dao;
import java.lang.StackWalker.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.naming.spi.DirStateFactory.Result;

import model.Conjunto;
import database.Conexion;

public class ConjuntoDAO {
     private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(Conjunto conjunto){
        String sql = "INSERT INTO conjunto(nombre, piezas, precio) VALUES(?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,conjunto.getNombre());
            ps.setInt(2, conjunto.getPiezas());
            ps.setDouble(3, conjunto.getPrecio());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Conjunto conjunto){
        String sql = "UPDATE conjunto SET nombre = ?,piezas = ? precio = ? WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setString(1, conjunto.getNombre());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM conjunto WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Conjunto> buscarPorId(int id){
        String sql = "SELECT * FROM conjunto where id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Conjunto conjunto = new Conjunto(rs.getInt("id"), rs.getString("nombre"), rs.getInt("piezas"), rs.getDouble("precio"));
                return Optional.of(conjunto);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Conjunto> encontrarTodo(){
        List<Conjunto> conjuntos = new ArrayList<>();
        String sql = "SELECT * FROM conjunto";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    Conjunto conjunto = new Conjunto(rs.getInt("id"), rs.getString("nombre"), rs.getInt("piezas"), rs.getDouble("precio"));
                    conjuntos.add(conjunto);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return conjuntos;
    }
}
