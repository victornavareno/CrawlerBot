import java.io.*;
import java.util.*;

public class ListaURLs {
    // EN ESTA LISTA GUARDAREMOS LAS URLS DE LOS ARCHIVOS PROCESADOS PARA INDEXARLOS
    // USANDO ids
    private static List<String> listaURLs = new ArrayList<>();
    private static final String ARCHIVO_URLS = "urls.ser";

    // Cargar URLs desde archivo serializado
    @SuppressWarnings("unchecked")
    public static void cargarListaURLs() {
        File archivo = new File(ARCHIVO_URLS);
        if (!archivo.exists()) {
            listaURLs = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            listaURLs = (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la lista de URLs.");
            e.printStackTrace();
            listaURLs = new ArrayList<>();
        }
    }

    // Guardar la lista de URLs en archivo serializado
    public static void guardarListaURLs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_URLS))) {
            oos.writeObject(listaURLs);
        } catch (IOException e) {
            System.out.println("Error al guardar la lista de URLs.");
            e.printStackTrace();
        }
    }

    // devuelve el nuemro asociado a la url guardada en la lista
    public static int obtenerIndiceURL(String url) {
           int index = listaURLs.indexOf(url);
        if (index == -1) {
            listaURLs.add(url);
            guardarListaURLs();
            return listaURLs.size() - 1;
        }
        return index;
    }

    public static List<String> getListaURLs() {
        return listaURLs;
    }
}
