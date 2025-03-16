import java.io.*;
import java.util.*;

public class CargarObjeto {

    // Método para deserializar el diccionario desde un archivo
    @SuppressWarnings("unchecked")
    public static Map<String, Integer> cargarDiccionario() {
        File archivo = new File("diccionario.ser");
        if (!archivo.exists()) {
            System.out.println("No se encontró el diccionario. Se creará uno nuevo.");
            Map<String, Integer> diccionarioVacio = new HashMap<>();
            GuardarObjeto.guardarObjeto(diccionarioVacio); // Se crea el archivo serializado.
            return diccionarioVacio;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Map<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el diccionario. Se creará uno nuevo.");
            e.printStackTrace();
            Map<String, Integer> diccionarioVacio = new HashMap<>();
            GuardarObjeto.guardarObjeto(diccionarioVacio);
            return diccionarioVacio;
        }
    }

    // Método para cargar el thesauro desde el archivo "thesauro.txt"
    public static Set<String> cargarThesauro() {
        Set<String> thesauroSet = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("thesauro.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Separa usando ; o , como delimitador
                String[] tokens = linea.split("[;,]");
                for (String token : tokens) {
                    token = token.trim().toLowerCase();
                    if (token.isEmpty())
                        continue;
                    // Ignorar tokens que contengan "(fig.)", "(norae)" o "(loc.)"
                    if (token.contains("(fig.)") || token.contains("(norae)") || token.contains("(loc.)")) {
                        continue;
                    }
                    thesauroSet.add(token);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el thesauro: " + e.getMessage());
            e.printStackTrace();
        }
        return thesauroSet;
    }
}
