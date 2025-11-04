package Mapa;
import UnidadPackage.Unidad;
import EfectosTerreno.EfectoTerreno;

public class Terreno{
    private String nombre;
    private boolean esTransitable;
    private EfectoTerreno efecto;

    public Terreno(String nombre, boolean esTransitable, EfectoTerreno efecto) {
        this.nombre = nombre;
        this.esTransitable = esTransitable;
        this.efecto = efecto;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public boolean isEsTransitable() {
        return esTransitable;
    }
    public void setEsTransitable(boolean esTransitable) {
        this.esTransitable = esTransitable;
    }
    public EfectoTerreno getEfecto() {
        return efecto;
    }
    public void setEfecto(EfectoTerreno efecto) {
        this.efecto = efecto;
    }
//    public boolean esTransitable(Unidad u) {
//        return efecto.esTransitablepor(u);
//
//    }
    public int movimientoEfectivo(Unidad u, int movimiento) {
        return efecto.movimientoEfectivo(u, movimiento);
    }
    public int modificarAtk(Unidad u) {
        return efecto.modificarAtk(u);
    }
    public int modificarDef(Unidad u) {
        return efecto.modificarDef(u);
    }
    public int modificarMgc(Unidad u) {
        return efecto.modificarMgc(u);
    }
    public void aplicarEfectoFinalTurno(Unidad u) {
         efecto.aplicarEfectoFinalTurno(u);
    }
}
