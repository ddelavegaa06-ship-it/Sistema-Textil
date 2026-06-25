package model;
import java.time.LocalDate;

public class DevolucionConjunto {
    private int id;
    private int folio;
    private int idConjunto;
    private LocalDate fecha;

    public DevolucionConjunto(int id, int folio, int idConjunto){
        this.id = id;
        this.folio = folio;
        this.idConjunto = idConjunto;
        this.fecha = null;
    }
    public DevolucionConjunto(int id, int folio, int idConjunto, LocalDate fecha){
        this.id = id;
        this.folio = folio;
        this.idConjunto = idConjunto;
        this.fecha = fecha;
    }

    public int getId(){
        return id;
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
