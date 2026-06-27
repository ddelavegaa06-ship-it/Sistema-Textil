package model;

import java.time.LocalDate;

public class DevolucionConjuntoVista {
    private int folioVenta;
    private String nombreConjunto;
    private LocalDate fechaDevolucion;
    private LocalDate fechaVenta;

    public DevolucionConjuntoVista(int folioVenta, String nombreConjunto, LocalDate fechaDevolucion, LocalDate fechaVenta) {
        this.folioVenta = folioVenta;
        this.nombreConjunto = nombreConjunto;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaVenta = fechaVenta;
    }

    public int getFolioVenta() {
        return folioVenta;
    }

    public String getNombreConjunto() {
        return nombreConjunto;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }
}
