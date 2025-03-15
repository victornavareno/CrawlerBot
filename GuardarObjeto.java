import java.io.*;

public class GuardarObjeto {
    public static <T> void guardarObjeto(String rutaArchivo, T objeto) {
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(objeto);
            System.out.println("Objeto guardado correctamente en " + rutaArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}