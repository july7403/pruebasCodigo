package Mapa;

import EfectosTerreno.EfectoTerreno;
import UnidadPackage.Unidad;

public class Celda {
    private Integer x;
    private Integer y;
    private String nombre;
    private boolean esTransitable;
    private boolean unidadOculta;
    private EfectoTerreno efectoTerreno;
    private Unidad unidadActaul = null;

    public Celda(String nombre, boolean esTransitable, EfectoTerreno efectoTerreno, int Y, int X) {
        this.nombre = nombre;
        this.esTransitable = esTransitable;
        this.efectoTerreno = efectoTerreno;
        this.y = Y;
        this.x = X;
    }

    public boolean estaVacia() {
        return this.unidadActaul == null; //fix: deberia ser true si esta vacio
    }
    public Unidad getUnidadActaul(){
        return this.unidadActaul;
    }

    /**
     * Si es pocible coloca la unidad en la casilla, caso contrario lanza excepcion
     * @param unidad Unidad a colocar
     */
    public void colocarUnidad(Unidad unidad){
        if (!this.estaVacia()){
            throw new IllegalArgumentException("La casilla ya esta ocupada por una unidad");
        }
        this.unidadActaul=unidad;
        //aplicarEfecto(this.unidadActual);
    }
    public Unidad removerUnidad(){
        if(!this.estaVacia()){
            Unidad unidadRemovida = this.getUnidadActaul();
            //quitarEfecto(unidadRemovida)
            this.unidadActaul = null;
            return unidadRemovida;
        }
        return null;
    }
    public boolean esTransitable(){return this.esTransitable;}
    public Integer getX(){return this.x;}
    public Integer getY(){return this.y;}
    public String getColor(){
        return this.efectoTerreno.getColorFondo();
    }
    public boolean UnidadOculta(){return this.unidadOculta;}
}
