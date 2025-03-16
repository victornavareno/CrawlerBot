import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CargarObjeto {

    // Método para deserializar el diccionario desde un archivo
    @SuppressWarnings("unchecked")
    public static Map<String, Integer> cargarObjeto() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("diccionario.ser"))) {
            return (Map<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontró el diccionario. Se creará uno nuevo.");
            return new HashMap<>();
        }
    }
}