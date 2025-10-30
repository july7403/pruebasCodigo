package Builders;

import EfectosTerreno.*;
import Mapa.Mapa;
import Mapa.Celda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapaFactoryTexto implements MapaFactory{

    /**
     * Dado un path procesa el archivo de texto y devuelve un mapa
     * @param data, un path
     * @return Mapa mapa construido
     */
    @Override
    public Mapa procesarMapa(InputStream data) {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(data))){
            List<Celda[]> filas = new ArrayList<>();
            String linea;
            int fila = 0;

            while ((linea = lector.readLine()) != null){
                Celda[] celdas = crearDesdeLinea(linea,fila);
                filas.add(celdas);
                fila++;
            }
            Celda[][] matrizFinal = crearMatriz(filas);
            return new Mapa(matrizFinal);
        }catch (IOException e){
            throw new RuntimeException("Error al leer el input para procesar el mapa");
        }
    }

    /**
     * @param filas Strings que representan a las celdas a contruir
     * @return Crea la Matriz de celdas corresponiente
     */
    private Celda[][] crearMatriz(List<Celda[]> filas) {
        if (filas.isEmpty()){
            return new Celda[0][0];
        }
        int numeroFilas = filas.size();
        int numeroColumnas = filas.getFirst().length;
        Celda[][] matriz = new Celda[numeroFilas][numeroColumnas];

        for (int i = 0 ; i < numeroFilas; i++){
            System.arraycopy(filas.get(i), 0, matriz[i], 0, numeroColumnas);
        }
        return matriz;
    }

    /**
     * Dada una linea de texto, crea una vector con las celdas correctas
     * @param linea string que representa a las celdas a construir
     * @param fila coordena Y
     * @return vector de celdas contruidas
     */
    private Celda[] crearDesdeLinea(String linea, Integer fila){
        String[] codigos = linea.split(",");
        Celda[] celdas = new Celda[codigos.length];
        for (int i = 0; i < codigos.length;i++){
            String codigo = codigos[i].trim();
            // switch cases
            celdas[i] = crearCeldaPorCodigo(codigo, fila,i);
        }
        return celdas;
    }

    /**
     * Dado un codigo de 3 letras devuelva la celda correspondiente creada
     * @param codigo string de 3 letras
     * @param fila entero que representa la posicion en Y
     * @param i entero que representa la posicion en X
     * @return Celda contruida
     */
    private Celda crearCeldaPorCodigo(String codigo, int fila, int i) {
        String nombre;
        boolean esTransitable;
        EfectoTerreno efectoTerreno;
        switch (codigo){
            case "CLL" : //Llanura
                nombre = "Llanura";
                esTransitable = true;
                efectoTerreno = new EfectoLlanura();
                break;
            case "BSQ": //Bosque
                nombre = "Bosque";
                esTransitable = true;
                efectoTerreno = new EfectoBosque();
                break;
            case "ACL": //Acantilado
                nombre = "Acantilado";
                esTransitable = false;
                efectoTerreno = new EfectoAcantilado();
                break;
            case "Agua": //Mar,Lago,Rio,etc.
                nombre = "Agua";
                esTransitable = false;
                efectoTerreno = new EfectoAgua();
                break;
            case "PTN": //Pantano
                nombre = "pantano";
                esTransitable = true;
                efectoTerreno = new EfectoPantano();
                break;
            case "CST": //Castillo, fortaleza
                nombre = "Castillo";
                esTransitable = true;
                efectoTerreno = new EfectoCastillo();
                break;
            case "ACM": //Area Contamiada
                nombre = "Area Contaminada";
                esTransitable = true;
                efectoTerreno = new EfectoAreaContaminada();
                break;
            default:
                throw new IllegalArgumentException("Codigo de terreno no reconocido");

        }
        return new Celda(nombre,esTransitable,efectoTerreno,fila,i);
    }

}

/*  CLL --> llanura
    BSQ --> Bosque
    AGU --> Agua
    PTN --> pantano
    CST --> castillo
    ACM --> area contaminada
    ACL --> acantilado
 */

/* mapa ejemplo
CST, BSQ, AGU, AGU, AGU, CLL
BSQ, CLL, CLL, PTN, PTN, BSQ
ACL, CLL, CLL, CLL, CLL, CLL*/
