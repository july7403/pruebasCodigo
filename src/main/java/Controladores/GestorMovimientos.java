package Controladores;

import Mapa.Mapa;
import Mapa.Celda;
import UnidadPackage.Unidad;
import java.util.Set;

public interface GestorMovimientos {
    /**
     * Valida si la unidad puede moverse a la casilla indicada en (X,Y)
     *
     * @param unidad
     * @param mapa
     * @param destinoX
     * @param destinoY
     * @return booleano indicando si puede moverse a esa ubicacion o no
     */
    boolean validarMovimiento(Unidad unidad, Mapa mapa, Integer destinoX, Integer destinoY);

    /**
     * Devuelve las celdas a las que la unidad puede moverse
     *
     * @param unidad
     * @param mapa
     * @return
     */
    Set<Celda> obtenerMovimientosPosibles(Unidad unidad, Mapa mapa);

    /**
     * Mueve a la unidad a la celda indicada por el usuario
     * @param unidad
     * @param mapa
     * @param destinoX
     * @param destinoY
     */
    void moverUnidad(Unidad unidad, Mapa mapa, Integer destinoX, Integer destinoY);
}
