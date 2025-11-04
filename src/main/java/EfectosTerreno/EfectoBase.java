package EfectosTerreno;
import UnidadPackage.Unidad;
public abstract class EfectoBase implements EfectoTerreno{
    protected final boolean transitable;
    protected EfectoBase(boolean transitable) { this.transitable = transitable; }

    @Override public boolean esTransitablePor(Unidad u) { return transitable; }
    @Override public int movimientoEfectivo(Unidad u, int movBase) { return movBase; }
    @Override public int modificarAtk(Unidad u) { return 0; }
    @Override public int modificarDef(Unidad u) { return 0; }
    @Override public int modificarMgc(Unidad u) { return 0; }
    @Override public void aplicarEfectoFinalTurno(Unidad u) {
    }
    @Override public int bonusDefensaTurnoRival(Unidad u, boolean esTurnoDelRival) {
            return 0;
        }




}
