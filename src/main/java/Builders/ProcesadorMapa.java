package Builders;

import Mapa.Mapa;

import java.io.InputStream;

public class ProcesadorMapa {
    private MapaFactory procesadorMapa;

    public ProcesadorMapa(MapaFactory procesadorMapa){
        this.procesadorMapa = procesadorMapa;
    }
    public void setProcesadorMapa(MapaFactory procesadorMapa){
        this.procesadorMapa = procesadorMapa;
    }

    /**
     * @param data, input por donde se quiere recibir el mapa a utilizar, un archivo csv
     * @return Mapa construido
     */
    public Mapa procesarMapa(InputStream data){
        Mapa mapa = this.procesadorMapa.procesarMapa(data);
        return mapa;
    }
}
