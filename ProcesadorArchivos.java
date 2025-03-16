import java.io.*;
import java.util.*;

public class ProcesadorArchivos {

    // #################################
    // ########### SESIÓN 1 ############
    // #################################

    // Método para contar palabras en un archivo y actualizar el diccionario
    public static void contarPalabrasEnArchivo(String rutaArchivo, Map<String, Integer> diccionario,
            Set<String> thesauro)
            throws IOException {
        File archivo = new File(rutaArchivo);
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
                while (st.hasMoreTokens()) {
                    String palabra = st.nextToken().toLowerCase();
                    // Sólo indexar si la palabra está en el thesauro
                    if (thesauro.contains(palabra)) {
                        if (!palabra.contains("(fig.)") || !palabra.contains("(NoRAE)")
                                || !palabra.contains("(loc.)")) {
                            diccionario.put(palabra, diccionario.getOrDefault(palabra, 0) + 1);
                        }

                    }
                }
            }
        }
    }

    // Método para imprimir el contenido del diccionario FUNCIONA 100%
    public static void imprimirDiccionario(Map<String, Integer> diccionario) {
        // Map<String, Integer> diccionario = CargarObjeto.cargarObjeto();

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
    public static void recorrerDirectorios(String rutaDirectorio, Map<String, Integer> diccionario,
            Set<String> thesauro) throws Exception {
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
                            contarPalabrasEnArchivo(nuevaRuta, diccionario, thesauro);
                        }
                    }
                }
            } else {
                contarPalabrasEnArchivo(rutaActual, diccionario, thesauro);
            }
        }
    }

    // #################################
    // ########### SESIÓN 2 ############
    // #################################

    // Método para buscar un token en cada archivo a partir de un directorio raíz
    public static Ocurrencia buscarTokenEnArchivos(String rutaRaiz, String token, Set<String> thesauro) {
        // Si el token no está en el thesauro, se devuelve una ocurrencia vacía
        if (!thesauro.contains(token)) {
            System.out.println("El token \"" + token + "\" no se encuentra en el thesauro.");
            return new Ocurrencia(0, new HashMap<>());
        }

        Ocurrencia ocurrenciaGlobal = new Ocurrencia(0, new HashMap<>());
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
                    int cuenta = contarTokenEnArchivo(rutaActual, token, thesauro);
                    if (cuenta > 0) {
                        ocurrenciaGlobal.getRutaArchivo().put(rutaActual, cuenta);
                        ocurrenciaGlobal.setFT(ocurrenciaGlobal.getFT() + cuenta);
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo: " + rutaActual);
                    e.printStackTrace();
                }
            }
        }
        return ocurrenciaGlobal;
    }

    // Método para contar cuntas veces apare un token en un archivo
    public static int contarTokenEnArchivo(String rutaArchivo, String token, Set<String> thesauro) throws IOException {
        // Si el token buscado no está en el thesauro, no se cuenta
        if (!thesauro.contains(token))
            return 0;

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

    // #################################
    // ########### MAIN ############

    public static void main(String[] args) {
        // Cargamos el diccionario y el thesauro
        Map<String, Integer> diccionario = CargarObjeto.cargarDiccionario();
        Set<String> thesauro = CargarObjeto.cargarThesauro();

        // Si no hay argumentos, mostramos error y uso.
        if (args.length == 0) {
            System.out.println("No se han ingresado argumentos.");
            imprimirDiccionario(diccionario);
            System.out.println("Uso esperado:");
            System.out.println("1. <directorio> -> Ejecuta la sesión 1 (indexar).");
            System.out.println("2. <directorio> <token> -> Ejecuta la sesión 2 (buscar palabra).");
            return;
        }

        String directorioRaiz = args[0];
        File directorio = new File(directorioRaiz);

        // ============ SESIÓN 1 ============
        if (args.length == 1) {
            if (!directorio.exists()) {
                System.out.println("El directorio no existe.");
                return;
            }
            System.out.println("Ejecutando SESIÓN 1 (indexando directorio).");

            try {
                recorrerDirectorios(directorioRaiz, diccionario, thesauro);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Guardamos el diccionario actualizado
            GuardarObjeto.guardarObjeto(diccionario);

            // Mostramos el contenido actualizado
            imprimirDiccionario(diccionario);
        }
        // ============ SESIÓN 2 ============
        else if (args.length == 2) {
            if (!directorio.exists()) {
                System.out.println("El directorio no existe.");
                return;
            }
            String token = args[1].toLowerCase(); // Convertimos a minúsculas
            System.out.println("\n##### SESIÓN 2 #####");
            System.out.println("Buscando la palabra: \"" + token + "\" en los archivos...\n");

            // Verificamos que el token esté en el thesauro
            if (!thesauro.contains(token)) {
                System.out.println(
                        "El token \"" + token + "\" no se encuentra en el thesauro, por lo que no se indexará.");
                return;
            }

            // Buscar el token en los archivos usando el thesauro
            Ocurrencia resultado = buscarTokenEnArchivos(directorioRaiz, token, thesauro);

            if (resultado.getFT() == 0) {
                System.out.println("La palabra \"" + token + "\" no se encontró en ningún archivo.");
            } else {
                System.out.println("Frecuencia total de \"" + token + "\": " + resultado.getFT());
                System.out.println("Detalle por archivo:");

                List<Map.Entry<String, Integer>> listaOrdenada = new ArrayList<>(resultado.getRutaArchivo().entrySet());
                listaOrdenada.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())); // Orden descendente

                for (Map.Entry<String, Integer> entry : listaOrdenada) {
                    String rutaRelativa = new File(directorioRaiz).toURI()
                            .relativize(new File(entry.getKey()).toURI())
                            .getPath();
                    System.out.println(" - " + rutaRelativa + " -> " + entry.getValue() + " apariciones.");
                }
            }
        }
        // ============ ERROR DE USO ============
        else {
            System.out.println("Error: Se ha ingresado un número icorrecto de argumentos.");
            System.out.println("Uso esperado:");
            System.out.println("Ningún argumento: Imprime El Diccionario.");
            System.out.println("1. <directorio> -> Ejecuta la sesión 1 (indexar).");
            System.out.println("2. <directorio> <token> -> Ejecuta la sesión 2 (buscar palabra).");
        }
    }

}