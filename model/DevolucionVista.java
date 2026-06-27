package model;

import java.time.LocalDate;

public class DevolucionVista {
    private int folioVenta;
    private String nombrePrenda;
    private String talla;
    private LocalDate fechaDevolucion;
    private LocalDate fechaVenta;

    public DevolucionVista(int folioVenta, String nombrePrenda, String talla, LocalDate fechaDevolucion, LocalDate fechaVenta) {
        this.folioVenta = folioVenta;
        this.nombrePrenda = nombrePrenda;
        this.talla = talla;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaVenta = fechaVenta;
    }

    public int getFolioVenta() {
        return folioVenta;
    }

    public String getNombrePrenda() {
        return nombrePrenda;
    }

    public String getTalla() {
        return talla;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }
}
