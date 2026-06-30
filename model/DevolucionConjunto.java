package model;
import java.time.LocalDate;

public class DevolucionConjunto {
    private int folio;
    private int idConjunto;
    private LocalDate fecha;

    public DevolucionConjunto(int folio, int idConjunto){
        this.folio = folio;
        this.idConjunto = idConjunto;
        this.fecha = null;
    }

    public DevolucionConjunto(int folio, int idConjunto, LocalDate fecha){
        this.folio = folio;
        this.idConjunto = idConjunto;
        this.fecha = fecha;
    }

    public int getFolio(){
        return folio;
    }
    public int getIdConjunto(){
        return idConjunto;
    }
    public LocalDate getFecha(){
        return fecha;
    }
    public void setFecha(LocalDate nFecha){
        fecha = nFecha;
    }
}
