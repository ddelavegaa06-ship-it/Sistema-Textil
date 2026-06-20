package model;

import java.time.LocalDate;

public class InsumoPrenda {
    private int folio;
    private String idInsumo;
    private int idPrenda;
    private double cantidadInsumo;
    private LocalDate fecha;

    public InsumoPrenda(int folio, String idInsumo, int idPrenda,LocalDate fecha ,double cantidadInsumo){
        this.folio = folio;
        this.idInsumo = idInsumo;
        this.idPrenda = idPrenda;
        this.cantidadInsumo = cantidadInsumo;
        this.fecha = fecha;
    }
    public InsumoPrenda(int folio, String idInsumo, int idPrenda, double cantidadInsumo){
        this.folio = folio;
        this.idInsumo = idInsumo;
        this.idPrenda = idPrenda;
        this.cantidadInsumo = cantidadInsumo;
        this.fecha = null;
    }

    public int getFolio(){
        return folio; 
    }
    public String getIdInsumo(){
        return idInsumo; 
    }
    public int getIdPrenda(){
        return idPrenda; 
    }
    public double getCantidadInsumo(){
        return cantidadInsumo; 
    }
    public void setCantidadInsumo(double nCantidadInsumo){
        cantidadInsumo = nCantidadInsumo; 
    }
    public LocalDate getFecha(){
        return fecha; 
    }
}
