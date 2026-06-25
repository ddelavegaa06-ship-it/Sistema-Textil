package model;

import java.time.LocalDate;
import java.util.List;

public class ConjuntoVendido {
    private String idVenta;
    private String nombreConjunto;
    private int cantidad;
    private String tipoVenta;
    private double precioUnitario;
    private LocalDate fechaVenta;
    private LocalDate fechaLimiteDevolucion;
    private String descripcion;
    private List<String> nombresPrendas;

    public ConjuntoVendido(String idVenta, String nombreConjunto, int cantidad,
                           String tipoVenta, double precioUnitario,
                           LocalDate fechaVenta, LocalDate fechaLimiteDevolucion,
                           String descripcion, List<String> nombresPrendas) {
        this.idVenta = idVenta;
        this.nombreConjunto = nombreConjunto;
        this.cantidad = cantidad;
        this.tipoVenta = tipoVenta;
        this.precioUnitario = precioUnitario;
        this.fechaVenta = fechaVenta;
        this.fechaLimiteDevolucion = fechaLimiteDevolucion;
        this.descripcion = descripcion;
        this.nombresPrendas = nombresPrendas;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public String getNombreConjunto() {
        return nombreConjunto;
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

    public List<String> getNombresPrendas() {
        return nombresPrendas;
    }

    public void setNombreConjunto(String nombreConjunto) {
        this.nombreConjunto = nombreConjunto;
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

    public void setNombresPrendas(List<String> nombresPrendas) {
        this.nombresPrendas = nombresPrendas;
    }
}
