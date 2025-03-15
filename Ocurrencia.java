import java.io.Serializable;
import java.util.*;

class Ocurrencia implements Serializable {
    private Integer FT;
    private Map<String, Integer> rutaArchivo;

    public Ocurrencia() {
        this.FT = 0;
        this.rutaArchivo = new HashMap<>();
    }

    public void agregarOcurrencia(String ruta, int cantidad) {
        this.FT += cantidad;
        this.rutaArchivo.put(ruta, this.rutaArchivo.getOrDefault(ruta, 0) + cantidad);
    }

    public Integer getFT() {
        return FT;
    }

    public Map<String, Integer> getRutaArchivo() {
        return rutaArchivo;
    }

    @Override
    public String toString() {
        return "Ocurrencia [FT=" + FT + ", rutaArchivo=" + rutaArchivo + "]";
    }
}