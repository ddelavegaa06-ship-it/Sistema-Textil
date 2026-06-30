package model;

import java.time.LocalDate;

public class PrendaVendida {
    private String idVenta;
    private String nombrePrenda;
    private String talla;
    private int cantidad;
    private String tipoVenta;
    private double precioUnitario;
    private LocalDate fechaVenta;
    private LocalDate fechaLimiteDevolucion;
    private String descripcion;

    public PrendaVendida(String idVenta, String nombrePrenda, String talla, int cantidad,
                         String tipoVenta, double precioUnitario,
                         LocalDate fechaVenta, LocalDate fechaLimiteDevolucion, String descripcion) {
        this.idVenta = idVenta;
        this.nombrePrenda = nombrePrenda;
        this.talla = talla;
        this.cantidad = cantidad;
        this.tipoVenta = tipoVenta;
        this.precioUnitario = precioUnitario;
        this.fechaVenta = fechaVenta;
        this.fechaLimiteDevolucion = fechaLimiteDevolucion;
        this.descripcion = descripcion;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public String getNombrePrenda() {
        return nombrePrenda;
    }

    public String getTalla() {
        return talla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getTotal() {
        return cantidad * precioUnitario;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public LocalDate getFechaLimiteDevolucion() {
        return fechaLimiteDevolucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNombrePrenda(String nombrePrenda) {
        this.nombrePrenda = nombrePrenda;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setFechaLimiteDevolucion(LocalDate fechaLimiteDevolucion) {
        this.fechaLimiteDevolucion = fechaLimiteDevolucion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
