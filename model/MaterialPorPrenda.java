package model;

public class MaterialPorPrenda {
    private String idPrenda;
    private String idMateriaPrima;
    private double cantidad;

    public MaterialPorPrenda(String idPrenda, String idMateriaPrima, double cantidad) {
        this.idPrenda = idPrenda;
        this.idMateriaPrima = idMateriaPrima;
        this.cantidad = cantidad;
    }

    public String getIdPrenda() {
        return idPrenda;
    }

    public String getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}
