import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class GuardarObjeto {
    // Método para serializar el diccionario y guardarlo en un archivo
    public static void guardarObjeto(Map<String, Integer> diccionario) {
        try (FileOutputStream fos = new FileOutputStream("diccionario.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(diccionario);
            System.out.println("Diccionario guardado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}