package Controladores;

import Jugador.Jugador;

import java.util.List;
import java.util.Random;

public class ControladorTurnosTradicional implements ControladorTurnos {
    private List<Jugador> jugadores;
    private Integer indiceJugadorActual;
    private Integer numeroTurnos;
    private final Integer indiceJugadorInicial;

    public ControladorTurnosTradicional(List<Jugador> jugadores){
        if (!this.validarJugadores(jugadores)){
            throw new IllegalArgumentException("la lista de jugadores no puede estar vacia o tener menos de un jugador");
        }
        this.jugadores = jugadores;
        this.indiceJugadorActual = this.getJugadorInicial(jugadores);
        this.indiceJugadorInicial = this.indiceJugadorActual;
        this.numeroTurnos = 1;
    }

    private Integer getJugadorInicial(List<Jugador> jugadores) {
        Random random = new Random();
        return random.nextInt(this.jugadores.size());
    }

    private boolean validarJugadores(List<Jugador> jugadores){
        return (jugadores != null && jugadores.size() > 1);
    }

    @Override
    public void avanzarTurno() {
        this.indiceJugadorActual = (this.indiceJugadorActual+1) % this.jugadores.size();
        if (this.indiceJugadorActual.equals(this.indiceJugadorInicial)){
            this.numeroTurnos++;
        }

    }

    @Override
    public Jugador obtenerjugadorActual() {
        return this.jugadores.get(this.indiceJugadorActual);
    }

    @Override
    public Integer obtenerTurnoActual() {
        return this.numeroTurnos;
    }
}
