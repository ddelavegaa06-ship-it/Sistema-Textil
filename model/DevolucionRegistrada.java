package model;

import java.time.LocalDate;

public class DevolucionRegistrada {
    private String idDevolucion;
    private String nombreProducto;
    private String talla;
    private int cantidad;
    private String tipoVenta;
    private double precioUnitario;
    private LocalDate fechaDevolucion;
    private String motivo;

    public DevolucionRegistrada(String idDevolucion, String nombreProducto, String talla,
                                int cantidad, String tipoVenta, double precioUnitario,
                                LocalDate fechaDevolucion, String motivo) {
        this.idDevolucion = idDevolucion;
        this.nombreProducto = nombreProducto;
        this.talla = talla;
        this.cantidad = cantidad;
        this.tipoVenta = tipoVenta;
        this.precioUnitario = precioUnitario;
        this.fechaDevolucion = fechaDevolucion;
        this.motivo = motivo;
    }

    public String getIdDevolucion() {
        return idDevolucion;
    }

    public String getNombreProducto() {
        return nombreProducto;
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

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
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

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
