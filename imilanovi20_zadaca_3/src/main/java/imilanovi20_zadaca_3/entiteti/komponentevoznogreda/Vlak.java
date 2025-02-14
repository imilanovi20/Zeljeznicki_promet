package imilanovi20_zadaca_3.entiteti.komponentevoznogreda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import imilanovi20_zadaca_3.entiteti.Relacija;
import imilanovi20_zadaca_3.entiteti.ZeljeznickaStanica;
import imilanovi20_zadaca_3.entiteti.korisnik_simulacije.Subject;

public class Vlak extends Subject implements KomponentaVoznogReda {
	private String oznaka;

	private List<KomponentaVoznogReda> etape;

	public Vlak(String oznaka) {
		this.oznaka = oznaka;
		this.etape = new ArrayList<>();
	}

	@Override
	public int getDuzina() {
		int ukupnaDuzina = 0;
		for (KomponentaVoznogReda etapa : etape) {
			ukupnaDuzina += etapa.getDuzina();
		}
		return ukupnaDuzina;
	}

	public void dodajEtapu(KomponentaVoznogReda etapa) {
		etape.add(etapa);
	}

	public void ukloniEtapu(KomponentaVoznogReda etapa) {
		etape.remove(etapa);
	}

	public List<KomponentaVoznogReda> getEtape() {
		if (etape.isEmpty()) {
			System.out.println("Nema etapa za ovaj vlak.");
			return new ArrayList<>();
		}
		return etape;
	}

	public String getOznaka() {
		return oznaka;
	}

    public ZeljeznickaStanica getPolaznaStanica() {
        if (!etape.isEmpty() && etape.get(0) instanceof Etapa) {
            Etapa prvaEtapa = (Etapa) etape.get(0);
            return prvaEtapa.getPolaznaStanica();
        }
        return null;
    }

    public ZeljeznickaStanica getOdredisnaStanica() {
        if (!etape.isEmpty() && etape.get(etape.size() - 1) instanceof Etapa) {
            Etapa zadnjaEtapa = (Etapa) etape.get(etape.size() - 1);
            return zadnjaEtapa.getOdredisnaStanica();
        }
        return null;
    }

    public String getVrijemePolaska() {
        if (etape.get(0) instanceof Etapa) {
            Etapa prvaEtapa = (Etapa) etape.get(0);
            return prvaEtapa.getVrijemePolaska();
        }

        return "N/A";
    }

    public String getVrijemeDolaska() {
        if (etape.get(etape.size() - 1) instanceof Etapa) {
            Etapa zadnjaEtapa = (Etapa) etape.get(etape.size() - 1);
            return zadnjaEtapa.getVrijemeDolaska();
        }

        return "N/A";
    }
    
    public List<ZeljeznickaStanica> getStanice() {
        List<ZeljeznickaStanica> sveStanice = new ArrayList<>();
        for (KomponentaVoznogReda etapa : etape) {
            if (etapa instanceof Etapa) {
                sveStanice.addAll(((Etapa) etapa).getStanice());
            }
        }
        return sveStanice;
    }
    
