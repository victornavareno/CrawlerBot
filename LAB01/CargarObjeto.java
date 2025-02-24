/*
 * CargarObjeto.java: Lee un objeto serializable, previamente salvado en un fichero, a memoria
 * (i) Felix R. Rguez., EPCC, Universidad de Extremadura, 2009-23
 * http://madiba.unex.es/
 */

import java.io.*;
import java.util.*;

public class CargarObjeto {
    public static void main (String args[]) {
        try {
            FileInputStream fis = new FileInputStream("diccionario.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Hashtable h = (Hashtable) ois.readObject();
            /* en el caso de nuestro PC-Crawler ha de utilizarse la estructura Heap:
             * Map <String, Integer> map = TreeMap <String, Integer> ois.readObject();
             */
            System.out.println(h.toString());
        }
        catch (Exception e) { System.out.println(e); }
    }
}
