import java.io.*;
import java.util.*;

public class FileProcessor {

    // #################################
    // ########### SESION 1 ############
    // #################################
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

    // Método para imprimir el contenido del diccionario después de deserializarlo
    public static void imprimirDiccionario() {
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

    // Método para recorrer un directorio y contar palabras en cada archivo
    public static void RIBW(File fichero, Map<String, Integer> diccionario) throws Exception {
        Queue<File> frontera = new LinkedList<>();
        frontera.offer(fichero);

        while (!frontera.isEmpty()) {
            File actual = frontera.poll();

            if (actual.isDirectory()) {
                System.out.println("Estás en el directorio: " + actual);
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

    // #################################
    // ########### SESION 2 ############
    // #################################
    public static List<Ocurrencia> searchTokenInFiles(File root, String token) {
        List<Ocurrencia> occurrences = new ArrayList<>();
        Queue<File> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            File current = queue.poll();
            if (current.isDirectory()) {
                // Add all files/subdirectories to the queue
                File[] files = current.listFiles();
                if (files != null) {
                    for (File f : files) {
                        queue.offer(f);
                    }
                }
            } else {
                try {
                    int count = countTokenInFile(current, token);
                    // You may decide to only add files where the token appears
                    if (count > 0) {
                        occurrences.add(new Ocurrencia(current, count));
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + current.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
        return occurrences;
    }

    public static int countTokenInFile(File file, String token) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Tokenize the line with the same delimiters as before
                StringTokenizer st = new StringTokenizer(line, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
                while (st.hasMoreTokens()) {
                    // Compare tokens in a case-insensitive way
                    if (st.nextToken().equalsIgnoreCase(token)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        File fichero = new File("E1");

        Map<String, Integer> diccionario = cargarObjeto(); // Cargar diccionario

        // SESION 1
        System.out.println("##### SESIÓN 1 #####");
        if (fichero.exists())

        {
            try {
                RIBW(fichero, diccionario); // Procesar archivos y actualizar diccionario
            } catch (Exception e) {
                e.printStackTrace();
            }
            salvarObjeto(diccionario); // Guardar el diccionario actualizado
        }

        imprimirDiccionario(); // Mostrar el contenido final del diccionario

        // SESSION 2: Search for a token in each file
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce a palabra (token) para buscar su frecuencia en cada archivo:");
        String token = sc.nextLine().toLowerCase(); // Normalize token to lowercase

        // Assuming 'fichero' is your root directory
        File rootDirectory = new File("E1");
        List<Ocurrencia> Ocurrencias = searchTokenInFiles(rootDirectory, token);

        if (Ocurrencias.isEmpty()) {
            System.out.println("La palabra \"" + token + "\" no se encontró en ningún archivo.");
        } else {
            System.out.println("Frecuencia de \"" + token + "\" en cada archivo:");
            for (Ocurrencia occ : Ocurrencias) {
                System.out.println(occ);
            }
        }

        sc.close();
    }
}
