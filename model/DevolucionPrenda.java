package model;

import java.time.LocalDate;

public class DevolucionPrenda {
    private int id;
    private int folioVenta;
    private int idPrenda;
    private LocalDate fecha;

    public DevolucionPrenda( int id, int folioVenta, int idPrenda, LocalDate fecha){
        this.id = id;
        this.folioVenta = folioVenta;
        this.idPrenda = idPrenda;
        this.fecha = fecha;
    }
    public DevolucionPrenda( int id, int folioVenta, int idPrenda){
        this.id = id;
        this.folioVenta = folioVenta;
        this.idPrenda = idPrenda;
        this.fecha = null;
    }
    public int getId(){
        return id; 
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
}
