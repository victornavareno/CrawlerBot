import java.io.*;
import java.util.*;

public class ProcesadorArchivos {

    // #################################
    // ########### SESIÓN 1 ############
    // #################################

    // Método para contar palabras en un archivo y actualizar el diccionario
    public static void contarPalabrasEnArchivo(File archivoEntrada, Map<String, Integer> diccionario)
            throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));
        String linea;

        while ((linea = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
            while (st.hasMoreTokens()) {
                String palabra = st.nextToken().toLowerCase(); // Normalizamos a minúsculas
                diccionario.put(palabra, diccionario.getOrDefault(palabra, 0) + 1);
            }
        }
        br.close();
    }

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

    // Método para imprimir el contenido del diccionario
    public static void imprimirDiccionario() {
        Map<String, Integer> diccionario = cargarObjeto();

        if (!diccionario.isEmpty()) {
            System.out.println("Contenido del diccionario:");
            for (Map.Entry<String, Integer> entrada : diccionario.entrySet()) {
                System.out.println(entrada.getKey() + " : " + entrada.getValue());
            }
        } else {
            System.out.println("El diccionario está vacío.");
        }
    }

    // Método para recorrer un directorio y contar palabras en cada archivo
    public static void recorrerDirectorios(File directorio, Map<String, Integer> diccionario) throws Exception {
        Queue<File> frontera = new LinkedList<>();
        frontera.offer(directorio);

        while (!frontera.isEmpty()) {
            File actual = frontera.poll();

            if (actual.isDirectory()) {
                System.out.println("Estás en el directorio: " + actual);
                String[] listaArchivos = actual.list();

                if (listaArchivos != null) {
                    for (String nombreArchivo : listaArchivos) {
                        File nuevoArchivo = new File(actual, nombreArchivo);
                        System.out.println(nombreArchivo);
                        if (nuevoArchivo.isDirectory()) {
                            frontera.offer(nuevoArchivo);
                        } else {
                            contarPalabrasEnArchivo(nuevoArchivo, diccionario);
                        }
                    }
                }
            } else {
                contarPalabrasEnArchivo(actual, diccionario);
            }
        }
    }

    // #################################
    // ########### SESIÓN 2 ############
    // #################################

    // Método para buscar un token en cada archivo a partir de un directorio raíz
    public static List<Ocurrencia> buscarTokenEnArchivos(File raiz, String token) {
        List<Ocurrencia> ocurrencias = new ArrayList<>();
        Queue<File> cola = new LinkedList<>();
        cola.offer(raiz);

        while (!cola.isEmpty()) {
            File actual = cola.poll();
            if (actual.isDirectory()) {
                File[] archivos = actual.listFiles();
                if (archivos != null) {
                    for (File archivo : archivos) {
                        cola.offer(archivo);
                    }
                }
            } else {
                try {
                    int cuenta = contarTokenEnArchivo(actual, token);
                    if (cuenta > 0) {
                        ocurrencias.add(new Ocurrencia(actual, cuenta));
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo: " + actual);
                    e.printStackTrace();
                }
            }
        }
        return ocurrencias;
    }

    // Método para contar cuntas veces aparece un token en un archivo
    public static int contarTokenEnArchivo(File archivo, String token) throws IOException {
        int cuenta = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
                while (st.hasMoreTokens()) {
                    if (st.nextToken().equalsIgnoreCase(token)) {
                        cuenta++;
                    }
                }
            }
        }
        return cuenta;
    }

    public static void main(String[] args) {
        File directorioRaiz = new File("E1");
        Map<String, Integer> diccionario = cargarObjeto(); // Cargar diccionario

        // SESIÓN 1
        System.out.println();
        System.out.println("##### SESIÓN 1 #####");
        if (directorioRaiz.exists()) {
            try {
                recorrerDirectorios(directorioRaiz, diccionario); // Procesar archivos y actualizar diccionario
            } catch (Exception e) {
                e.printStackTrace();
            }
            guardarObjeto(diccionario); // Guardar el diccionario actualizado
        }

        imprimirDiccionario(); // Mostrar el contenido final del diccionario

        // SESIÓN 2: Búsqueda interactiva de tokens
        System.out.println();
        System.out.println("##### SESIÓN 2 #####");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(
                    "Introduce una palabra (token) para buscar su frecuencia en cada archivo (o escribe '/salir' para salir):");
            String token = sc.nextLine().toLowerCase(); // Normalizamos el token a minúsculas

            if (token.equals("/salir")) { // Si el usuario ingresa "ESC", terminamos el bucle
                System.out.println("Saliendo del programa...");
                break;
            }

            List<Ocurrencia> ocurrencias = buscarTokenEnArchivos(directorioRaiz, token);
            if (ocurrencias.isEmpty()) {
                System.out.println("La palabra \"" + token + "\" no se encontró en ningún archivo.");
            } else {
                System.out.println("Frecuencia de \"" + token + "\" en cada archivo:");
                for (Ocurrencia oc : ocurrencias) {
                    System.out.println(oc);
                }
            }
        }

        sc.close();
    }
}
