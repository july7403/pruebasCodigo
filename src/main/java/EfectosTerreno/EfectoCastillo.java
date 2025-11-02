package EfectosTerreno;

public class EfectoCastillo implements EfectoTerreno{
    @Override
    public String getColorFondo() {
        return "\\u001B[46m"; //Azul claro
    }
}
