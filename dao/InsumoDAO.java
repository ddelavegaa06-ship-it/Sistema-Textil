package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.Insumo;

public class InsumoDAO {
     private Connection getConnection() {
        return Conexion.getConnection();
    }
    public boolean insert(Insumo insumo){
        String sql = "INSERT INTO insumo(numeroPartida, existencia, tipoExistencia,descripcion,nombre,color,medida,ancho,composicion, tipo, no., tamanio, talla, material,tipoInsumo,idUbicacion) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,insumo.getNumeroPartida());
            ps.setDouble(2, insumo.getExistencia());
            ps.setString(3, insumo.getTipoExistencia());
            ps.setString(4, insumo.getDescripcion());
            ps.setString(5, insumo.getNombre());
            ps.setString(6, insumo.getColor());
            ps.setDouble(7, insumo.getMedida());
            ps.setDouble(8, insumo.getAncho());
            ps.setString(9, insumo.getComposicion());
            ps.setString(10, insumo.getTipo());
            ps.setInt(11, insumo.getNo());
            ps.setString(12, insumo.getTamanio());
            ps.setDouble(13, insumo.getTalla());
            ps.setString(14, insumo.getMaterial());
            ps.setString(15, insumo.getTipoInsumo());
            ps.setInt(16, insumo.getIdUbicacion());

            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Insumo insumo){
        String sql = "UPDATE insumo SET numeroPartida = ?, existencia = ?, tipoExistencia = ?, descripcion = ?, nombre = ?, color = ?, medida = ?, ancho = ?, composicion = ?, tipo = ?, no = ?, tamanio = ?, talla = ?, material = ?, tipoInsumo = ?, idUbicacion = ? WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql) ){
            ps.setString(1, insumo.getNumeroPartida());
            ps.setDouble(2, insumo.getExistencia());
            ps.setString(3, insumo.getTipoExistencia());
            ps.setString(4, insumo.getDescripcion());
            ps.setString(5, insumo.getNombre());
            ps.setString(6, insumo.getColor());
            ps.setDouble(7, insumo.getMedida());
            ps.setDouble(8, insumo.getAncho());
            ps.setString(9, insumo.getComposicion());
            ps.setString(10, insumo.getTipo());
            ps.setInt(11, insumo.getNo());
            ps.setString(12, insumo.getTamanio());
            ps.setDouble(13, insumo.getTalla());
            ps.setString(14, insumo.getMaterial());
            ps.setString(15, insumo.getTipoInsumo());
            ps.setInt(16, insumo.getIdUbicacion());
            ps.setString(17, insumo.getId());

            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id){
        String sql = "DELETE FROM insumo WHERE id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setString(1, id);
            return ps.executeUpdate() > 0; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Insumo> buscarPorId(String id){
        String sql = "SELECT * FROM insumo where id = ?";

        try(PreparedStatement ps = getConnection().prepareStatement(sql)){
            ps.setString(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Insumo insumo = new Insumo(
                    rs.getString("id"),
                    rs.getString("numeroPartida"),
                    rs.getDouble("existencia"),
                    rs.getString("tipoExistencia"),
                    rs.getString("descripcion"),
                    rs.getString("nombre"),
                    rs.getString("color"),
                    rs.getDouble("medida"),
                    rs.getDouble("ancho"),
                    rs.getString("composicion"),
                    rs.getString("tipo"),
                    rs.getInt("no"),
                    rs.getString("tamanio"),
                    rs.getDouble("talla"),
                    rs.getString("material"),
                    rs.getString("tipoInsumo"),
                    rs.getInt("idUbicacion")
                );
                return Optional.of(insumo);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Insumo> encontrarTodo(){
        List<Insumo> insumos = new ArrayList<>();
        String sql = "SELECT * FROM insumo";

        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

                while(rs.next()){
                    Insumo insumo = new Insumo(
                        rs.getString("id"),
                        rs.getString("numeroPartida"),
                        rs.getDouble("existencia"),
                        rs.getString("tipoExistencia"),
                        rs.getString("descripcion"),
                        rs.getString("nombre"),
                        rs.getString("color"),
                        rs.getDouble("medida"),
                        rs.getDouble("ancho"),
                        rs.getString("composicion"),
                        rs.getString("tipo"),
                        rs.getInt("no"),
                        rs.getString("tamanio"),
                        rs.getDouble("talla"),
                        rs.getString("material"),
                        rs.getString("tipoInsumo"),
                        rs.getInt("idUbicacion")
                    );
                    insumos.add(insumo);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return insumos;
    }

}
