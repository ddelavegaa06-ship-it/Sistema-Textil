package model;

import java.util.List;

public class VentaCompleta {
    private Venta venta;
    private List<DetalleVentaPrenda> detallesPrenda;
    private List<DetalleVentaConjunto> detallesConjunto;

    public VentaCompleta(Venta venta, List<DetalleVentaPrenda> detallesPrenda, List<DetalleVentaConjunto> detallesConjunto) {
        this.venta = venta;
        this.detallesPrenda = detallesPrenda;
        this.detallesConjunto = detallesConjunto;
    }

    public Venta getVenta() {
        return venta;
    }

    public List<DetalleVentaPrenda> getDetallesPrenda() {
        return detallesPrenda;
    }

    public List<DetalleVentaConjunto> getDetallesConjunto() {
        return detallesConjunto;
    }
}
