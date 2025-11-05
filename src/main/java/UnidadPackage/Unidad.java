package UnidadPackage;

import EquipamientoPackage.Equipamiento;
import EquipamientoPackage.PuñoLimpio;
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

    public Equipamiento getEquipamiento() {
        if (this.equipamiento==null){
            return PuñoLimpio.obtenerInstancia();
        }
        return this.equipamiento;
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

    //Apartado estadisticas:
    public int getAtk(){return this.estadisticas.getAtk();}
    public int getDef(){return this.estadisticas.getDef();}
    public int getMgc(){return this.estadisticas.getMgc();}
    public int getMov(){return this.estadisticas.getMov();}

    public void moverA(Integer posicionX,Integer posicionY, Mapa mapa){
        if (eliminada) throw new IllegalArgumentException("La unidad ya esta eliminada");
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    //pensar en aplicar observer
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
    public abstract String getDisplay();


    //Logica de atacar

    public void atacar(Unidad objetivo) {
        if (eliminada) {
            throw new IllegalStateException("Una unidad eliminada no puede atacar");
        }
        if (objetivo.estaEliminada()) {
            throw new IllegalArgumentException("No se puede atacar a una unidad eliminada");
        }
        equipamiento.usar(this, objetivo);

    }

    public void usarEquipamientoEn(Unidad aliado) {
        equipamiento.usar(this, aliado);
    }

    public int obtenerAtkTotal() {
        //int terr = mapa.terrenoEn(posicion).modificarAtk(this);
        //pensar si hay otra forma de hacerlo así no le pedimos a estadisticas que pase los datos
        int atkBase = estadisticas.getAtk();
        if (!equipamiento.estaRoto()) {
            return atkBase + equipamiento.getAtk();
        }
        return atkBase;
    }

    public int obtenerMgcTotal() {
        //int mgcTerr = mapa.terrenoEn(posicion).modificarAtk(this)
        int mgcBase = estadisticas.getMgc();
        if (!equipamiento.estaRoto()) {
            return mgcBase + equipamiento.getMgc();
        }
        return mgcBase;
    }

    public int obtenerDefensaMagicaTotal() {
        return estadisticas.getMgc();
    }

    public int obtenerDefensaTotal() {
        return estadisticas.getDef();
    }

    public boolean esLaMisma(Unidad unidad) {
        return false;
    }

    //pendiente pensar en a logica de los baculos


}

