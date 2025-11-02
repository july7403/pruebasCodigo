package EfectosTerreno;

public class EfectoAreaContaminada implements EfectoTerreno{
    @Override
    public String getColorFondo() {
        return "\u001B[45m"; //Violeta oscuro
    }
}
