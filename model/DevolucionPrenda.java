package model;

import java.time.LocalDate;

public class DevolucionPrenda {
    private int folioVenta;
    private int idPrenda;
    private LocalDate fecha;

    public DevolucionPrenda(int folioVenta, int idPrenda, LocalDate fecha){
        this.folioVenta = folioVenta;
        this.idPrenda = idPrenda;
        this.fecha = fecha;
    }

    public DevolucionPrenda(int folioVenta, int idPrenda){
        this.folioVenta = folioVenta;
        this.idPrenda = idPrenda;
        this.fecha = null;
    }

    public int getFolioVenta(){
        return folioVenta; 
    }

    public int getIdPrenda(){
        return idPrenda; 
    }

    public LocalDate getFecha(){
        return fecha; 
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
