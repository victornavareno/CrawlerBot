/*
 * SalvarObjeto.java: Guarda un objeto serializable en un fichero
 * (i) Felix R. Rguez., EPCC, Universidad de Extremadura, 2009-23
 * http://madiba.unex.es/
 */

import java.io.*;
import java.util.*;

public class SalvarObjeto {
    public static void main (String args[]) {
        Hashtable <String, Object> h = new Hashtable <String, Object> ();
        h.put("String","Luis Rodriguez Duran");
        h.put("Integer",new Integer(23));
        h.put("Double",new Double(0.96));
        /* 
         * en el caso de nuestro PC-Crawler ha de utilizarse la estructura Heap
         * Map <String, Integer> map
         */
        try {
            FileOutputStream fos = new FileOutputStream("diccionario.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(h);
        }
        catch (Exception e) { System.out.println(e); }
    }
}
