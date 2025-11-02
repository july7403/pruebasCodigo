package EfectosTerreno;

public class EfectoAgua implements EfectoTerreno{
    @Override
    public String getColorFondo() {
        return "\u001B[44m"; //Azul
    }
}
