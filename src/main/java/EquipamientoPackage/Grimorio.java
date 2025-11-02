package EquipamientoPackage;

public class Grimorio extends Equipamiento {
    public Grimorio(String nombre, Integer rango, Integer mgc, Integer usosMaximos) {
        super(
                nombre,
                rango,
                0, // No tiene ATK físico
                mgc,
                usosMaximos,
                new EstrategiaCuracionHP()
        );
    }

}
