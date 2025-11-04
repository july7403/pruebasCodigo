package EfectosTerreno;

import UnidadPackage.Unidad;

public interface EfectoTerreno {
    //public void aplicar(Unidad unidad) {}
    //public void remover(Unidad unidad) {}


    boolean esTransitablePor(Unidad u);
    int movimientoEfectivo(Unidad u, int movimiento);
    int modificarAtk(Unidad u);
    int modificarDef(Unidad u);
    int modificarMgc(Unidad u);
    public void aplicarEfectoFinalTurno(Unidad unidad);
    int bonusDefensaTurnoRival(Unidad u, boolean esTurnoDelRival);
}
