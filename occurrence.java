import java.io.*;
import java.util.*;

public class occurrence implements Serializable {
    int TF; // File ID
    Map<File, Integer> ocurrences; // Word and the Frequency of the word

    public occurrence() {
        this.TF = 0;
        this.ocurrences = new TreeMap<>();
    }

    public occurrence(int TF, Map<File, Integer> ocurrences) {
        this.TF = TF;
        this.ocurrences = new TreeMap<>(ocurrences);
    }

    public void setTF(int TF) {
        this.TF = TF;
    }

    public int getTF() {
        return TF;
    }

    public Map<File, Integer> getOcurrences() {
        return ocurrences;
    }

    public void setOcurrences(Map<File, Integer> ocurrences) {
        this.ocurrences = ocurrences;
    }

}
