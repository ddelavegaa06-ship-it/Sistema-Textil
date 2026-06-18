package model;

public class Conjunto {
    private int id;
    private String nombre;
    private int piezas;
    private double precio;
    public Conjunto(int id, String nombre, int piezas, double precio){
        this.id = id;
        this.nombre = nombre;
        this.piezas = piezas;
        this.precio = precio;
    }

    public int getId(){return id;}
    public String getNombre(){return nombre;}
    public void setNombre(String nombre){
        this.nombre = nombre;   
    }
    
    public int getPiezas(){return piezas;}
    public void setPiezas(int piezas){
        this.piezas = piezas;
    }

    public double getPrecio(){return precio;}
    public void setPrecio(double precio){
        this.precio = precio;
    }
    
}