import java.io.File;

public class Ocurrencia implements Comparable<Ocurrencia> {
    private File file;
    private int count;

    public Ocurrencia(File file, int count) {
        this.file = file;
        this.count = count;
    }

    public File getFile() {
        return file;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return file.getAbsolutePath() + " : " + count;
    }

    // Optional: This can help if you want to sort your occurrences by file name.
    @Override
    public int compareTo(Ocurrencia other) {
        return this.file.getAbsolutePath().compareTo(other.file.getAbsolutePath());
    }
}
