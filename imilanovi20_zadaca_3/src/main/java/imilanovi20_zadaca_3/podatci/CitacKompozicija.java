package imilanovi20_zadaca_3.podatci;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imilanovi20_zadaca_3.entiteti.Kompozicija;

public class CitacKompozicija extends CitacPodataka<Kompozicija> {

    public CitacKompozicija(List<Path> filePaths) {
        super(filePaths);
    }

    @Override
    public List<Kompozicija> ucitajPodatke() throws IOException {
        Map<String, List<String>> oznakeVozilaMap = new HashMap<>();
        Map<String, List<String>> ulogeMap = new HashMap<>();
        Path filePath = filePaths.get(0);
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
        	String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] polja = line.split(";");
                if (polja.length >= 3) {
                    Kompozicija kompozicija = kreirajObjekt(polja);
                    if (kompozicija != null) {
                        oznakeVozilaMap.computeIfAbsent(kompozicija.getOznaka(), k -> new ArrayList<>())
                                .addAll(kompozicija.getOznakeVozila());
                        ulogeMap.computeIfAbsent(kompozicija.getOznaka(), k -> new ArrayList<>())
                                .addAll(kompozicija.getUloge());
                    }
                } else {
                    System.err.println("Neispravan zapis u liniji: " + line);
                }
            }
        }

        List<Kompozicija> kompozicije = new ArrayList<>();
        for (String oznaka : oznakeVozilaMap.keySet()) {
            List<String> oznakeVozila = oznakeVozilaMap.get(oznaka);
            List<String> uloge = ulogeMap.get(oznaka);
            if (jeValidnaKompozicija(oznakeVozila, uloge)) {
                kompozicije.add(new Kompozicija(oznaka, oznakeVozila, uloge));
            } else {
                System.err.println("Kompozicija " + oznaka + " nije validna. Mora imati barem dva vozila, od kojih jedno s pogonom.");
            }
        }

        return kompozicije;
    }
    
    private boolean jeValidnaKompozicija(List<String> oznakeVozila, List<String> uloge) {
        if (oznakeVozila.size() < 2) {
            return false;
        }
        for (String uloga : uloge) {
            if (uloga.equalsIgnoreCase("P")) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected Kompozicija kreirajObjekt(String[] polja) {
        try {
            String oznaka = polja[0];
            List<String> oznakeVozila = new ArrayList<>();
            List<String> uloge = new ArrayList<>();
            oznakeVozila.add(polja[1]);
            uloge.add(polja[2]);
            return new Kompozicija(oznaka, oznakeVozila, uloge);
        } catch (Exception e) {
            System.err.println("Pogreška pri kreiranju kompozicije: " + String.join(";", polja));
            return null;
        }
    }
}
