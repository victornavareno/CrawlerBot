import java.io.Serializable;
import java.util.Map;

class Ocurrencia implements Serializable {
    // FT = Frecuencia Total de la palabra en todos los archivos.
    private Integer FT;

    // Mapa que asocia la ruta de un archivo con el número de apariciones en ese
    // archivo.
    private Map<String, Integer> rutaArchivo;

    public Ocurrencia(Integer FT, Map<String, Integer> rutaArchivo) {
        this.FT = FT;
        this.rutaArchivo = rutaArchivo;
    }

    public Integer getFT() {
        return FT;
    }

    public void setFT(Integer FT) {
        this.FT = FT;
    }

    public Map<String, Integer> getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(Map<String, Integer> rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public String toString() {
        return "Ocurrencia{" +
                "FT=" + FT +
                ", rutaArchivo=" + rutaArchivo +
                '}';
    }
}
