class Ocurrencia implements Comparable<Ocurrencia> {
    private String rutaArchivo;
    private int cuenta;

    public Ocurrencia(String rutaArchivo, int cuenta) {
        this.rutaArchivo = rutaArchivo;
        this.cuenta = cuenta;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public int getCuenta() {
        return cuenta;
    }

    @Override
    public String toString() {
        return rutaArchivo + " : " + cuenta;
    }

    @Override
    public int compareTo(Ocurrencia otra) {
        return this.rutaArchivo.compareTo(otra.rutaArchivo);
    }
}