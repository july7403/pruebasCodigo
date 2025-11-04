import Builders.*;
import Controladores.ControladorTurnos;
import Controladores.ControladorTurnosTradicional;
import Controladores.GestorMovimientos;
import Controladores.GestorMovimientosBasico;
import InterfazUsuario.InterfazConsola;
import InterfazUsuario.InterfazUsuario;
import Jugador.Jugador;
import Mapa.Mapa;
import Mapa.Celda;
import MostrablePackage.Mostrable;
import MostrablePackage.MostrableConsola;
import UnidadPackage.Unidad;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
        public static void main(String[] args) {
            System.out.println("Hola, mundo!");
            //inicializacion de instancias
            InterfazUsuario inputsConsola = null;
            try {
                 inputsConsola = new InterfazConsola();
            } catch (IOException e) {
                System.err.println("Error al instanciar la interfaz de consola");
                e.printStackTrace();
                System.exit(1);
            }
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

            mostrarConsola.mostrarMapa(mapa,null);
            for (Jugador j: jugadores){
                mostrarConsola.mostrarJugador(j);
                List<Unidad> unidades = j.getUnidades();
                for (Unidad u: unidades){
                    mostrarConsola.mostrarUnidad(u);
                }
            }
            setUpNombresJugadores(jugadores,inputsConsola);
            setUpInicial(mapa,jugadores,mostrarConsola,inputsConsola);
            //TODO:juego.iniciarPartida();
        }

    /**
     * Matodo para settear los nombre iniciales de los jugadores del juego
     * @param jugadores
     * @param inputsConsola interfaz por la cula se puede pedir nombres a los usuarios
     */
    private static void setUpNombresJugadores(List<Jugador> jugadores, InterfazUsuario inputsConsola) {
        for (Jugador jugador : jugadores) {
            String nuevoNombre = inputsConsola.pedirNombreUsuario();
            jugador.setNombre(nuevoNombre);
        }
    }

    /**
     * Metodo que setea inicialmente las unidades de los jugadores y deja el mapa listo para jugar
     * @param mapa
     * @param jugadores
     * @param mostrarConsola
     * @param inputsConsola
     */
    private static void setUpInicial(Mapa mapa, List<Jugador> jugadores, Mostrable mostrarConsola, InterfazUsuario inputsConsola) {
        mostrarConsola.mostrarMensaje("--- INICIO DE COLOCACIÓN DE UNIDADES ---");

        int i = 0;
        for (Jugador jugadorActual : jugadores){
            mostrarConsola.mostrarJugador(jugadorActual);
            mostrarConsola.mostrarMapa(mapa,jugadorActual);
            setUpUnidadesJugador(mapa,jugadorActual,mostrarConsola,inputsConsola,i);
            i++;
            mostrarConsola.mostrarMapa(mapa,jugadorActual);
            }
        mostrarConsola.mostrarMensaje("--- COLOCACIÓN FINALIZADA. COMIENZA LA PARTIDA. ---");
        }

    private static void setUpUnidadesJugador(Mapa mapa, Jugador jugadorActual, Mostrable mostrarConsola, InterfazUsuario inputsConsola,int indiceJugador) {
        int anchoMapa = mapa.getAncho();
        int limiteX = anchoMapa / 2;

        for (Unidad unidad: jugadorActual.getUnidades()){
            mostrarConsola.mostrarMensaje("Colocando unidad "+ getNombreUnidad(unidad));
            mostrarConsola.mostrarUnidad(unidad);
            Celda celdaSeleccionada = obtenerCelda(mapa,inputsConsola,jugadorActual,limiteX,mostrarConsola);
            celdaSeleccionada.colocarUnidad(unidad);
        }
        }

    private static Celda obtenerCelda(Mapa mapa, InterfazUsuario inputsConsola, Jugador jugadorActual, int limiteX, Mostrable mostrarConsola) {
            //TODO: continuar metodo, debe solicitar una celda que este
            // en la mitad que le corresponde a este jugador
            return null;
    }

    private static String getNombreUnidad(Unidad unidad) {
        String display = unidad.getDisplay();
        return switch (display) {
            case "L" -> "Lord";
            case "U" -> "Unidad Basica";
            default -> throw new IllegalArgumentException("Unidad no reconocida");
        };
    }
}
