package model;

public class Ubicacion {
    private int id;
    private String nombre;
    private String tipo;
    private int idPadre;

    public Ubicacion(int id, String nombre, String tipo, int idPadre){
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.idPadre = idPadre;
    }
    public int getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nNombre){
        nombre = nNombre;
    }
    public String getTipo(){
        return tipo;
    }
    
    public int getIdPadre(){
        return idPadre;
    }
    public void setIdPadre(int nIdPadre){
        idPadre = nIdPadre;
    }
    public void setTipo(String nTipo){
        tipo = nTipo;
    }
}
