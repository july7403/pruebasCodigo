package Controladores;

import EquipamientoPackage.Equipamiento;
import UnidadPackage.Unidad;
import Mapa.Mapa;
import java.util.ArrayList;
import java.util.List;

//pendiente
public class GestorCombate {

    private final List<Unidad> unidadesQueActuaronEsteTurno;

    public GestorCombate() {

        this.unidadesQueActuaronEsteTurno = new ArrayList<>();
    }

    public boolean intentarAtaque(Unidad atacante, Unidad enemigo) {

        if (!validarUnidadNoActuo(atacante) ) {
            throw new IllegalStateException("Esta Unidad Ya Atacó ");
        }

        registrarAccion(atacante);
        atacante.atacar(enemigo);
        return true;
    }



    private boolean validarUnidadNoActuo(Unidad unidad) {
        for (Unidad u : unidadesQueActuaronEsteTurno) {
            if (u.esLaMisma(unidad)) {
                return false;
            }
        }
        return true;
    }

    private int calcularDistancia(Unidad unidad1, Unidad unidad2) {
        int deltaX = Math.abs(unidad1.getPosicionX() - unidad2.getPosicionX());
        int deltaY = Math.abs(unidad1.getPosicionY() - unidad2.getPosicionY());

        return Math.max(deltaX, deltaY);
    }

    private void registrarAccion(Unidad unidad) {
        unidadesQueActuaronEsteTurno.add(unidad);
    }

}