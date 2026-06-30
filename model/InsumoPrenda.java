package model;

import java.time.LocalDate;

public class InsumoPrenda {
    private int folio;
    private String idInsumo;
    private int idPrenda;
    private double cantidadInsumo;
    private LocalDate fecha;

    public InsumoPrenda() {
        this.folio = 0;
        this.idInsumo = null;
        this.idPrenda = 0;
        this.cantidadInsumo = 0.0;
        this.fecha = null;
    }

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
    public InsumoPrenda(String idInsumo, int idPrenda, double cantidadInsumo){
        this.folio = 0;
        this.idInsumo = idInsumo;
        this.idPrenda = idPrenda;
        this.cantidadInsumo = cantidadInsumo;
        this.fecha = null;
    }

    public int getFolio(){
        return folio; 
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getIdInsumo(){
        return idInsumo; 
    }

    public void setIdInsumo(String idInsumo) {
        this.idInsumo = idInsumo;
    }

    public int getIdPrenda(){
        return idPrenda; 
    }

    public void setIdPrenda(int idPrenda) {
        this.idPrenda = idPrenda;
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

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
