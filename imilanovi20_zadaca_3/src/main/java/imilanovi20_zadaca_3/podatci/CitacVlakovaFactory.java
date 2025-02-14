package imilanovi20_zadaca_3.podatci;

import java.nio.file.Path;
import java.util.List;

public class CitacVlakovaFactory extends CitacPodatakaFactory {

    @Override
    public CitacVlakova kreirajCitac(List<Path> filePaths) {
        return new CitacVlakova(filePaths);
    }
}
