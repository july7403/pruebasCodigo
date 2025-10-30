package Builders;

import EquipamientoPackage.Baculo;
import EquipamientoPackage.Equipamiento;
import Jugador.Jugador;

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
        //TODO:
        return new ArrayList<Jugador>();
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

    private Equipamiento crearEquipamiento(String dato, String dato1) {
        //TODO:
        return new Baculo();
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

