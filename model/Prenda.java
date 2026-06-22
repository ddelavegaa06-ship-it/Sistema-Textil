package model;

public class Prenda {
    private int id;
    private String nombre;
    private String talla;
    private int existencia;
    private double precioMayoreo;
    private double precioMenudeo;
    private Integer idTienda;
    private String codigoBarras;
    private int minimoExistencia = 5;

    public Prenda() {}

    public Prenda(int id, String nombre, String talla, int existencia, double precioMayoreo, double precioMenudeo, Integer idTienda, String codigoBarras) {
        this.id = id;
        this.nombre = nombre;
        this.talla = talla;
        this.existencia = existencia;
        this.precioMayoreo = precioMayoreo;
        this.precioMenudeo = precioMenudeo;
        this.idTienda = idTienda;
        this.codigoBarras = codigoBarras;
    }

    public Prenda(String nombre, String idStr, String talla, String tipoPrenda, int existencia, double precioMayoreo, double precioMenudeo, String idTiendaStr, String descripcion) {
        this.nombre = nombre;
        this.talla = talla;
        this.existencia = existencia;
        this.precioMayoreo = precioMayoreo;
        this.precioMenudeo = precioMenudeo;
        this.codigoBarras = "";
        try {
            this.id = idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : 0;
        } catch (NumberFormatException e) {
            this.id = 0;
        }
        try {
            this.idTienda = idTiendaStr != null && !idTiendaStr.isEmpty() ? Integer.parseInt(idTiendaStr.replaceAll("[^0-9]", "")) : 1;
        } catch (NumberFormatException e) {
            this.idTienda = 1;
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public double getPrecioMayoreo() {
        return precioMayoreo;
    }

    public void setPrecioMayoreo(double precioMayoreo) {
        this.precioMayoreo = precioMayoreo;
    }

    public double getPrecioMenudeo() {
        return precioMenudeo;
    }

    public void setPrecioMenudeo(double precioMenudeo) {
        this.precioMenudeo = precioMenudeo;
    }

    public Integer getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(Integer idTienda) {
        this.idTienda = idTienda;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getMinimoExistencia() {
        return minimoExistencia;
    }

    public void setMinimoExistencia(int minimoExistencia) {
        this.minimoExistencia = minimoExistencia;
    }

    public String getTipoPrenda() {
        if (nombre == null) return "Uniforme";
        String n = nombre.toLowerCase();
        if (n.contains("polo") || n.contains("playera") || n.contains("casual")) {
            return "Casual";
        }
        return "Uniforme";
    }

    public String getDescripcion() {
        if (nombre == null) return "";
        String desc = nombre;
        if (talla != null) {
            String tStr = talla.toLowerCase();
            if (tStr.equals("ch")) desc += " talla chica";
            else if (tStr.equals("m")) desc += " talla mediana";
            else if (tStr.equals("g")) desc += " talla grande";
            else if (tStr.equals("xl")) desc += " talla extra grande";
            else desc += " talla " + talla;
        }
        return desc;
    }
}
