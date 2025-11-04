package EfectosTerreno;

import UnidadPackage.Unidad;

public class EfectoAreaContaminada extends EfectoBase{
   public EfectoAreaContaminada() {
       super(true);
   }

    @Override
    public void aplicarEfectoFinalTurno(Unidad u) {
        u.recibirDanio(1);
    }
}
