package model;

public class Prenda {
    private int id;
    private String nombre;
    private String talla;
    private int existencia;
    private double precioMayoreo;
    private double precioMenudeo;
    private int tienda;
    private String codigoBarras;


    public Prenda(int id,String nombre, String talla, int existencia, double precioMayoreo,double precioMenudeo, int tienda, String codigoBarras){
        this.id = id;
        this.nombre = nombre;
        this.talla = talla;
        this.existencia = existencia;
        this.precioMayoreo = precioMayoreo;
        this.precioMenudeo = precioMenudeo;
        this.tienda = tienda;
        this.codigoBarras = codigoBarras;
    }

    public int getId(){return this.id;}

    public String getNombre(){return this.nombre;}
    
    public String getTalla(){return this.talla;}

    public int getExistencia(){return this.existencia;}
    public void setExistencia(int exis){
        existencia = exis;
    }
    public double getPrecioMenudeo(){return this.precioMenudeo;}
    public void setPrecioMenudeo(double pre){
        precioMenudeo = pre;
    }
    
    public double getPrecioMayoreo(){return this.precioMayoreo;}
    public void setPrecioMayoreo(double precio){
        precioMayoreo = precio;
    }
    public int getIdTienda(){
        return tienda;
    }
    public void setIdTienda(int nTienda){
        tienda = nTienda;
    }

    public String getCodigoBarras(){
        return this.codigoBarras;
    }

}
