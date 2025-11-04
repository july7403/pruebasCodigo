package EfectosTerreno;

import UnidadPackage.Unidad;

public final class EfectoPantano extends EfectoBase {

    public EfectoPantano() {
        super(true);
    }

    @Override
    public int movimientoEfectivo(Unidad u, int movBase) {
        return 1;
    }



}
