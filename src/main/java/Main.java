import Builders.*;
import Controladores.ControladorTurnos;
import Controladores.ControladorTurnosTradicional;
import Controladores.GestorMovimientos;
import Controladores.GestorMovimientosBasico;
import InterfazUsuario.InterfazConsola;
import InterfazUsuario.InterfazUsuario;
import Juego.Juego;
import Jugador.Jugador;
import Mapa.Mapa;
import MostrablePackage.Mostrable;
import MostrablePackage.MostrableConsola;

import java.io.InputStream;
import java.util.List;

public class Main {
        public static void main(String[] args) {
            System.out.println("Hola, mundo!");
            //inicializacion de instancias
            InterfazUsuario inputsConsola = new InterfazConsola();
            Mostrable mostrarConsola = new MostrableConsola();
            MapaFactory mapaFactory = new MapaFactoryTexto();
            IEjercitoFactory ejercitoFactory = new EjercitoFactoryTexto();
            ProcesadorMapa procesadorMapa = new ProcesadorMapa(mapaFactory);

            //obtener Datos iniciales

            //Para Mapa
            mostrarConsola.mostrarMensaje("Ingrese ruta al archivo inicial del mapa (CSV): ");
            InputStream inputMapa = inputsConsola.pedirRutaArchivo();
            Mapa mapa = procesadorMapa.procesarMapa(inputMapa);
            //Para obtener Jugadores y Ejercito Inical:
            mostrarConsola.mostrarMensaje("Ingrese ruta al archivo de los ejercitos (CSV): ");
            InputStream inputEjercito = inputsConsola.pedirRutaArchivo();
            List<Jugador> jugadores = ejercitoFactory.procesarEjecito(inputEjercito);
            //TODO:logica de set up inicial (crear clase setup o hacerlo en este mismo archivo)

            //Creacion de gestores
            GestorMovimientos gestorMovimientos = new GestorMovimientosBasico();
            ControladorTurnos controladorTurnos = new ControladorTurnosTradicional(jugadores);
            Juego juego = new Juego(
                    mapa,
                    jugadores,
                    controladorTurnos,
                    gestorMovimientos,
                    inputsConsola,
                    mostrarConsola
            );
            //TODO:juego.iniciarPartida();
        }

}
