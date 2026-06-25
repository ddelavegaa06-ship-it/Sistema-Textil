package model;

public class ItemVenta {
    private String nombreProducto;
    private String tipoVenta;
    private int cantidad;
    private double precioUnitario;
    private Prenda prenda;
    private Conjunto conjunto;

    public ItemVenta(String nombreProducto, int cantidad, double precioUnitario,
                     String tipoVenta, Prenda prenda, Conjunto conjunto) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoVenta = tipoVenta;
        this.prenda = prenda;
        this.conjunto = conjunto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    public Prenda getPrenda() {
        return prenda;
    }

    public Conjunto getConjunto() {
        return conjunto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
