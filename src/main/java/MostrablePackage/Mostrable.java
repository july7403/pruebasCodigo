package MostrablePackage;

import Jugador.Jugador;
import Mapa.Mapa;
import UnidadPackage.Unidad;

import java.util.List;

public interface Mostrable {
    //Depende de quien sea el turno actual, como se va a mostrar el mapa
    void mostrarMapa(Mapa mapa, Jugador jugadorActual);
    //mostraria la informacion necesaria del jugador actual (unidades, turnos y demas)
    void mostrarJugador(Jugador jugadorActual);
    //recibe una unidad y muestra sus estaditicas estado y demas
    void mostrarUnidad(Unidad unidad);
    void mostrarMensaje(String mensaje);
    void mostrarGanador(Jugador ganador);
    void mostrarOpciones(List<String> opciones);
}
