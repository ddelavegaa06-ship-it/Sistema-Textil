package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.InsumoPrenda;

public class InsumoPrendaDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(InsumoPrenda ip){
        String sql = "INSERT INTO insumoPrenda(folio, idInsumo, idPrenda, cantidadInsumo) VALUES(?,?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,ip.getFolio());
            ps.setString(2,ip.getIdInsumo());
            ps.setInt(3,ip.getIdPrenda());
            ps.setDouble(4, ip.getCantidadInsumo());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(InsumoPrenda ip){
        String sql = "UPDATE insumoPrenda SET idInsumo = ?, idPrenda = ?, cantidadInsumo = ? WHERE folio = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setString(1, ip.getIdInsumo());
            ps.setInt(2, ip.getIdPrenda());
            ps.setDouble(3, ip.getCantidadInsumo());
            ps.setInt(4, ip.getFolio());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM insumoPrenda WHERE folio = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<InsumoPrenda> buscarPorId(int id){
        String sql = "SELECT * FROM insumoPrenda where folio = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                InsumoPrenda ip = new InsumoPrenda(
                    rs.getInt("folio"),
                    rs.getString("idInsumo"),
                    rs.getInt("idPrenda"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getDouble("cantidadInsumo")
                );
                return Optional.of(ip);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<InsumoPrenda> encontrarTodo(){
        List<InsumoPrenda> insumos = new ArrayList<>();
        String sql = "SELECT * FROM insumoPrenda";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    InsumoPrenda ip = new InsumoPrenda(
                        rs.getInt("folio"),
                        rs.getString("idInsumo"),
                        rs.getInt("idPrenda"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("cantidadInsumo")
                    );
                    insumos.add(ip);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return insumos;
    }
}
