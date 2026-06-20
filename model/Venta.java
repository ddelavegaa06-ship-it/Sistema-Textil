package model;
import java.time.LocalDate;
public class Venta {
    private int folio;
    private LocalDate fecha;

    public Venta(int folio, LocalDate fecha){
        this.folio = folio;
        this.fecha = fecha;
    }
    public Venta(int folio){
        this.folio = folio;
        this.fecha = null;
    }
    public int getFolio(){
        return folio;
    }
    public LocalDate getFecha(){
        return fecha;
    }
}
