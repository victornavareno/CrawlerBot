/*
 * FichContPalabras.java: Contabiliza palabras contenidas en un fichero
 * (i) Felix R. Rguez., EPCC, Universidad de Extremadura, 2009-23
 * http://madiba.unex.es/
 */

import java.io.*;
import java.util.*;

public class FichContPalabras {
    
    public static void main (String args[]) throws IOException {
        if (args.length < 2) {
            System.out.println ("ERROR. Utilizar: >java FichContPalabras fichero_entrada fichero_salida");
            return;
        }

        String fichEntrada = args[0];
        String fichSalida = args[1];

        Map <String, Integer> map = new TreeMap <String, Integer> ();
        BufferedReader br = new BufferedReader (new FileReader (fichEntrada));
        String linea;

        while ( (linea = br.readLine () ) != null) {
            StringTokenizer st = new StringTokenizer (linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
            while (st.hasMoreTokens () ) {
                String s = st.nextToken();
                Object o = map.get(s);
                if (o == null) map.put (s, new Integer (1));
                else {
                    Integer cont = (Integer) o;
                    map.put (s, new Integer (cont.intValue () + 1));
                }
            }
        }
        br.close ();

        List <String> claves = new ArrayList <String> (map.keySet ());
        Collections.sort (claves);

        PrintWriter pr = new PrintWriter (new FileWriter (fichSalida));
        Iterator <String> i = claves.iterator ();
        while (i.hasNext ()) {
            Object k = i.next ();
            pr.println (k + " : " + map.get (k));
        }
        pr.close ();

    }
}
