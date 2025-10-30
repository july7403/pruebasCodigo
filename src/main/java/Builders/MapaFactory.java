package Builders;

import Mapa.Mapa;

import java.io.InputStream;

public interface MapaFactory {
    public Mapa procesarMapa(InputStream data);
}
