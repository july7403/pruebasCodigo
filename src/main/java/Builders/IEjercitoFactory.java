package Builders;

import Jugador.Jugador;
import java.io.InputStream;
import java.util.List;

public interface IEjercitoFactory {
    public List<Jugador> procesarEjecito(InputStream dataEjercito);
}
