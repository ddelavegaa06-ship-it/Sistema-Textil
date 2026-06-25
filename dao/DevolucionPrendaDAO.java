package dao;

import java.sql.* ;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.DevolucionConjunto;
import model.DevolucionPrenda;

public class DevolucionPrendaDAO {
    private Connection getConnection() {
        return Conexion.getConnection();
    }

    public boolean insert(DevolucionPrenda devolucion){
        String sql = "INSERT INTO devolucionPrenda(id, folioVenta, idPrenda) VALUES(?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, devolucion.getId());
            ps.setInt(2, devolucion.getFolioVenta());
            ps.setInt(3, devolucion.getIdPrenda());
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(DevolucionPrenda devolucion){
        String sql = "UPDATE devolucionPrenda SET idPrenda = ?, fecha = ? WHERE folioVenta = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setInt(1, devolucion.getIdPrenda());
            ps.setDate(2, java.sql.Date.valueOf(devolucion.getFecha()));
            ps.setInt(3, devolucion.getFolioVenta());
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM devolucionPrenda WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<DevolucionPrenda> buscarPorId(int id){
        String sql = "SELECT * FROM devolucionPrenda where folioVenta = ? ";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                DevolucionPrenda devolucion = new DevolucionPrenda(
                    rs.getInt("id"),
                    rs.getInt("folioVenta"),
                    rs.getInt("idPrenda"),
                    rs.getDate("fecha").toLocalDate()
                );
                return Optional.of(devolucion);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public List<DevolucionPrenda> encontrarTodo(){
        List<DevolucionPrenda> devoluciones = new ArrayList<>();
        String sql = "SELECT * FROM devolucionPrenda";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    DevolucionPrenda devolucion = new DevolucionPrenda(
                        rs.getInt("id"),
                        rs.getInt("folioVenta"),
                        rs.getInt("idPrenda"),
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
