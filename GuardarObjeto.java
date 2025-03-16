import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class GuardarObjeto {
    // Método para serializar el diccionario y guardarlo en un archivo
    public static void guardarObjeto(Map<String, Integer> diccionario) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("diccionario.ser"))) {
            oos.writeObject(diccionario);
            System.out.println("Diccionario actualizado guardado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el diccionario.");
            e.printStackTrace();
        }
    }

}