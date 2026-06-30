package model;

public class Conjunto {
    private int id;
    private String nombre;
    private int piezas;
    private double precio;
    private String descripcion = "";
    private java.util.List<String> idPrendas = new java.util.ArrayList<>();
    private int minimoExistencia = 3;

    public Conjunto(int id, String nombre, int piezas, double precio){
        this.id = id;
        this.nombre = nombre;
        this.piezas = piezas;
        this.precio = precio;
    }

    public Conjunto(String idStr, String nombre, String descripcion, java.util.List<String> idPrendas, int piezas) {
        try {
            this.id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            this.id = 0;
        }
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idPrendas = idPrendas;
        this.piezas = piezas;
        this.precio = 0.0;
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

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public java.util.List<String> getIdPrendas() { return idPrendas; }
    public void setIdPrendas(java.util.List<String> idPrendas) { this.idPrendas = idPrendas; }
    public int getMinimoExistencia() { return minimoExistencia; }
    public void setMinimoExistencia(int minimoExistencia) { this.minimoExistencia = minimoExistencia; }
}