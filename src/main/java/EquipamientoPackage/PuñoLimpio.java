package EquipamientoPackage;

public class PuñoLimpio extends Equipamiento{
    private static PuñoLimpio instancia;
    private PuñoLimpio() {
        super("Puño Limpio",
                1,
                0,
                0,
                10000, //infinito
                new EstrategiaCuerpoACuerpo());
    }
    public static PuñoLimpio obtenerInstancia(){
        if (instancia==null){
            instancia = new PuñoLimpio();
        }
        return instancia;
    }

}
