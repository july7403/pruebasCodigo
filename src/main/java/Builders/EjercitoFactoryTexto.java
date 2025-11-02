package Builders;

import EquipamientoPackage.Baculo;
import EquipamientoPackage.Equipamiento;
import Jugador.Jugador;
import EquipamientoPackage.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import UnidadPackage.*;

/* Ejemplo de archivo leido:
    1   TipoUnidad,Jugador,ATK,DEF,MGC,MOV,HP,ArmaInicial,TipoEstrategiaArma
    2   LORD,J1,35,30,10,15,1,ESPADA,D_FISICO
    3   LORD,J2,30,25,15,20,2,GRIMORIO,D_MAGICO
 */

public class EjercitoFactoryTexto implements IEjercitoFactory{
    private static Integer idUnidades = 1;
    /**
     * Toma una ruta pasada por parametro y devuelve una lista de jugadores con sus ejercitos inicializados
     * @param dataEjercito una ruta al archivo csv con la configuracion inicial del ejecito
     */
    @Override
    public List<Jugador> procesarEjecito(InputStream dataEjercito) {
        Map<String,List<Unidad>> unidadesPorJugador = new HashMap<>();
        try(BufferedReader lector = new BufferedReader(new InputStreamReader(dataEjercito))){
            String linea;
            // Linea cabecera no cuenta
            lector.readLine();
            while((linea=lector.readLine())!=null){
                String[] datos = linea.split(",");
                String codigoJugador = datos[1].trim();
                Unidad unidad = crearUnidad(datos);
                unidadesPorJugador
                        .computeIfAbsent(codigoJugador, k->new ArrayList<>()) //crea una nueva entrada si no esta presente ya el jugador
                        .add(unidad);
            }

        }catch (IOException e){
            throw new RuntimeException("Error al leer el input para procesar Ejercito");
        }
        return ensamblarJugadores(unidadesPorJugador);
    }

    private List<Jugador> ensamblarJugadores(Map<String, List<Unidad>> unidadesPorJugador) {
        List<Jugador> jugadores = new ArrayList<>();

        String[] coloresDisponibles = {"Rojo", "Azul", "Verde", "Amarillo", "Negro", "Blanco"};
        int indiceColor = 0;

        for (Map.Entry<String, List<Unidad>> entry : unidadesPorJugador.entrySet()) {
            String nombreJugador = entry.getKey().trim();
            List<Unidad> unidadesJugador = entry.getValue();

            String colorAsignado = coloresDisponibles[indiceColor % coloresDisponibles.length];
            indiceColor++;

            Jugador jugador = new Jugador(nombreJugador, unidadesJugador, colorAsignado);
            jugadores.add(jugador);
        }

        // Validación mínima
        if (jugadores.size() < 2) {
            throw new IllegalArgumentException("Debe haber al menos dos jugadores definidos en el archivo de ejército.");
        }

        return jugadores;
    }

    private Unidad crearUnidad(String[] datos) {
        //[0] = Tipo de unidad - [2] = ATK - [3] = DEF - [4] = MGC - [5] = MOV - [6] = HP
        //[7] = Equipamiento - [8] = estrategiaEquipamiento

        Integer atque = Integer.parseInt(datos[2]);
        Integer defensa = Integer.parseInt(datos[3]);
        Integer magia = Integer.parseInt(datos[4]);
        Integer movimiento = Integer.parseInt(datos[5]);
        Integer vida = Integer.parseInt(datos[6]);

        Estadistica estadisticasUnidad =
                new Estadistica(atque,defensa,magia,movimiento);
        Equipamiento arma = crearEquipamiento(datos[7],datos[8]);
        Integer id = idUnidades++;
        return nuevaUnidad(datos[0],id,vida,estadisticasUnidad,arma);
    }

    private Equipamiento crearEquipamiento(String nombreArma, String tipoEstrategia) {
        nombreArma = nombreArma.trim().toUpperCase();
        tipoEstrategia = tipoEstrategia.trim().toUpperCase();

        switch (nombreArma) {
            case "ESPADA":
            case "LANZA":
            case "HACHA":
                return new ArmaCuerpoACuerpo(nombreArma, 10, 20); // atk base 10, 20 usos

            case "BACULO":
                return new Baculo("Báculo de Curación", 3, 15, new EstrategiaCuracionHP());

            case "GRIMORIO":
                if (tipoEstrategia.equals("D_MAGICO")) {
                    return new Grimorio("Grimorio de Fuego", 3, 12, 15);
                } else {
                    return new Grimorio("Grimorio Básico", 3, 10, 15);
                }
            default:
                throw new IllegalArgumentException("Tipo de equipamiento no reconocido: " + nombreArma);
        }
    }

    private Unidad nuevaUnidad(String tipoUnidad, Integer id,Integer vida, Estadistica estadisticasUnidad, Equipamiento arma) {
        switch (tipoUnidad.trim().toUpperCase()){
            case "LORD":
                return new Lord(id,vida,estadisticasUnidad,arma);
            case "BASICA":
                return new UnidadBasica(id,vida,estadisticasUnidad,arma);
            default:
                throw new IllegalArgumentException("Tipo de unidad no registrado");
        }
    }
}

