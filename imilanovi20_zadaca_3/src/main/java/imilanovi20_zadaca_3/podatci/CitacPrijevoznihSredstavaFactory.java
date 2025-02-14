package imilanovi20_zadaca_3.podatci;

import java.nio.file.Path;
import java.util.List;

public class CitacPrijevoznihSredstavaFactory extends CitacPodatakaFactory {

    @Override
    public CitacPrijevoznihSredstava kreirajCitac(List<Path> filePaths) {
        return new CitacPrijevoznihSredstava(filePaths);
    }
}

