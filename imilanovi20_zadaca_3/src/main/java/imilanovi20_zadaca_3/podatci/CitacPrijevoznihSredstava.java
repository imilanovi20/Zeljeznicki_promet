package imilanovi20_zadaca_3.podatci;

import java.nio.file.Path;
import java.util.List;

import imilanovi20_zadaca_3.entiteti.PrijevoznoSredstvo;

public class CitacPrijevoznihSredstava extends CitacPodataka<PrijevoznoSredstvo> {

    public CitacPrijevoznihSredstava(List<Path> filePaths) {
        super(filePaths);
    }

    @Override
    protected PrijevoznoSredstvo kreirajObjekt(String[] polja) {
        try {
            return new PrijevoznoSredstvo.PrijevoznoSredstvoBuilder()
                    .setOznaka(polja[0])
                    .setOpis(polja[1])
                    .setProizvodac(polja[2]) 
                    .setGodinaProizvodnje(Integer.parseInt(polja[3]))
                    .setNamjena(polja[4])
                    .setVrstaPrijevoza(polja[5])
                    .setVrstaPogona(polja[6])
                    .setMaksimalnaBrzina(Integer.parseInt(polja[7]))
                    .setMaksimalnaSnaga(Double.parseDouble(polja[8].replace(",", ".")))
                    .setBrojSjedecihMjesta(Integer.parseInt(polja[9]))
                    .setBrojStajacihMjesta(Integer.parseInt(polja[10]))
                    .setBrojBicikala(Integer.parseInt(polja[11]))
                    .setBrojKreveta(Integer.parseInt(polja[12]))
                    .setBrojAutomobila(Integer.parseInt(polja[13]))
                    .setNosivost(Double.parseDouble(polja[14].replace(",", ".")))
                    .setPovrsina(Double.parseDouble(polja[15].replace(",", ".")))
                    .setZapremina(Double.parseDouble(polja[16].replace(",", ".")))
                    .setStatus(polja[17])
                    .build();
        } catch (Exception e) {
            System.err.println("Pogreška pri parsiranju retka: " + String.join(";", polja));
            return null;
        }
    }

}
