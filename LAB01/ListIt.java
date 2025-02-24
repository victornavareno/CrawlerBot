/*
 * ListIt.java: Lista contenido de ficheros textuales
 * (i) Felix R. Rodriguez, EPCC, Universidad de Extremadura, 2009-23
 * http://madiba.unex.es/
 */

import java.io.*;

class ListIt {

    public static void main (String [] args) throws Exception {
        if (args.length<1) {
            System.out.println("ERROR. Ejecutar: >java ListIt nombre_archivo");
            return;
        }
        File fichero = new File(args[0]);
            if (!fichero.exists() || !fichero.canRead()) {
                System.out.println("ERROR. No puedo leer " + fichero);
                return;
            }
            if (fichero.isDirectory()) {
                String [] listaFicheros = fichero.list();
                for (int i=0; i<listaFicheros.length; i++)
                    System.out.println(listaFicheros[i]);
            }
            else try {
                /* Interesante filtrar previamente archivos solo textuales, como los
                 * .txt, .java, .c, .cpp, etc., empleando metodos de la clase String:
                 * lastIndexOf('.'), substring (posic) y equals(".txt")...
                 */
                FileReader fr = new FileReader(fichero);
                BufferedReader br = new BufferedReader(fr);
                String linea;
                while ((linea=br.readLine()) != null)
                    System.out.println(linea);
            }
            catch (FileNotFoundException fnfe) {
                System.out.println("ERROR. Fichero desaparecido en combate  ;-)");
            }
    }
}
