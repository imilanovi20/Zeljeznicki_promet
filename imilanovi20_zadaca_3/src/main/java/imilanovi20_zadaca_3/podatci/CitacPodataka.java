package imilanovi20_zadaca_3.podatci;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class CitacPodataka <T> {
    protected List<Path> filePaths;

    public CitacPodataka(List<Path> filePaths) {
        this.filePaths = filePaths;
    }

    protected abstract T kreirajObjekt(String[] polja);

    public List<T> ucitajPodatke() throws IOException {
        List<T> podaci = new ArrayList<T>();
        Path filePath  = filePaths.get(0);
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] polja = line.split(";");
                T objekt = kreirajObjekt(polja);

                if (objekt != null) {
                    podaci.add(objekt);
                }
            }
        }

        return podaci;
    }
}
