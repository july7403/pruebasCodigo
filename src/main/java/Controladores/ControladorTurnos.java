package Controladores;

import Jugador.Jugador;

public interface ControladorTurnos {
    void avanzarTurno();
    Jugador obtenerjugadorActual();
    Integer obtenerTurnoActual();
}
