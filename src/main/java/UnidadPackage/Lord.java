package UnidadPackage;

import EquipamientoPackage.Equipamiento;

public class Lord extends Unidad {
    public Lord(Integer id, Integer vida, Estadistica estadisticas, Equipamiento equipamiento) {
        super(id, vida, estadisticas, equipamiento);
    }

    @Override
    public String getDisplay() {
        return "L";  //L de lord
    }
}