    public void pokreniSimulaciju(String dan, int koeficijent) {
        System.out.println("Simulacija započeta za vlak " + getOznaka() + " na dan: " + dan);
        System.out.println("-------------------------------------------------------------------------------");

        List<ZeljeznickaStanica> stanice = getStanice();
        List<KomponentaVoznogReda> etape = getEtape();

        if (stanice.isEmpty()) {
            System.out.println("Vlak " + getOznaka() + " nema stanica u svom voznom redu.");
            return;
        }
        ZeljeznickaStanica trenutnaStanica = stanice.get(0);
        Etapa trenutnaEtapa = (Etapa) etape.get(0);
        String vrijemePolaska = dohvatiVrijemePolaskaZaStanicu(trenutnaStanica, dan, trenutnaEtapa);

        if (vrijemePolaska == null) {
            System.out.println("Vrijeme polaska nije definirano za stanicu: " + trenutnaStanica.getNaziv() + " na dan " + dan);
            return;
        }

        System.out.println("=> Vlak " + getOznaka() + " krenuo sa stanice " + trenutnaStanica.getNaziv() + " u " + vrijemePolaska);
        obavijestiPretplatnike("Vlak " + getOznaka() + " krenuo sa stanice " + trenutnaStanica.getNaziv() + " u " + vrijemePolaska);
        trenutnaStanica.obavijestiPretplatnike("Vlak " + getOznaka() + " krenuo sa stanice " + trenutnaStanica.getNaziv() + " u " + vrijemePolaska);

        for (int i = 1; i < stanice.size(); i++) {
            ZeljeznickaStanica prethodnaStanica = stanice.get(i - 1);
            trenutnaStanica = stanice.get(i);
            for (KomponentaVoznogReda komponenta : etape) {
                Etapa etapa = (Etapa) komponenta;
                if (etapa.getStanice().contains(trenutnaStanica)) {
                    trenutnaEtapa = etapa;
                    break;
                }
            }

            String vrijemeTrenutneStanice = trenutnaEtapa.dohvatiPolazakSaStanice(trenutnaStanica);
            String vrijemePrethodneStanice = trenutnaEtapa.dohvatiPolazakSaStanice(prethodnaStanica);
            if (vrijemeTrenutneStanice != null && vrijemePrethodneStanice != null && vrijemeTrenutneStanice.equals(vrijemePrethodneStanice)) {
                System.out.println("PRESKAČEM: " + trenutnaStanica.getNaziv());
                continue;
            }

            if (vrijemeTrenutneStanice == null) {
                System.out.println("Stanica " + trenutnaStanica.getNaziv() + " nema definirano vrijeme dolaska za dan " + dan);
                continue;
            }

            int vrijemePolaskaUMinutama = pretvoriVrijemeUMinute(vrijemePolaska);
            int vrijemeDolaskaUMinutama = pretvoriVrijemeUMinute(vrijemeTrenutneStanice);
            int razlikaUMinutama = vrijemeDolaskaUMinutama - vrijemePolaskaUMinutama;

            try {
                Thread.sleep((razlikaUMinutama * 1000) / koeficijent);

                vrijemePolaska = vrijemeTrenutneStanice;
                String obavijest = String.format("Vlak %s stigao u stanicu %s u %s", getOznaka(), trenutnaStanica.getNaziv(), vrijemeTrenutneStanice);
                System.out.println("-------------------------------------------------------------------------------");
                System.out.println("=>" + obavijest);
                obavijestiPretplatnike(obavijest);
                trenutnaStanica.obavijestiPretplatnike(obavijest);
                System.out.println("-------------------------------------------------------------------------------");

            } catch (InterruptedException e) {
                System.err.println("Simulacija prekinuta zbog greške: " + e.getMessage());
                return;
            }
            if (provjeriUnosPrekida()) {
                System.out.println("Simulacija prekinuta na zahtjev korisnika.");
                return;
            }
        }

        System.out.println("Simulacija završena za vlak " + getOznaka());
    }



    private int pretvoriVrijemeUMinute(String vrijeme) {
        String[] dijelovi = vrijeme.split(":");
        int sati = Integer.parseInt(dijelovi[0]);
        int minute = Integer.parseInt(dijelovi[1]);
        return sati * 60 + minute;
    }

    private boolean provjeriUnosPrekida() {
        try {
            if (System.in.available() > 0) {
                int unos = System.in.read();
                return unos == 'X' || unos == 'x';
            }
        } catch (Exception e) {
            System.err.println("Greška pri provjeri unosa za prekid: " + e.getMessage());
        }
        return false;
    }
    
    private String dohvatiVrijemePolaskaZaStanicu(ZeljeznickaStanica stanica, String dan, Etapa etapa) {
            if (etapa.getDaniUTjednu().contains(dan)) {
                return etapa.dohvatiPolazakSaStanice(stanica);
            }
        return null;
    }

