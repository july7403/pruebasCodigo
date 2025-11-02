package EquipamientoPackage;

public class Baculo extends Equipamiento {

    public Baculo(String nombre, Integer rango, Integer usosMaximos, IEstrategiaEquipamiento estrategia) {
        super(
                nombre,
                rango,
                0, // No tiene ATK
                0, // No tiene MGC
                usosMaximos,
                new EstrategiaCuracionHP() //habría que crear mas estrategias

        );
    }}
