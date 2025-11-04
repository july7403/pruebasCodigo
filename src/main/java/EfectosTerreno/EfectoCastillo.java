package EfectosTerreno;

import UnidadPackage.Unidad;

public class EfectoCastillo extends EfectoBase{
    public EfectoCastillo() {
        super(true);
    }

    @Override
    public int modificarDef(Unidad u) {
        return 1;
    }

    @Override
    public void aplicarEfectoFinalTurno(Unidad u) {
        super.aplicarEfectoFinalTurno(u);
    }
    @Override
    public int bonusDefensaTurnoRival(Unidad u, boolean esTurnoDelRival) {
        return esTurnoDelRival ? 2 : 0;
    }
}
