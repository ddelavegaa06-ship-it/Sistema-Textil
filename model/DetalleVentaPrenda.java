package model;

public class DetalleVentaPrenda {
    private int folioVenta;
    private int idPrenda;
    private int cantidad;
    private double total;
    public DetalleVentaPrenda(int folioVenta, int idPrenda, int cantidad, double total){
        this.folioVenta = folioVenta;
        this.idPrenda = idPrenda;
        this.cantidad = cantidad;
        this.total = total;
    }
    public int getFolioVenta(){
        return folioVenta;
    }
    public int  getIdPrenda(){
        return idPrenda;
    }
    public void setIdPrenda(int idPrenda){
        this.idPrenda = idPrenda;
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
