package model;

public class DetalleVentaConjunto {
    private int folioVenta;
    private int idConjunto;
    private int cantidad;
    private double total;
    public DetalleVentaConjunto(int folioVenta, int idConjunto, int cantidad, double total){
        this.folioVenta = folioVenta;
        this.idConjunto = idConjunto;
        this.cantidad = cantidad;
        this.total = total;
    }
    public int getFolioVenta(){
        return folioVenta;
    }
    public int  getIdConjunto(){
        return idConjunto;
    }
    public void setIdConjunto(int idConjunto){
        this.idConjunto = idConjunto;
    }
    public int getCantidad(){
        return cantidad;
    }
    public void setCantidad(int cant){
        this.cantidad = cant;
    }

    public double getTotal(){
        return total;
    }
    public void setTotal(double total){
        this.total = total;
    }
    
}
