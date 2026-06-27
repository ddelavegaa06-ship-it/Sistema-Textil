package model;

import java.time.LocalDate;

public class VentaResumen {
    private int folioVenta;
    private LocalDate fechaVenta;
    private String tipoItem;
    private int idItem;
    private String nombreItem;
    private String talla;
    private int cantidadVendida;
    private double total;
    private String estado;

    public VentaResumen(int folioVenta, LocalDate fechaVenta, String tipoItem, int idItem,
                        String nombreItem, String talla, int cantidadVendida, double total, String estado) {
        this.folioVenta = folioVenta;
        this.fechaVenta = fechaVenta;
        this.tipoItem = tipoItem;
        this.idItem = idItem;
        this.nombreItem = nombreItem;
        this.talla = talla;
        this.cantidadVendida = cantidadVendida;
        this.total = total;
        this.estado = estado;
    }

    public int getFolioVenta() {
        return folioVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public int getIdItem() {
        return idItem;
    }

    public String getNombreItem() {
        return nombreItem;
    }

    public String getTalla() {
        return talla;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }
}
