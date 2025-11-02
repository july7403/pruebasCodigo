package EquipamientoPackage;

import UnidadPackage.Unidad;


public abstract class Equipamiento {
    protected String nombre;
    protected Integer rango;
    protected Integer atk; // ataque físico que se le suma a unidad
    protected Integer mgc; // magia que se le suma a unidad
    protected Integer usosMaximos;
    protected Integer usosRestantes;
    protected IEstrategiaEquipamiento estrategia;

    public Equipamiento(String nombre, Integer rango, Integer atk, Integer mgc, Integer usosMaximos, IEstrategiaEquipamiento estrategia) {
        this.nombre = nombre;
        this.rango = rango;
        this.atk = atk;
        this.mgc = mgc;
        this.usosMaximos = usosMaximos;
        this.usosRestantes = usosMaximos;
        this.estrategia = estrategia;
    }


    public void usar(Unidad atacante, Unidad objetivo) {
        //if esta roto pero quiero encontrar otra logica que no sea if
        estrategia.ejecutar(atacante, objetivo);
        usosRestantes--;
    }

    public boolean estaRoto() {
        return usosRestantes <= 0;
    }

}