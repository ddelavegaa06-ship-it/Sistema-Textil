package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.PrendaConjunto;

public class PrendaConjuntoDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(PrendaConjunto pc){
        String sql = "INSERT INTO prendaConjunto(idPrenda, idConjunto) VALUES(?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,pc.getIdPrenda());
            ps.setInt(2,pc.getIdConjunto());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(PrendaConjunto pc){
        String sql = "UPDATE prendaConjunto SET idPrenda = ?, idConjunto = ? WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setInt(1, pc.getIdPrenda());
            ps.setInt(2, pc.getIdConjunto());
            ps.setInt(3, pc.getId());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM prendaConjunto WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<PrendaConjunto> buscarPorId(int id){
        String sql = "SELECT * FROM prendaConjunto where id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                PrendaConjunto pc = new PrendaConjunto(
                    rs.getInt("id"),
                    rs.getInt("idPrenda"),
                    rs.getInt("idConjunto")
                );
                return Optional.of(pc);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<PrendaConjunto> encontrarTodo(){
        List<PrendaConjunto> prendasConjunto = new ArrayList<>();
        String sql = "SELECT * FROM prendaConjunto";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    PrendaConjunto pc = new PrendaConjunto(
                        rs.getInt("id"),
                        rs.getInt("idPrenda"),
                        rs.getInt("idConjunto")
                    );
                    prendasConjunto.add(pc);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return prendasConjunto;
    }
}
