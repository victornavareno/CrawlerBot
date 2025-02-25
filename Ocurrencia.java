import java.io.File;
import java.io.Serializable;

public class Ocurrencia implements Comparable<Ocurrencia>, Serializable {
    private static final long serialVersionUID = 1L;
    private File archivo;
    private int cuenta;

    public Ocurrencia(File archivo, int cuenta) {
        this.archivo = archivo;
        this.cuenta = cuenta;
    }

    public File getArchivo() {
        return archivo;
    }

    public int getCuenta() {
        return cuenta;
    }

    @Override
    public String toString() {
        return archivo.getAbsolutePath() + " : " + cuenta;
    }

    @Override
    public int compareTo(Ocurrencia otra) {
        return this.archivo.getAbsolutePath().compareTo(otra.archivo.getAbsolutePath());
    }
}
