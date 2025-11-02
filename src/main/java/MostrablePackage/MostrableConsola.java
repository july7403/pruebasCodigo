package MostrablePackage;

import Jugador.Jugador;
import Mapa.Mapa;
import UnidadPackage.Unidad;
import Jugador.Jugador;
import Mapa.Celda;
import java.util.List;

public class MostrableConsola implements Mostrable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String COLOR_OPONENTE_NEUTRO = "\u001B[37m";
    private static final String COLOR_ENREDADERA = "\u001B[42m";
    public void mostrarMapa(Mapa mapa, Jugador jugadorActual){
        int alto = mapa.getAlto();
        int ancho = mapa.getAncho();
        for (int y = 0; y < alto; y++){
            for (int x = 0; x<ancho;x++){
                Celda celda = mapa.getCelda(x,y);
                String salida = decidirSalida(celda,jugadorActual);
                System.out.println(salida);
            }
            System.out.println();
        }
    }

    private String decidirSalida(Celda celda, Jugador jugadorActual) {
        if (!celda.estaVacia()){
            Unidad unidad = celda.getUnidadActaul();
            //no romperia TDA ya que no le preguntamos para hacer otra accion sobre el mismo objeto despues
            if (!jugadorActual.tieneEstaUnidad(unidad) && celda.UnidadOculta() ){
                //TODO: para clase jugador el siguiente metodo
                String colorFondo = COLOR_ENREDADERA;
                String colorFrente = COLOR_OPONENTE_NEUTRO;
                String caracter = "X";
                return colorFondo+colorFrente+caracter+ANSI_RESET;
            }
            // si la unidad no esta oculta del enemigo o propia
            String colorFondo = celda.getColor();
            String caracter = unidad.getDisplay(); //TODO: hacer para cada tipo de unidad
            if (jugadorActual.tieneEstaUnidad(unidad)){
                return colorFondo + jugadorActual.getColor() + caracter + ANSI_RESET;
            }else{
                return colorFondo + COLOR_OPONENTE_NEUTRO + caracter + ANSI_RESET;
            }
        }else {
            String colorFondo = celda.getColor();
            String colorFrente = "\u001B[30m";
            return colorFondo + colorFrente +  " "  + ANSI_RESET;
        }
    }

    @Override
    public void mostrarUnidad(Unidad unidad) {

    }

    @Override
    public void mostrarMensaje(String mensaje) {

    }

    @Override
    public void mostrarOpciones(List<String> opciones) {

    }

    @Override
    public void mostrarGanador(Jugador ganador) {

    }

    @Override
    public void mostrarJugador(Jugador jugadorActual) {

    }
}
