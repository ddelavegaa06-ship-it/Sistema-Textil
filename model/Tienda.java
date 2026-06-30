package model;

public class Tienda {
    private int id;
    private String tipo;
    private String nombre;
    private int idPadre;

    public Tienda(int id, String tipo, String nombre, int idPadre){
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.idPadre = idPadre;
    }
    public int getId(){
        return id;
    }
    public String getTipo(){
        return tipo;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nNombre){
        nombre = nNombre;
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
