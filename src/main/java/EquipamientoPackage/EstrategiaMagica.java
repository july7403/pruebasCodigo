package EquipamientoPackage;

import UnidadPackage.Unidad;

public class EstrategiaMagica implements IEstrategiaEquipamiento{
    @Override
    public void ejecutar(Unidad atacante, Unidad objetivo) {

        int mgcTotal = atacante.obtenerMgcTotal();
        int mgcDefensa = objetivo.obtenerDefensaMagicaTotal();
        int danio = mgcTotal - mgcDefensa;

        if (danio > 0) {
            objetivo.recibirDanio(danio);
        }
    }
}