	public int izracunajUkupneKilometreIzmeduStanica(ZeljeznickaStanica polaznaStanica,
			ZeljeznickaStanica odresdisnaStanica) {
		
		List<ZeljeznickaStanica> sveStanice = getStanice();
	    int indexPolazne = sveStanice.indexOf(polaznaStanica);
	    int indexOdredisne = sveStanice.indexOf(odresdisnaStanica);

	    if (indexPolazne == -1 || indexOdredisne == -1) {
	        throw new IllegalArgumentException("Jedna od stanica nije pronađena na ruti vlaka.");
	    }

	    if (indexPolazne >= indexOdredisne) {
	        throw new IllegalArgumentException("Polazna stanica mora biti prije odredišne stanice na ruti.");
	    }

	    int ukupniKilometri = 0;
	    for (int i = indexPolazne+1; i <= indexOdredisne; i++) {
	        ukupniKilometri += sveStanice.get(i).getDuzina();
	    }

	    return ukupniKilometri;
	}
	
	public boolean jeLiDozvoljenaVoznja(ZeljeznickaStanica polaznaStanica, ZeljeznickaStanica odredisnaStanica) {
	    List<ZeljeznickaStanica> sveStanice = getStanice();
	    int indexPolazne = sveStanice.indexOf(polaznaStanica);
	    int indexOdredisne = sveStanice.indexOf(odredisnaStanica);

	    if (indexPolazne == -1 || indexOdredisne == -1) {
	        throw new IllegalArgumentException("Jedna od stanica nije pronađena na ruti vlaka.");
	    }

	    if (indexPolazne >= indexOdredisne) {
	        throw new IllegalArgumentException("Polazna stanica mora biti prije odredišne stanice na ruti.");
	    }

	    List<ZeljeznickaStanica> segment = sveStanice.subList(indexPolazne, indexOdredisne + 1);

	    for (KomponentaVoznogReda komponenta : getEtape()) {
	        if (komponenta instanceof Etapa) {
	            Etapa etapa = (Etapa) komponenta;

	            List<Relacija> relacije = etapa.getPruga().getRelacije();

	            for (Relacija relacija : relacije) {
	                List<ZeljeznickaStanica> staniceRelacije = relacija.getStanice();

	                if (isPodlista(staniceRelacije, segment)) {
	                    if (!relacija.dozvoljenoPutovanje()) {
	                    	System.out.println("----------------------------------------------");
	                    	System.out.println("Nije moguća vožnja: ");
	                    	relacija.ispisRelacije();
	                    	System.out.println("----------------------------------------------");
	                        return false;
	                    }
	                }
	            }
	        }
	    }

	    return true;
	}


	private boolean isPodlista(List<ZeljeznickaStanica> relacija, List<ZeljeznickaStanica> segment) {
		
	    int indexRelacija = -1;
	    int indexSegment = -1;
	    
	    pronadiZajednicku:
	    	for (ZeljeznickaStanica stanicaSegment : segment) {
	    	    for (ZeljeznickaStanica stanicaRelacija : relacija) {
	    	        if (stanicaSegment.getNaziv().equals(stanicaRelacija.getNaziv())) {
	    	            indexRelacija = relacija.indexOf(stanicaRelacija) + 1;
	    	            indexSegment = segment.indexOf(stanicaSegment) + 1;
	    	            break pronadiZajednicku;
	    	        }
	    	    }
	    	}

	    if (indexRelacija == -1 || indexSegment == -1) {
	        return false;
	    }


	    ZeljeznickaStanica slijedecaStanicaSegment = null;
	    if (indexSegment < segment.size()) {
	        slijedecaStanicaSegment = segment.get(indexSegment);
	    }

	    ZeljeznickaStanica slijedecaStanicaRelacija = null;
	    if (indexRelacija < relacija.size()) {
	        slijedecaStanicaRelacija = relacija.get(indexRelacija);
	    }

	    if (slijedecaStanicaSegment != null && slijedecaStanicaRelacija != null) {
	        return slijedecaStanicaSegment.getNaziv().equals(slijedecaStanicaRelacija.getNaziv());
	    }

	    return false;
	}



}
