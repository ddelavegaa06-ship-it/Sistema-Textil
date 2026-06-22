package model;

public class Insumo {
    private String id;
    private String numeroPartida;
    private double existencia;
    private String tipoExistencia;
    private String descripcion;
    private String nombre;
    private String color;
    private double medida;
    private double ancho;
    private String composicion;
    private String tipo;
    private int no;
    private String tamanio;
    private double talla;
    private String material;
    private String tipoInsumo;
    private Integer idUbicacion;
    private int minimoExistencia = 10;

    public Insumo() {}

    public Insumo(String id, String numeroPartida, double existencia, String tipoExistencia, String descripcion, String nombre, String color, double medida, double ancho, String composicion, String tipo, int no, String tamanio, double talla, String material, String tipoInsumo, Integer idUbicacion) {
        this.id = id;
    }

    public Insumo(int id, String numeroPartida, int existencia, String tipoExistencia,
                  String descripcion, String nombre, String color, String medidaStr,
                  Double ancho, String composicion, String tipo, Integer no,
                  String tamanio, String tallaStr, String material, String tipoInsumo) {
        this.id = String.valueOf(id);
        this.numeroPartida = numeroPartida;
        this.existencia = existencia;
        this.tipoExistencia = tipoExistencia;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.color = color;
        try {
            this.medida = medidaStr != null && !medidaStr.isEmpty() ? Double.parseDouble(medidaStr) : 0.0;
        } catch (NumberFormatException e) {
            this.medida = 0.0;
        }
        this.ancho = ancho != null ? ancho : 0.0;
        this.composicion = composicion;
        this.tipo = tipo;
        this.no = no != null ? no : 0;
        this.tamanio = tamanio;
        try {
            this.talla = tallaStr != null && !tallaStr.isEmpty() ? Double.parseDouble(tallaStr) : 0.0;
        } catch (NumberFormatException e) {
            this.talla = 0.0;
        }
        this.material = material;
        this.tipoInsumo = tipoInsumo;
        this.idUbicacion = 1;
        this.numeroPartida = numeroPartida;
        this.existencia = existencia;
        this.tipoExistencia = tipoExistencia;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.color = color;
        this.medida = medida;
        this.ancho = ancho;
        this.composicion = composicion;
        this.tipo = tipo;
        this.no = no;
        this.tamanio = tamanio;
        this.talla = talla;
        this.material = material;
        this.tipoInsumo = tipoInsumo;
        this.idUbicacion = idUbicacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroPartida() {
        return numeroPartida;
    }

    public void setNumeroPartida(String numeroPartida) {
        this.numeroPartida = numeroPartida;
    }

    public double getExistencia() {
        return existencia;
    }

    public void setExistencia(double existencia) {
        this.existencia = existencia;
    }

    public String getTipoExistencia() {
        return tipoExistencia;
    }

    public void setTipoExistencia(String tipoExistencia) {
        this.tipoExistencia = tipoExistencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getMedida() {
        return medida;
    }

    public void setMedida(double medida) {
        this.medida = medida;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public double getTalla() {
        return talla;
    }

    public void setTalla(double talla) {
        this.talla = talla;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTipoInsumo() {
        return tipoInsumo;
    }

    public void setTipoInsumo(String tipoInsumo) {
        this.tipoInsumo = tipoInsumo;
    }

    public Integer getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }
}
