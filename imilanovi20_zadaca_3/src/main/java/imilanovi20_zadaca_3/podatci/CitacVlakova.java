package imilanovi20_zadaca_3.podatci;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imilanovi20_zadaca_3.entiteti.Pruga;
import imilanovi20_zadaca_3.entiteti.ZeljeznickaStanica;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Etapa;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Vlak;

public class CitacVlakova extends CitacPodataka<Vlak> {

	private List<Pruga> pruge;
	private List<Path> putanjaPruge;
	private Path putanjaDani;
	private Path putanjaVozniRed;

	public CitacVlakova(List<Path> filePaths) {
		super(filePaths);
		try {
			this.putanjaPruge = List.of(filePaths.get(2));
			this.putanjaDani = filePaths.get(1);
			this.putanjaVozniRed = filePaths.get(0);

			CitacPodatakaFactory prugeFactory = new CitacPrugaFactory();
			CitacPruga citacPruga = (CitacPruga) prugeFactory.kreirajCitac(putanjaPruge);
			this.pruge = citacPruga.ucitajPodatke();
		} catch (IOException e) {
			System.err.println("Greška pri učitavanju pruga: " + e.getMessage());
		}
	}

	@Override
	protected Vlak kreirajObjekt(String[] polja) {
	    try {
	        if (polja.length <= 1) {
	            return null;
	        }
	        String oznakaVlaka = polja[4];
	        List<Etapa> etape = kreirajEtapeZaVlak(oznakaVlaka);
	        
	        etape.sort(Comparator.comparingInt(etapa -> pretvoriVrijemeUMinute(etapa.getVrijemePolaska())));
	        
	        if (!provjeriPreklapanjeVremena(etape)) {
	            return null;
	        }
	        
	        Vlak vlak = new Vlak(oznakaVlaka);
	        if (!etape.isEmpty()) {
	            for (Etapa etapa : etape) {
	                vlak.dodajEtapu(etapa);
	            }
	        }
	        return vlak;
	    } catch (Exception e) {
	        System.err.println("Greška pri kreiranju vlaka: " + e.getMessage());
	        return null;
	    }
		
	}

	private List<Etapa> kreirajEtapeZaVlak(String oznakaVlaka) {
	    List<Etapa> etape = new ArrayList<>();
	    try (BufferedReader reader = Files.newBufferedReader(putanjaVozniRed)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.trim().isEmpty())
	                continue;
	            String[] polja = line.split(";");
	            
	            if (polja.length <=4) {
	                continue;
	            }
	            
	            if (oznakaVlaka.equals(polja[4])) {
	                Etapa objekt = kreirajEtapu(polja);
	                if (objekt != null) {
	                    etape.add(objekt);
	                }
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Greška prilikom čitanja datoteke: " + e.getMessage());
	    }
	    return etape;
	}


	private Etapa kreirajEtapu(String[] polja) {
		try {
			Map<String, String> oznakeDana = dohvatiOznakeDana(putanjaDani);
			
			String oznakaPruge = polja[0];
			String vlak = polja[4];
			String oznakaVlaka = polja[5].isEmpty() ? "N" : polja[5];
			String smjer = polja[1];
			String vrijemePolaska = polja[6];
			String trajanje = polja[7];
			String oznakaDana = polja.length > 8 ? polja[8] : "";

			Pruga pruga = pronadjiPrugu(oznakaPruge);

			if (pruga == null) {
				System.err.println("Pruge nije pronađena za oznaku: " + oznakaPruge);
				return null;
			}

			List<ZeljeznickaStanica> staniceEtape = dohvatiStaniceEtape(pruga, polja);

			if (staniceEtape.isEmpty()) {
				System.err.println("Nema stanica za etapu na pruzi: " + oznakaPruge);
				return null;
			}

			String daniUTjednu = oznakeDana.getOrDefault(oznakaDana, "PoUSrČPeSuN");

			String vrijemeDolaska = izracunajVrijemeDolaska(vrijemePolaska, trajanje);

			return new Etapa(oznakaPruge, smjer, vlak, oznakaVlaka, staniceEtape, vrijemePolaska, vrijemeDolaska, daniUTjednu);
		} catch (Exception e) {
			System.err.println("Greška pri kreiranju etape: " + String.join(";", polja) + " - " + e.getMessage());
			return null;
		}
	}
	
