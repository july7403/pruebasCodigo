package Jugador;
import java.util.List;
import UnidadPackage.Unidad;

public class Jugador {
    private String nombre;
    private List<Unidad> unidades;
    private String color;

    public Jugador (String nombre, List<Unidad> unidades, String color){
        this.nombre = nombre;
        this.unidades = unidades;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }
    public String getColor(){
        return this.color;
    }
    public List<Unidad> getUnidades() {
        return unidades;
    }
    public boolean tieneEstaUnidad(Unidad unidad){
        return this.unidades.contains(unidad);
    }

    public String contarUnidadesVivas() {
        return String.valueOf(this.unidades.size());
    }

    public void setNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }
}
