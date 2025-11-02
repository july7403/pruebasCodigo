package EfectosTerreno;

public class EfectoAcantilado implements EfectoTerreno{
    @Override
    public String getColorFondo() {
        return "\u001B[40m"; //Negro
    }
}
