package Controladores;

import Mapa.Mapa;
import Mapa.Celda;
import UnidadPackage.Unidad;

import java.util.Set;

public class GestorMovimientosBasico implements GestorMovimientos {

    @Override
    public boolean validarMovimiento(Unidad unidad, Mapa mapa, Integer destinoX, Integer destinoY) {
        return false;
    }

    @Override
    public void moverUnidad(Unidad unidad, Mapa mapa, Integer destinoX, Integer destinoY) {

    }

    @Override
    public Set<Celda> obtenerMovimientosPosibles(Unidad unidad, Mapa mapa) {
        return Set.of();
    }
}
