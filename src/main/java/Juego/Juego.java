package Juego;

import Controladores.ControladorTurnos;
import Controladores.GestorMovimientos;
import InterfazUsuario.InterfazUsuario;
import Jugador.Jugador;
import Mapa.Mapa;
import MostrablePackage.Mostrable;

import java.util.List;

public class Juego {
    private Mapa mapaJuego;
    private List<Jugador> jugadores;
    private ControladorTurnos controladorTurnos;
    private GestorMovimientos gestorMovimientos;
    private InterfazUsuario manejadorInputs; //maneja los inputs del usuario
    private Mostrable mostrable; //maneja solamente lo que se va a mostrar
    private boolean partidaFinalizada; //podria ser un ENUM o algo mas pero planteo inicial

    public Juego(Mapa mapa, List<Jugador> jugadores, ControladorTurnos controladorTurnos,
                 GestorMovimientos gestorMovimientos, InterfazUsuario interfazUsuario, Mostrable mostrable){
        this.mapaJuego = mapa;
        this.jugadores = jugadores;
        this.controladorTurnos = controladorTurnos;
        this.gestorMovimientos = gestorMovimientos;
        this.manejadorInputs = interfazUsuario;
        this.mostrable = mostrable;
        this.partidaFinalizada = false;
        //falta logica de setear lords para el patron Observer
    }
}
