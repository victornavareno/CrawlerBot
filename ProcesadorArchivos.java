import java.io.*;
import java.util.*;

public class ProcesadorArchivos {

    // Variables Globales
    private static Map<String, Ocurrencia> diccionario = new TreeMap<>();
    private static Map<String, Object> thesauro = new TreeMap<>();

    // #################################
    // ########### SESIÓN 1 ############
    // #################################

    // Método para contar palabras en un archivo y actualizar el diccionario
    public static void contarPalabrasEnArchivo(String rutaArchivo, Map<String, Ocurrencia> diccionario)
            throws IOException {
        File archivo = new File(rutaArchivo);
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
                while (st.hasMoreTokens()) {
                    String palabra = st.nextToken().toLowerCase();
                    if (!palabra.contains("(loc.)") || !palabra.contains("(NoRAE)") || !palabra.contains("(fig.)")) {
                        // comprobamos si está en el thesauro
                        if (thesauro.containsKey(palabra)) {
                            // Si la palabra está en el thesauro, se actualiza el diccionario con la
                            // frecuencia de la palabra
                            Ocurrencia oc;
                            if (diccionario.containsKey(palabra)) {
                                oc = diccionario.get(palabra);
                            } else {
                                oc = new Ocurrencia();
                            }
                            oc.agregarOcurrencia(rutaArchivo, 1);
                            diccionario.put(palabra, oc);
                        }
                    }
                }
            }
        }
    }

    // Método para imprimir el contenido del diccionario
    public static void imprimirDiccionario() {
        // Se asume que CargarObjeto.cargarObjeto() ahora retorna Map<String,
        // Ocurrencia>
        Map<String, Ocurrencia> diccionario = CargarObjeto.cargarObjeto("diccionario.ser");

        if (!diccionario.isEmpty()) {
            System.out.println("Contenido del diccionario:");
            for (Map.Entry<String, Ocurrencia> entrada : diccionario.entrySet()) {
                System.out.println(entrada.getKey() + " : " + entrada.getValue());
            }
        } else {
            System.out.println("El diccionario está vacío.");
        }
    }

    // Método para recorrer un directorio y contar palabras en cada archivo
    public static void recorrerDirectorios(String rutaDirectorio, Map<String, Ocurrencia> diccionario)
            throws Exception {
        Queue<String> frontera = new LinkedList<>();
        frontera.offer(rutaDirectorio);

        while (!frontera.isEmpty()) {
            String rutaActual = frontera.poll();
            File actual = new File(rutaActual);

            if (actual.isDirectory()) {
                System.out.println("Estás en el directorio: " + actual);
                String[] listaArchivos = actual.list();
                if (listaArchivos != null) {
                    for (String nombreArchivo : listaArchivos) {
                        String nuevaRuta = actual.getAbsolutePath() + File.separator + nombreArchivo;
                        File nuevoArchivo = new File(nuevaRuta);
                        if (nuevoArchivo.isDirectory()) {
                            frontera.offer(nuevaRuta);
                        } else {
                            contarPalabrasEnArchivo(nuevaRuta, diccionario);
                        }
                    }
                }
            } else {
                contarPalabrasEnArchivo(rutaActual, diccionario);
            }
        }
    }

    // #################################
    // ########### SESIÓN 2 ############
    // #################################

    public static Map<String, Ocurrencia> buscarTokenEnArchivos(String rutaRaiz, String token) {
        Map<String, Ocurrencia> ocurrencias = new HashMap<>();
        Queue<String> cola = new LinkedList<>();
        cola.offer(rutaRaiz);

        while (!cola.isEmpty()) {
            String rutaActual = cola.poll();
            File actual = new File(rutaActual);

            if (actual.isDirectory()) {
                File[] archivos = actual.listFiles();
                if (archivos != null) {
                    for (File archivo : archivos) {
                        cola.offer(archivo.getAbsolutePath());
                    }
                }
            } else {
                try {
                    int cuenta = contarTokenEnArchivo(rutaActual, token);
                    if (cuenta > 0) {
                        ocurrencias.putIfAbsent(token, new Ocurrencia());
                        ocurrencias.get(token).agregarOcurrencia(rutaActual, cuenta);
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo: " + rutaActual);
                    e.printStackTrace();
                }
            }
        }
        return ocurrencias;
    }

    // Método para contar cuántas veces aparece un token en un archivo
    public static int contarTokenEnArchivo(String rutaArchivo, String token) throws IOException {
        int cuenta = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
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

    public static void sesion1(String dirRaiz, File dir) {
        if (!dir.exists()) {
            System.out.println("El directorio no existe.");
            return;
        } else {
            System.out.println("Ejecutando SESIÓN 1.");
            try {
                recorrerDirectorios(dirRaiz, diccionario); // Procesar archivos y actualizar diccionario
            } catch (Exception e) {
                e.printStackTrace();
            }
            GuardarObjeto.guardarObjeto("diccionario.ser", diccionario); // Guardar el diccionario actualizado
            imprimirDiccionario(); // Mostrar el diccionario (deserializado)
        }
    }

    public static void buscarFrecuenciaPalabras(String dirRaiz, File dir, String token) {
        System.out.println("\n##### SESIÓN 2 #####");
        System.out.println("Buscando la palabra: \"" + token + "\" en los archivos...\n");

        Map<String, Ocurrencia> ocurrencias = buscarTokenEnArchivos(dirRaiz, token);

        if (ocurrencias.isEmpty()) {
            System.out.println("La palabra \"" + token + "\" no se encontró en ningún archivo.");
        } else {
            System.out.println("Frecuencia de \"" + token + "\" en cada archivo:");

            // Crear una lista de rutas ordenadas por frecuencia descendente
            List<Map.Entry<String, Integer>> listaArchivosOrdenados = new ArrayList<>(
                    ocurrencias.get(token).getRutaArchivo().entrySet());
            listaArchivosOrdenados.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

            // Imprimir la lista ordenada solo con las rutas y frecuencias (ruta relativa)
            for (Map.Entry<String, Integer> entry : listaArchivosOrdenados) {
                String rutaRelativa = new File(dirRaiz).toURI().relativize(new File(entry.getKey()).toURI()).getPath();
                System.out.println(rutaRelativa + " = " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No se han ingresado argumentos, imprimiendo diccionario...");
            imprimirDiccionario();
            System.out.println("Uso esperado:");
            System.out.println("1. <directorio> -> Ejecuta la sesión 1.");
            System.out.println("2. <directorio> <palabra> -> Busca la palabra en sesión 2.");
            return;
        }

        String directorioRaiz = args[0];
        File directorio = new File(directorioRaiz);
        // Cargar cada estructura desde su archivo correspondiente:
        thesauro = CargarObjeto.<Map<String, Object>>cargarObjeto("thesauro.ser");
        diccionario = CargarObjeto.<Map<String, Ocurrencia>>cargarObjeto("diccionario.ser");

        if (thesauro == null) {
            // Inicializar el thesauro si no se pudo cargar
            thesauro = new TreeMap<>();
        }
        if (diccionario == null) {
            // Inicializar el diccionario si no se pudo cargar
            diccionario = new TreeMap<>();
        }

        if (args.length == 1) {
            sesion1(directorioRaiz, directorio);
        } else if (args.length == 2) {
            String token = args[1].toLowerCase();
            buscarFrecuenciaPalabras(directorioRaiz, directorio, token);
        } else {
            System.out.println("Error: Se ha ingresado un número incorrecto de argumentos.");
            System.out.println("Uso esperado:");
            System.out.println("1. <directorio> -> Ejecuta la sesión 1.");
            System.out.println("2. <directorio> <palabra> -> Busca la palabra en sesión 2.");
        }
    }

}
