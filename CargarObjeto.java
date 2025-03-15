import java.io.*;

public class CargarObjeto {
    public static <T> T cargarObjeto(String rutaArchivo) {
        T objeto = null;
        try (FileInputStream fis = new FileInputStream(rutaArchivo);
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            objeto = (T) obj;

            System.out.println("Objeto cargado correctamente desde " + rutaArchivo);
        } catch (FileNotFoundException e) {
            System.out.println("El archivo " + rutaArchivo + " no existe. Se creará uno nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objeto;
    }
}