package EquipamientoPackage;

import UnidadPackage.Unidad;

public class EstrategiaCuerpoACuerpo implements IEstrategiaEquipamiento {
   //le paso la responsabilidad a Cada estrategia

    @Override
    public void ejecutar(Unidad atacante, Unidad objetivo) {
            int atkTotal = atacante.obtenerAtkTotal();
            int defTotal = objetivo.obtenerDefensaTotal();
            int danio = atkTotal - defTotal;

            if (danio > 0) {
                objetivo.recibirDanio(danio);
            }

    }
}