	private Map<String, String> dohvatiOznakeDana(Path filePath) {
		Map<String, String> oznakeDana = new HashMap<>();
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				String[] polja = line.split(";");
				if (polja.length >= 2) {
					oznakeDana.put(polja[0], polja[1]);
				}
			}
		} catch (IOException e) {
			System.err.println("Greška pri učitavanju oznaka dana: " + e.getMessage());
		}
		return oznakeDana;
	}
	
	private List<ZeljeznickaStanica> dohvatiStaniceEtape(Pruga pruga, String[] polja) {
		String smjer = polja[1];
	    String polaznaStanica = polja[2].isEmpty() ? null : polja[2];
	    String odredisnaStanica = polja[3].isEmpty() ? null : polja[3];

	    List<ZeljeznickaStanica> staniceNaPrugi = pruga.getStanice();
	    if ("O".equals(smjer)) {
	        List<ZeljeznickaStanica> obrnuteStanice = new ArrayList<>(staniceNaPrugi);
	        Collections.reverse(obrnuteStanice);
	        staniceNaPrugi = obrnuteStanice;
	    }

	    ZeljeznickaStanica polazna = polaznaStanica == null
	            ? staniceNaPrugi.get(0)
	            : staniceNaPrugi.stream().filter(s -> s.getNaziv().equals(polaznaStanica)).findFirst().orElse(null);

	    ZeljeznickaStanica odredisna = odredisnaStanica == null
	            ? staniceNaPrugi.get(staniceNaPrugi.size() - 1)
	            : staniceNaPrugi.stream().filter(s -> s.getNaziv().equals(odredisnaStanica)).findFirst().orElse(null);

	    if (polazna == null || odredisna == null) {
	        System.err.println("Polazna ili odredišna stanica nije pronađena za prugu: " + pruga.getOznaka());
	        return new ArrayList<>();
	    }

	    int polaznaIndex = staniceNaPrugi.indexOf(polazna);
	    int odredisnaIndex = staniceNaPrugi.indexOf(odredisna);
	    if (polaznaIndex > odredisnaIndex) {
	        return staniceNaPrugi.subList(odredisnaIndex, polaznaIndex + 1);
	    } else {
	        return staniceNaPrugi.subList(polaznaIndex, odredisnaIndex + 1);
	    }
	}

	
	private Pruga pronadjiPrugu(String oznakaPruge) {
	    for (Pruga pruga : pruge) {
	        if (pruga.getOznaka().equals(oznakaPruge)) {
	            return pruga;
	        }
	    }
	    return null;
	}
	
	private String izracunajVrijemeDolaska(String vrijemePolaska, String trajanje) {
	    String[] poljeVrijemePolaska = vrijemePolaska.split(":");
	    String[] poljeTrajanje = trajanje.split(":");

	    int satiPolaska = Integer.parseInt(poljeVrijemePolaska[0]);
	    int minutePolaska = Integer.parseInt(poljeVrijemePolaska[1]);

	    int satiTrajanje = Integer.parseInt(poljeTrajanje[0]);
	    int minuteTrajanje = Integer.parseInt(poljeTrajanje[1]);

	    int ukupneMinute = (satiPolaska * 60 + minutePolaska) + (satiTrajanje * 60 + minuteTrajanje);

	    int satiDolaska = (ukupneMinute / 60) % 24;
	    int minuteDolaska = ukupneMinute % 60;

	    return String.format("%02d:%02d", satiDolaska, minuteDolaska);
	}
	
	private boolean provjeriPreklapanjeVremena(List<Etapa> etape) {
	    for (int i = 0; i < etape.size() - 1; i++) {
	        Etapa trenutnaEtapa = etape.get(i);
	        Etapa sljedecaEtapa = etape.get(i + 1);

	        int dolaznoVrijemeTrenutne = pretvoriVrijemeUMinute(trenutnaEtapa.getVrijemeDolaska());
	        int polaznoVrijemeSljedece = pretvoriVrijemeUMinute(sljedecaEtapa.getVrijemePolaska());


	        String stvarnoVrijemeDolaskaString = trenutnaEtapa.dohvatiPolazakSaStanice(
	                trenutnaEtapa.getStanice().get(trenutnaEtapa.getStanice().size() - 1));
	        int stvarnoVrijemeDolaska = pretvoriVrijemeUMinute(stvarnoVrijemeDolaskaString);

	        int dolaznoVrijemeTrenutneEtape = pretvoriVrijemeUMinute(trenutnaEtapa.getVrijemeDolaska());

	    }
	    return true;
	}

	private int pretvoriVrijemeUMinute(String vrijeme) {
	    String[] dijelovi = vrijeme.split(":");
	    int sati = Integer.parseInt(dijelovi[0]);
	    int minute = Integer.parseInt(dijelovi[1]);
	    return sati * 60 + minute;
	}
}