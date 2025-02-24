import java.io.*;
import java.util.*;

public class FileProcessor {

    // Método para contar palabras en un archivo y actualizar el diccionario
    public static void fichContPalabras(File fichEntrada, Map<String, Integer> diccionario) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fichEntrada));
        String linea;

        while ((linea = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
            while (st.hasMoreTokens()) {
                String palabra = st.nextToken().toLowerCase(); // Normalizamos las palabras a minúsculas
                diccionario.put(palabra, diccionario.getOrDefault(palabra, 0) + 1);
            }
        }
        br.close();
    }

    // Método para serializar el diccionario y guardarlo en un archivo
    public static void salvarObjeto(Map<String, Integer> diccionario) {
        try (FileOutputStream fos = new FileOutputStream("diccionario.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(diccionario);
            System.out.println("Diccionario guardado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    // Método para recorrer un directorio y contar palabras en cada archivo
    public static void RIBW(File fichero, Map<String, Integer> diccionario) throws Exception {
        Queue<File> frontera = new LinkedList<>();
        frontera.offer(fichero);

        while (!frontera.isEmpty()) {
            File actual = frontera.poll();

            if (actual.isDirectory()) {
                System.out.println("Estás en el directorio: " + actual.getAbsolutePath());
                String[] listaFicheros = actual.list();

                if (listaFicheros != null) {
                    for (String nombreFichero : listaFicheros) {
                        File nuevoFichero = new File(actual, nombreFichero);
                        if (nuevoFichero.isDirectory()) {
                            frontera.offer(nuevoFichero);
                        } else {
                            fichContPalabras(nuevoFichero, diccionario);
                        }
                    }
                }
            } else {
                fichContPalabras(actual, diccionario);
            }
        }
    }

    // Método para imprimir el contenido del diccionario después de deserializarlo
    public static void deserializarFichero() {
        Map<String, Integer> diccionario = cargarObjeto();

        if (!diccionario.isEmpty()) {
            System.out.println("Contenido del diccionario:");
            for (Map.Entry<String, Integer> entry : diccionario.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        } else {
            System.out.println("El diccionario está vacío.");
        }
    }

    public static void main(String[] args) {
        File fichero = new File("E:\\4º\\RIBW\\E1");

        Map<String, Integer> diccionario = cargarObjeto(); // Cargar diccionario
        Map<Integer, occurrence> ocurrencias = new TreeMap<>(); // Crear diccionario de ocurrencias
        String userInput = ""; // Inicializar variable para entrada de usuario

        // MIENTRAS NO PULSAMOS ESCAPE
        // INPUT: USUARIO ESCRIBE PALABRA
        // PROCESAR PALABRA
        // OUTPUT: MOSTRAR PALABRA Y FRECUENCIA

        if (fichero.exists())

        {
            try {
                RIBW(fichero, diccionario); // Procesar archivos y actualizar diccionario
            } catch (Exception e) {
                e.printStackTrace();
            }
            salvarObjeto(diccionario); // Guardar el diccionario actualizado
        }

        deserializarFichero(); // Mostrar el contenido final del diccionario
    }
}
