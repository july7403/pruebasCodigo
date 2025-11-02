package EfectosTerreno;

public class EfectoBosque implements EfectoTerreno{
    @Override
    public String getColorFondo() {
        return "\\u001B[42m"; //Verde
    }
}
