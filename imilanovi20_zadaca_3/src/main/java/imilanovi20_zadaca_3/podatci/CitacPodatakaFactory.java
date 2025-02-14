package imilanovi20_zadaca_3.podatci;

import java.nio.file.Path;
import java.util.List;

public abstract class CitacPodatakaFactory {
    public abstract CitacPodataka kreirajCitac(List<Path> filePaths);
}