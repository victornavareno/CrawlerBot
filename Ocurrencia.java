import java.io.Serializable;
import java.util.Map;

class Ocurrencia implements Serializable {
    private Integer FT;
    private Map<Integer, Integer> indiceArchivos; // Ahora usamos índices en vez de rutas

    public Ocurrencia(Integer FT, Map<Integer, Integer> indiceArchivos) {
        this.FT = FT;
        this.indiceArchivos = indiceArchivos;
    }

    public Integer getFT() {
        return FT;
    }

    public void setFT(Integer FT) {
        this.FT = FT;
    }

    public Map<Integer, Integer> getIndiceArchivos() {
        return indiceArchivos;
    }

    public void setIndiceArchivos(Map<Integer, Integer> indiceArchivos) {
        this.indiceArchivos = indiceArchivos;
    }

    @Override
    public String toString() {
        return "Ocurrencia{" +
                "FT=" + FT +
                ", indiceArchivos=" + indiceArchivos +
                '}';
    }
}
