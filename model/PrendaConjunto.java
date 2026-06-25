package model;

public class PrendaConjunto {
    private int id;
    private int idPrenda;
    private int idConjunto;
    public PrendaConjunto(int id, int idPrenda, int idConjunto){
        this.id = id;
        this.idPrenda = idPrenda;
        this.idConjunto = idConjunto;
    }
    public int getId(){
        return id;
    }
    public int getIdPrenda(){
        return idPrenda;
    }
    public int getIdConjunto(){
        return idConjunto;
    }
}
