package EquipamientoPackage;

public class ArmaCuerpoACuerpo extends Equipamiento {

    public ArmaCuerpoACuerpo(String nombre, Integer atk, Integer usosMaximos) {
        super(
                nombre,
                1, // siempre 1 para armas cuerpo a cuerpo
                atk,
                0, // No tiene boost mágico
                usosMaximos,
                new EstrategiaCuerpoACuerpo());
    }}