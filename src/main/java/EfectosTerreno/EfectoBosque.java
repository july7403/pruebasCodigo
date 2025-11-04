package EfectosTerreno;

import UnidadPackage.Unidad;

public class EfectoBosque extends EfectoBase{
    public EfectoBosque() {
        super(true);
    }

    @Override
    public int modificarAtk(Unidad u) {
        return 1;
    }
    public int modificarDef(Unidad u) {
        return 1;
    }


}
