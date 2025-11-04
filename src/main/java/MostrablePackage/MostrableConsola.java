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
            System.out.print("|");
            for (int x = 0; x<ancho;x++){
                Celda celda = mapa.getCelda(x,y);
                String salida = decidirSalida(celda,jugadorActual);
                System.out.print(salida);
            }
            System.out.println();
        }
    }

    private String decidirSalida(Celda celda, Jugador jugadorActual) {
        String caracter = " ";
        String relleno = " ";
        if (!celda.estaVacia()){
            Unidad unidad = celda.getUnidadActaul();
            if (!jugadorActual.tieneEstaUnidad(unidad) && celda.UnidadOculta() ){

                String colorFondo = COLOR_ENREDADERA;
                String colorFrente = COLOR_OPONENTE_NEUTRO;
                caracter = "X";
                return colorFondo+colorFrente+relleno+caracter+relleno+ANSI_RESET;
            }
            // si la unidad no esta oculta del enemigo o propia
            String colorFondo = celda.getColor();
             caracter = unidad.getDisplay();
            if (jugadorActual.tieneEstaUnidad(unidad)){
                return colorFondo + jugadorActual.getColor()+relleno+  caracter+relleno + ANSI_RESET;
            }else{
                return colorFondo + COLOR_OPONENTE_NEUTRO +relleno+ caracter +relleno+ ANSI_RESET;
            }
        }else {
            String colorFondo = celda.getColor();
            String colorFrente = "\u001B[30m";
            return colorFondo + colorFrente +relleno+" "+relleno  + ANSI_RESET;
        }
    }

    @Override
    public void mostrarUnidad(Unidad unidad) {
        //TODO: ampliar con mas informacion, quizas se pued dividir en dos metodos
        // version resumida o version completa (uno muestra informacion importante y el otro
        // informacion completa de la unidad)
        System.out.println("-- Unidad: "+getNombreUnidad(unidad)+ "Id: "+unidad.getId());
        System.out.println("-- HP: "+unidad.getVida());
        System.out.println("------------------------------------------");
    }

    private String getNombreUnidad(Unidad unidad) {
        String display = unidad.getDisplay();
        return switch (display) {
            case "L" -> "Lord";
            case "U" -> "Unidad Basica";
            default -> throw new IllegalArgumentException("Unidad no reconocida");
        };
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void mostrarOpciones(List<String> opciones) {
        //lista las opciones disponibles (como Atacar,Dormir,Mover o las direcciones en las que se puede mover)
        System.out.println("\n--- Opciones Disponibles ---");
        for (int i = 0; i<opciones.size();i++){
            System.out.println(" ["+(i+1)+"] "+ opciones.get(i));
        }
        System.out.println(">>> Ingrese el número de la opción:");
    }

    @Override
    public void mostrarGanador(Jugador ganador) {
        System.out.println("\n*********************************************");
        System.out.println(" ¡FELICIDADES! EL GANADOR ES: " + ganador.getNombre());
        System.out.println("*********************************************");
    }

    @Override
    public void mostrarJugador(Jugador jugadorActual) {
        //esto seria informacion que se muestra antes del turno de un jugador
        System.out.println("\n=============================================");
        System.out.println(" TURNO DEL JUGADOR: " + jugadorActual.getNombre());
        System.out.println("  Unidades vivas: " + jugadorActual.contarUnidadesVivas());
        System.out.println("=============================================");
    }
}
