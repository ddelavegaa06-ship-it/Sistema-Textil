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
    private int idUbicacion;

    public Insumo(String id, String numeroPartida, double existencia, String tipoExistencia, String descripcion, String nombre, String color, double medida, double ancho, String composicion, String tipo, int no, String tamanio, double talla, String material, String tipoInsumo, int idUbicacion){
        this.id = id; 
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
    public String getId(){
        return id;
    }  
    public String getNumeroPartida(){
        return numeroPartida;
    }   
    public double getExistencia(){
        return existencia;
    }   
    public void setExistencia(double nExistencia){
        existencia = nExistencia;
    }

    public String getTipoExistencia(){
        return tipoExistencia;
    }  
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String nDescripcion){
        descripcion = nDescripcion;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nNombre){
        nombre = nNombre;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String nColor){
        color = nColor;
    }
    public double getMedida(){
        return medida;
    }
    public void setMedida(double nMedida){
        medida = nMedida;
    }
    public double getAncho(){
        return ancho;
    }
    public String getComposicion(){
        return composicion;
    }
    public void setComposicion(String nComposicion){
        composicion = nComposicion;
    }
    public String getTipo(){
        return tipo;
    }
    public int getNo(){
        return no;
    }
    public String getTamanio(){
        return tamanio;
    }
    public double getTalla(){
        return talla;
    }
    public String getMaterial(){
        return material;
    }
    public String getTipoInsumo(){
        return tipoInsumo;
    }
    public void setTipoInsumo(String nTipoInsumo){
        tipoInsumo = nTipoInsumo;
    }
    public int getIdUbicacion(){
        return idUbicacion;
    }
    public void setIdUbicacion(int nIdUbicacion){
        idUbicacion = nIdUbicacion;
    }
    

}
