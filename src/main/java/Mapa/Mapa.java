package Mapa;

public class Mapa {
    private Celda [][] celdas;

    public Mapa (Celda[][] celdas){
        this.celdas = celdas;
    }

    /**
     * Dado un (x,y) devuelve si es una celda validad dentro del mapa actual
     * @param x Integer
     * @param y Integer
     * @return boolean (true o false)
     */
    public boolean validarCelda(Integer x, Integer y){
        boolean filaValida = (0<= y && y<this.celdas.length);
        boolean columnaValida = (0<=x && x<this.celdas[0].length);
        return filaValida && columnaValida;
    }

    /**
     * Dado un (x,y) devuelve la Celda correspondiente, caso de (x,y) no valido devuelve null
     * @param x Integer
     * @param y Integer
     */
    public Celda getCelda(Integer x, Integer y){
        if (validarCelda(x,y)){
            return this.celdas[y][x];
        }
        return null;
    }
    // Devuelve el ancho del mapa rectangular
    public Integer getAncho(){
        return this.celdas[0].length;
    }
    //devuelve el alto del mapa rectangular
    public Integer getAlto(){
        return this.celdas.length;
    }
}
