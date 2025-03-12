import java.io.*;
import java.util.*;

public class ProcesadorArchivos {
    private static Map<String, Integer> diccionario = new TreeMap<>(); // variable global

    // #################################
    // ########### SESIÓN 1 ############
    // #################################

    // Método para contar palabras en un archivo y actualizar el diccionario
    public static void contarPalabrasEnArchivo(String rutaArchivo, Map<String, Integer> diccionario)
            throws IOException {
        File archivo = new File(rutaArchivo);
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
                while (st.hasMoreTokens()) {
                    String palabra = st.nextToken().toLowerCase();
                    diccionario.put(palabra, diccionario.getOrDefault(palabra, 0) + 1);
                }
            }
        }
    }

    // Método para imprimir el contenido del diccionario
    public static void imprimirDiccionario() {
        Map<String, Integer> diccionario = CargarObjeto.cargarObjeto();

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
    public static void recorrerDirectorios(String rutaDirectorio, Map<String, Integer> diccionario) throws Exception {
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

    // Método para buscar un token en cada archivo a partir de un directorio raíz
    public static List<Ocurrencia> buscarTokenEnArchivos(String rutaRaiz, String token) {
        List<Ocurrencia> ocurrencias = new ArrayList<>();
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
                        ocurrencias.add(new Ocurrencia(rutaActual, cuenta));
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo: " + rutaActual);
                    e.printStackTrace();
                }
            }
        }
        return ocurrencias;
    }

    // Método para contar cuntas veces aparece un token en un archivo
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

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No se han ingresado argumentos.");
            return;
        }

        String directorioRaiz = args[0];
        File directorio = new File(directorioRaiz);

        // EL DIRECTORIO INTRODUCIDO ES INCORRECTO
        if (args.length == 1) {
            if (!directorio.exists()) {
                System.out.println("El directorio no existe.");
                return;
            }
            // EJECUTO SESION 1
            else if (directorio.exists()) {
                System.out.println("Ejecutando SESIÓN 1.");
                try {
                    recorrerDirectorios(directorioRaiz, diccionario); // Procesar archivos y actualizar diccionario
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GuardarObjeto.guardarObjeto(diccionario);// Guardar el diccionario actualizado
                imprimirDiccionario(); // Mostrar el diccionario (deserializado)
            }

        }

        // Si hay 1 argumento -> Ejecuta SESIÓN 2 con búsqueda automática
        else if (args.length == 2) {
            String token = args[1].toLowerCase(); // Convertimos el argumento a minúsculas
            System.out.println("\n##### SESIÓN 2 #####");
            System.out.println("Buscando la palabra: \"" + token + "\" en los archivos...\n");

            List<Ocurrencia> ocurrencias = buscarTokenEnArchivos(directorioRaiz, token);
            if (ocurrencias.isEmpty()) {
                System.out.println("La palabra \"" + token + "\" no se encontró en ningún archivo.");
            } else {
                System.out.println("Frecuencia de \"" + token + "\" en cada archivo:");
                ocurrencias.sort((o1, o2) -> Integer.compare(o2.getCuenta(), o1.getCuenta())); // Ordenar por frecuencia
                                                                                               // descendente
                for (Ocurrencia oc : ocurrencias) {
                    System.out.println(oc);
                }
            }
        }

        // Si hay más de 1 argumento, muestra error
        else {
            System.out.println("Error: Se ha ingresado un número incorrecto de argumentos.");
            System.out.println("Uso esperado:");
            System.out.println("1. Sin argumentos -> Ejecuta la sesión 1.");
            System.out.println("2. Con un argumento -> Busca la palabra en sesión 2.");
        }
    }

}
