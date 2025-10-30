package UnidadPackage;

import EquipamientoPackage.Equipamiento;
import Mapa.Mapa;

public abstract class Unidad {
    private Integer id;
    private Estadistica estadisticas;
    private Integer vida;
    private Equipamiento equipamiento;
    private Integer posicionX;
    private Integer posicionY;
    private boolean oculta;
    private boolean eliminada;
    public Unidad(Integer id,Integer vida, Estadistica estadisticas, Equipamiento equipamiento){
        this.id = id;
        this.vida = vida;
        this.estadisticas = estadisticas;
        this.equipamiento = equipamiento;
        this.oculta = false;
        this.eliminada = false;
    }
    public Integer getId() {
        return id;
    }
    public Integer getVida() {
        return vida;
    }
    public Estadistica getEstadisticas() {
        return estadisticas;
    }
    public Equipamiento getEquipamiento() {
        return equipamiento;
    }
    public Integer getPosicionX() {
        return posicionX;
    }
    public Integer getPosicionY() {
        return posicionY;
    }
    public boolean estaOculta() {
        return oculta;
    }
    public boolean estaEliminada() {
        return eliminada;
    }

    public void moverA(Integer posicionX,Integer posicionY, Mapa mapa){
        if (eliminada) throw new IllegalArgumentException("La unidad ya esta eliminada");
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }
    public void recibirDanio(int danio){
        if (eliminada) return;
        this.vida -= danio;
        if (this.vida <= 0){
            vida = 0;
            eliminada = true;
        }

    }
    public void equipar(Equipamiento equipamiento){
        this.equipamiento = equipamiento;
    }
        


}