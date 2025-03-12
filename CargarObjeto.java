import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class CargarObjeto {

    // Método para deserializar el diccionario desde un archivo
    public static Map<String, Integer> cargarObjeto() {
        Map<String, Integer> diccionario = new TreeMap<>();

        try (FileInputStream fis = new FileInputStream("diccionario.ser");
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            if (obj instanceof Map) {
                diccionario = (Map<String, Integer>) obj;
                System.out.println("Diccionario cargado correctamente.");
            } else {
                System.out.println("El objeto deserializado no es un Map.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("El archivo diccionario.ser no existe. Se creará uno nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return diccionario;
    }
}