package EfectosTerreno;

public class EfectoPantano implements EfectoTerreno{
    @Override
    public String getColorFondo() {
        return "\\u001B[43m"; //amarillo, verde oscuro
    }
}
