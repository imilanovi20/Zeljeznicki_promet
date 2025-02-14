package imilanovi20_zadaca_3.podatci;

import java.nio.file.Path;
import java.util.List;

public class CitacPrugaFactory extends CitacPodatakaFactory {
    @Override
    public CitacPruga kreirajCitac(List<Path> filePaths) {
        return new CitacPruga(filePaths);
    }

}
