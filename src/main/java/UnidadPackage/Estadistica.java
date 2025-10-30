package UnidadPackage;

public class Estadistica {
    private Integer atk;
    private Integer def;
    private Integer mgc;
    private Integer mov;

    public Estadistica(Integer atk, Integer def, Integer mgc, Integer mov){
        this.atk = atk;
        this.def = def;
        this.mgc = mgc;
        this.mov = mov;
    }

    public Integer getDef() {
        return def;
    }

    public Integer getMgc() {
        return mgc;
    }

    public Integer getMov() {
        return mov;
    }

    public Integer getAtk() {
        return atk;
    }
}
