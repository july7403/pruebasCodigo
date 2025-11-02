package EfectosTerreno;

public class EfectoLlanura implements EfectoTerreno{
    @Override
    public String getColorFondo() {
        return "\\u001B[47m"; //gris claro
    }
}
