package imilanovi20_zadaca_3.podatci;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imilanovi20_zadaca_3.entiteti.Pruga;
import imilanovi20_zadaca_3.entiteti.ZeljeznickaStanica;

public class CitacPruga extends CitacPodataka<Pruga> {

    public CitacPruga(List<Path> filePaths) {
        super(filePaths);
    }

    @Override
    public List<Pruga> ucitajPodatke() throws IOException {
        List<Pruga> pruge = new ArrayList<>();
        Map<String, List<String[]>> prugeMap = ucitajLinijeIzDatoteke();

        for (String oznaka : prugeMap.keySet()) {
            List<String[]> linijeZaPruge = prugeMap.get(oznaka);

            double ukupnaDuzina = izracunajUkupnuDuzinu(linijeZaPruge);
            List<ZeljeznickaStanica> stanice = kreirajStaniceZaPruge(linijeZaPruge);
            
            if (stanice.size() <= 1) {
                System.err.println("GREŠKA: <Pruga s oznakom " + oznaka + " ima jednu stanicu.>");
                continue;
            }

            String[] poljaZaPrugu = linijeZaPruge.get(0).clone();
            poljaZaPrugu[13] = String.valueOf(ukupnaDuzina);

            Pruga pruga = kreirajObjekt(poljaZaPrugu);
            if (pruga != null) {
                pruga.setStanice(stanice);
                pruga.kreirajIspravnuObicnuRelaciju();
                pruga.kreirajIspravnuObrnutuRelaciju();
                pruge.add(pruga);
            }
        }

        return pruge;
    }

    private Map<String, List<String[]>> ucitajLinijeIzDatoteke() throws IOException {
        Map<String, List<String[]>> prugeMap = new HashMap<>();
        Path filePath = filePaths.get(0);
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] polja = line.split(";");
                if (polja.length > 1) {
                    String oznakaPruge = polja[1];
                    prugeMap.computeIfAbsent(oznakaPruge, k -> new ArrayList<>()).add(polja);
                } else {
                    continue;
                }
            }
        }

        return prugeMap;
    }

    private double izracunajUkupnuDuzinu(List<String[]> linijeZaPruge) {
        double ukupnaDuzina = 0;
        for (String[] linija : linijeZaPruge) {
            if (linija.length > 13) {
                try {
                    ukupnaDuzina += Double.parseDouble(linija[13].replace(",", "."));
                } catch (NumberFormatException e) {
                    System.err.println("Neispravan format dužine u liniji: " + String.join(";", linija));
                }
            } else {
                System.err.println("Nedostaje vrijednost za dužinu u liniji: " + String.join(";", linija));
            }
        }
        return ukupnaDuzina;
    }

    private List<ZeljeznickaStanica> kreirajStaniceZaPruge(List<String[]> linijeZaPruge) {
        List<ZeljeznickaStanica> stanice = new ArrayList<>();
        for (String[] linija : linijeZaPruge) {
            ZeljeznickaStanica stanica = kreirajStanicu(linija);
            if (stanica != null) {
                stanice.add(stanica);
            }
        }
        return stanice;
    }


    @Override
    protected Pruga kreirajObjekt(String[] polja) {
        try {
            return new Pruga.PrugaBuilder()
                    .setOznaka(polja[1])
                    .setKategorija(polja[6])
                    .setNacinPrijevoza(polja[8])
                    .setBrojKolosijeka(Integer.parseInt(polja[9]))
                    .setDopustenoOpterecenjeOsovina(Double.parseDouble(polja[10].replace(",", ".")))
                    .setDopustenoOpterecenjeDuzniMetar(Double.parseDouble(polja[11].replace(",", ".")))
                    .setStatus(polja[12])
                    .setDuzina((int) Double.parseDouble(polja[13].replace(",", ".")))
                    .build();
        } catch (Exception e) {
            System.err.println("Pogreška pri kreiranju objekta pruge: " + String.join(";", polja));
            return null;
        }
    }

    private ZeljeznickaStanica kreirajStanicu(String[] polja) {
        try {
        	Integer vrijemeUbrzaniVlak = (polja.length > 15 && !polja[15].isEmpty()) ? Integer.parseInt(polja[15]) : null;
            Integer vrijemeBrziVlak = (polja.length > 16 && !polja[16].isEmpty()) ? Integer.parseInt(polja[16]) : null;

            ZeljeznickaStanica.ZeljeznickaStanicaBuilder builder = new ZeljeznickaStanica.ZeljeznickaStanicaBuilder()
                    .setNaziv(polja[0])
                    .setVrstaStanice(polja[2])
                    .setStatusStanice(polja[3])
                    .setPutniciUlIiz(polja[4])
                    .setRobaUtIist(polja[5])
                    .setBrojPerona(Integer.parseInt(polja[7]))
                    .setStatusStanice(polja[12])
                    .setDuzina(Integer.parseInt(polja[13]))
                    .setVrijemeNormalniVlak(Integer.parseInt(polja[14]));
            if (vrijemeUbrzaniVlak != null) {
                builder.setVrijemeUbrzaniVlak(vrijemeUbrzaniVlak);
            }
            if (vrijemeBrziVlak != null) {
                builder.setVrijemeBrziVlak(vrijemeBrziVlak);
            }

            return builder.build();
        } catch (Exception e) {
            System.err.println("Pogreška pri kreiranju stanice: " + String.join(";", polja));
            return null;
        }
    }

}
