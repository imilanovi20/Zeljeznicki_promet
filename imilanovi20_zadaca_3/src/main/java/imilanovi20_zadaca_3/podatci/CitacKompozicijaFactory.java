package imilanovi20_zadaca_3.podatci;

import java.nio.file.Path;
import java.util.List;

public class CitacKompozicijaFactory extends CitacPodatakaFactory {

    @Override
    public CitacKompozicija kreirajCitac(List<Path> filePaths) {
        return new CitacKompozicija(filePaths);
    }
}
