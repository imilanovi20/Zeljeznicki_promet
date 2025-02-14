package imilanovi20_zadaca_3.entiteti;

import java.time.DayOfWeek;
import java.time.LocalDate;

import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Etapa;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.KomponentaVoznogReda;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Vlak;
import imilanovi20_zadaca_3.strategijePlacanja.PlacanjeStartegy;
import imilanovi20_zadaca_3.upravljanje_kartama.KartaMemento;

public class Karta {
	private CijenikKarte cijenik;
	private Vlak vlak;
	private ZeljeznickaStanica polaznaStanica;
	private ZeljeznickaStanica odresdisnaStanica;
	private LocalDate datum;
	private PlacanjeStartegy strategijaPlacanja;
	
    public Karta(CijenikKarte cijenik, Vlak vlak, ZeljeznickaStanica polaznaStanica,
            ZeljeznickaStanica odresdisnaStanica, LocalDate datum, PlacanjeStartegy strategijaPlacanja) {
	   this.cijenik = cijenik;
	   this.vlak = vlak;
	   this.polaznaStanica = polaznaStanica;
	   this.odresdisnaStanica = odresdisnaStanica;
	   this.datum = datum;
	   this.strategijaPlacanja = strategijaPlacanja;
	}
    
    public KartaMemento spremiStanje() {
        String oznakaVlaka = vlak.getOznaka();
        String pocetnaStanica = polaznaStanica.getNaziv();
        String odredisnaStanica = odresdisnaStanica.getNaziv();
        double osnovnaCijena = odrediOsnovnuCijenu() * vlak.izracunajUkupneKilometreIzmeduStanica(polaznaStanica, odresdisnaStanica);
        double popustSuN = odrediPopustSuN();
        double konacnaCijena = izracunajCijenuKarte();
        String nacinPlacanja = strategijaPlacanja.getNazivStrategije();
        
        String vrijemePolaska;
        String vrijemeDolaska;

        try {
            vrijemePolaska = dohvatiVrijemeZaStanicu(polaznaStanica);
            vrijemeDolaska = dohvatiVrijemeZaStanicu(odresdisnaStanica);
        } catch (IllegalArgumentException e) {
            vrijemePolaska = "Nepoznato";
            vrijemeDolaska = "Nepoznato";
        }

        return new KartaMemento(oznakaVlaka, pocetnaStanica, odredisnaStanica, datum, osnovnaCijena, popustSuN,
        		konacnaCijena, nacinPlacanja, vrijemePolaska, vrijemeDolaska);
    }
	
	private double izracunajCijenuKarte() {
		double popustSuN = odrediPopustSuN();
		double osnovnaCijena = odrediOsnovnuCijenu();
		int kilometri = vlak.izracunajUkupneKilometreIzmeduStanica(polaznaStanica, odresdisnaStanica);

		return strategijaPlacanja.izracunajCijenuKarte(osnovnaCijena, kilometri, cijenik, popustSuN);
	}

	private double odrediOsnovnuCijenu() {
	    String vrstaVlaka = odrediVrstuVlaka();

	    switch (vrstaVlaka) {
	        case "N":
	            return cijenik.getCijenaNormalni();
	        case "U":
	            return cijenik.getCijenaUbrzani();
	        case "B":
	            return cijenik.getCijenaBrzi();
	        default:
	            throw new IllegalArgumentException("Nepoznata vrsta vlaka: " + vrstaVlaka);
	    }
	}

	private String odrediVrstuVlaka() {
		Etapa prvaEtapa = (Etapa) vlak.getEtape().get(0);
	    return prvaEtapa.getVrtstaVlaka();
	}
	
	private String dohvatiVrijemeZaStanicu(ZeljeznickaStanica stanica) {
	    for (KomponentaVoznogReda etapa : vlak.getEtape()) {
	    	Etapa etapaZaPretragu = (Etapa) etapa;
	        if (etapaZaPretragu.getStanice().contains(stanica)) {
	            return etapaZaPretragu.dohvatiPolazakSaStanice(stanica);
	        }
	    }
	    throw new IllegalArgumentException("Stanica nije pronađena u etapama vlaka.");
	}


	private double odrediPopustSuN() {
	    DayOfWeek danUTjednu = datum.getDayOfWeek();
	    if (danUTjednu == DayOfWeek.SATURDAY || danUTjednu == DayOfWeek.SUNDAY) {
	        return cijenik.getPopustSuN(); 
	    } else {
	        return 0;
	    }
	}
	
	
	public void ispisiKartu() {
	    System.out.println("------------------------------------------------");
	    System.out.printf("Oznaka vlaka: %s\n", vlak.getOznaka());
	    System.out.printf("Relacija: %s - %s\n", polaznaStanica.getNaziv(), odresdisnaStanica.getNaziv());
	    System.out.printf("Datum putovanja: %s\n", datum);
	    
	    try {
	        String vrijemePolaska = dohvatiVrijemeZaStanicu(polaznaStanica);
	        String vrijemeDolaska = dohvatiVrijemeZaStanicu(odresdisnaStanica);

	        System.out.printf("Vrijeme polaska sa stanice %s: %s\n", polaznaStanica.getNaziv(), vrijemePolaska);
	        System.out.printf("Vrijeme dolaska na stanicu %s: %s\n", odresdisnaStanica.getNaziv(), vrijemeDolaska);
	    } catch (IllegalArgumentException e) {
	        System.out.println("Greška: " + e.getMessage());
	    }
	    
	    double osnovnaCijena = odrediOsnovnuCijenu() * vlak.izracunajUkupneKilometreIzmeduStanica(polaznaStanica, odresdisnaStanica);
	    double popustSuN = odrediPopustSuN();
	    double konacnaCijena = izracunajCijenuKarte();

	    System.out.printf("Izvorna cijena: %.2f €\n", osnovnaCijena);
	    System.out.printf("Popust vikendom: %.2f %%\n", popustSuN);
	    System.out.printf("Konačna cijena: %.2f €\n", konacnaCijena);

	    System.out.printf("Način kupovine karte: %s\n", strategijaPlacanja.getNazivStrategije());


	    System.out.println("------------------------------------------------");
	}
}
