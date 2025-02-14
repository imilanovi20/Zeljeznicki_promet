package imilanovi20_zadaca_3;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import imilanovi20_zadaca_3.entiteti.CijenikKarte;
import imilanovi20_zadaca_3.entiteti.Kompozicija;
import imilanovi20_zadaca_3.entiteti.PrijevoznoSredstvo;
import imilanovi20_zadaca_3.entiteti.Pruga;
import imilanovi20_zadaca_3.entiteti.Relacija;
import imilanovi20_zadaca_3.entiteti.ZeljeznickaStanica;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Etapa;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.KomponentaVoznogReda;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Vlak;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.VozniRed;
import imilanovi20_zadaca_3.entiteti.korisnik_simulacije.Korisnik;
import imilanovi20_zadaca_3.entiteti.korisnik_simulacije.Observer;
import imilanovi20_zadaca_3.podatci.CitacKompozicija;
import imilanovi20_zadaca_3.podatci.CitacKompozicijaFactory;
import imilanovi20_zadaca_3.podatci.CitacPodatakaFactory;
import imilanovi20_zadaca_3.podatci.CitacPrijevoznihSredstava;
import imilanovi20_zadaca_3.podatci.CitacPrijevoznihSredstavaFactory;
import imilanovi20_zadaca_3.podatci.CitacPruga;
import imilanovi20_zadaca_3.podatci.CitacPrugaFactory;
import imilanovi20_zadaca_3.podatci.CitacVlakova;
import imilanovi20_zadaca_3.podatci.CitacVlakovaFactory;
import imilanovi20_zadaca_3.strategijePlacanja.PlacanjeBlagajnaStrategy;
import imilanovi20_zadaca_3.strategijePlacanja.PlacanjeStartegy;
import imilanovi20_zadaca_3.strategijePlacanja.VlakPlacanjeStrategy;
import imilanovi20_zadaca_3.strategijePlacanja.WebMobPlacanjeStrategy;
import imilanovi20_zadaca_3.upravljanje_kartama.UpraviteljKartama;

public class ZeljeznickaTvrtka {

	private static volatile ZeljeznickaTvrtka instanca;

	private List<PrijevoznoSredstvo> prijevoznaSredstva;
	private List<Pruga> pruge;
	private List<Kompozicija> kompozicije;
	private KomponentaVoznogReda vozniRed = new VozniRed();
	private List<Observer> korisnici = new ArrayList<>();
	private CijenikKarte cijenikKarte = new CijenikKarte();
	private UpraviteljKartama upraviteljKartama = new UpraviteljKartama();

	private ZeljeznickaTvrtka() {
		this.prijevoznaSredstva = new ArrayList<>();
	}

	public static ZeljeznickaTvrtka getInstance() {
		if (instanca == null) {
			synchronized (ZeljeznickaTvrtka.class) {
				if (instanca == null) {
					instanca = new ZeljeznickaTvrtka();
				}
			}
		}
		return instanca;
	}

	public void inicijalizirajTvrtku(Path putanjaPrijevoznaSredstva, Path putanjaStanicePruge, Path putanjaKompozicije,
			Path putanjaVozniRed, Path putanjaDani) {
		try {
			CitacPodatakaFactory vozilaFactory = new CitacPrijevoznihSredstavaFactory();
			CitacPodatakaFactory prugeFactory = new CitacPrugaFactory();
			CitacPodatakaFactory kompozicijeFactory = new CitacKompozicijaFactory();
			CitacPodatakaFactory vlakoviFactory = new CitacVlakovaFactory();

			List<Path> putanjePrijevoznaSredstva = List.of(putanjaPrijevoznaSredstva);
			List<Path> putanjeStanicePruge = List.of(putanjaStanicePruge);
			List<Path> putanjeKompozicije = List.of(putanjaKompozicije);
			List<Path> putanjeVlakovi = List.of(putanjaVozniRed, putanjaDani, putanjaStanicePruge);

			CitacPrijevoznihSredstava citacVozila = (CitacPrijevoznihSredstava) vozilaFactory
					.kreirajCitac(putanjePrijevoznaSredstva);
			CitacPruga citacPruga = (CitacPruga) prugeFactory.kreirajCitac(putanjeStanicePruge);
			CitacKompozicija citacKompozicija = (CitacKompozicija) kompozicijeFactory.kreirajCitac(putanjeKompozicije);
			CitacVlakova citacVlakova = (CitacVlakova) vlakoviFactory.kreirajCitac(putanjeVlakovi);

			prijevoznaSredstva = citacVozila.ucitajPodatke();
			pruge = citacPruga.ucitajPodatke();
			kompozicije = citacKompozicija.ucitajPodatke();
			List<Vlak> vlakovi = citacVlakova.ucitajPodatke();

			Set<String> dodaneOznake = new HashSet<>();

			for (Vlak vlak : vlakovi) {
				if (!dodaneOznake.contains(vlak.getOznaka())) {
					((VozniRed) vozniRed).dodajVlak(vlak);
					dodaneOznake.add(vlak.getOznaka());
				}
			}

			azurirajPruge();

		} catch (IOException e) {
			System.err.println("Pogreška pri učitavanju datoteka: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Neočekivana pogreška: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void azurirajPruge() {
		if (vozniRed instanceof VozniRed) {
			VozniRed vozniRedObjekt = (VozniRed) vozniRed;
			for (KomponentaVoznogReda komponentaVlaka : vozniRedObjekt.getVlakovi()) {
				if (komponentaVlaka instanceof Vlak) {
					Vlak vlak = (Vlak) komponentaVlaka;
					for (KomponentaVoznogReda komponentaEtape : vlak.getEtape()) {
						if (komponentaEtape instanceof Etapa) {
							Etapa etapa = (Etapa) komponentaEtape;
							String oznakaPruge = etapa.getOznakaPruge();
							for (Pruga pruga : pruge) {
								if (pruga.getOznaka().equalsIgnoreCase(oznakaPruge)) {
									etapa.setPruga(pruga);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	public List<Pruga> getPruge() {
		return pruge;
	}

	public List<Kompozicija> getKompozicije() {
		return kompozicije;
	}

	public PrijevoznoSredstvo getVoziloPoOznaci(String oznaka) {
		for (PrijevoznoSredstvo vozilo : prijevoznaSredstva) {
			if (vozilo.getOznaka().equalsIgnoreCase(oznaka)) {
				return vozilo;
			}
		}
		return null;
	}

	public Kompozicija getKompozicijaPoOznaci(String oznakaKompozicije) {
		for (Kompozicija kompozicija : kompozicije) {
			if (kompozicija.getOznaka().equalsIgnoreCase(oznakaKompozicije)) {
				return kompozicija;
			}
		}
		return null;
	}

	public List<ZeljeznickaStanica> getStaniceZaPrugu(String oznakaPruge) {
		Pruga trazenaPruga = pruge.stream().filter(pruga -> pruga.getOznaka().equalsIgnoreCase(oznakaPruge)).findFirst()
				.orElse(null);

		if (trazenaPruga == null) {
			System.out.println("Nije pronađena pruga s oznakom: " + oznakaPruge);
			return Collections.emptyList();
		}
		return trazenaPruga.getStanice();
	}

	public boolean jeLiNormalniRedoslijed(String polaznaStanica, String odredisnaStanica) {
		for (Pruga pruga : pruge) {
			List<ZeljeznickaStanica> stanice = pruga.getStanice();
			int polaznaIndex = -1, odredisnaIndex = -1;
			for (int i = 0; i < stanice.size(); i++) {
				String nazivStanice = stanice.get(i).getNaziv().trim();
				if (nazivStanice.equalsIgnoreCase(polaznaStanica.trim()))
					polaznaIndex = i;
				if (nazivStanice.equalsIgnoreCase(odredisnaStanica.trim()))
					odredisnaIndex = i;
			}

			if (polaznaIndex != -1 && odredisnaIndex != -1) {
				return polaznaIndex < odredisnaIndex;
			}
		}
		return true;
	}

	public List<ZeljeznickaStanica> getStaniceIzmeduStanica(String polaznaStanica, String odredisnaStanica) {
		Map<String, Map<String, Integer>> graf = kreirajGrafStanica();

		if (!graf.containsKey(polaznaStanica) || !graf.containsKey(odredisnaStanica)) {
			System.out.println("Jedna ili obje stanice nisu pronađene u grafu.");
			return Collections.emptyList();
		}

		Map<String, Integer> udaljenosti = new HashMap<>();
		Map<String, String> prethodnici = new HashMap<>();
		PriorityQueue<String> red = new PriorityQueue<>(Comparator.comparingInt(udaljenosti::get));

		for (String stanica : graf.keySet()) {
			udaljenosti.put(stanica, Integer.MAX_VALUE);
			prethodnici.put(stanica, null);
		}
		udaljenosti.put(polaznaStanica, 0);
		red.add(polaznaStanica);

		while (!red.isEmpty()) {
			String trenutnaStanica = red.poll();

			for (Map.Entry<String, Integer> susjed : graf.get(trenutnaStanica).entrySet()) {
				String susjedStanica = susjed.getKey();
				int tezina = susjed.getValue();

				int novaUdaljenost = udaljenosti.get(trenutnaStanica) + tezina;
				if (novaUdaljenost < udaljenosti.get(susjedStanica)) {
					udaljenosti.put(susjedStanica, novaUdaljenost);
					prethodnici.put(susjedStanica, trenutnaStanica);
					red.add(susjedStanica);
				}
			}
		}

		List<ZeljeznickaStanica> putanja = new ArrayList<>();
		String trenutnaStanica = odredisnaStanica;

		while (trenutnaStanica != null) {
			ZeljeznickaStanica stanicaObjekt = pronadiStanicuPoNazivu(trenutnaStanica);
			if (stanicaObjekt != null) {
				putanja.add(0, stanicaObjekt);
			}
			trenutnaStanica = prethodnici.get(trenutnaStanica);
		}

		if (!putanja.isEmpty() && putanja.get(0).getNaziv().equals(polaznaStanica)) {
			return putanja;
		} else {
			System.out.println("Nema putanje između stanica.");
			return Collections.emptyList();
		}
	}

	private Map<String, Map<String, Integer>> kreirajGrafStanica() {
		Map<String, Map<String, Integer>> graf = new HashMap<>();

		for (Pruga pruga : pruge) {
			List<ZeljeznickaStanica> stanice = pruga.getStanice();
			for (int i = 0; i < stanice.size(); i++) {
				ZeljeznickaStanica trenutna = stanice.get(i);
				graf.putIfAbsent(trenutna.getNaziv(), new HashMap<>());

				if (i > 0) {
					ZeljeznickaStanica prethodna = stanice.get(i - 1);
					graf.get(trenutna.getNaziv()).put(prethodna.getNaziv(), trenutna.getDuzina());
					graf.get(prethodna.getNaziv()).put(trenutna.getNaziv(), trenutna.getDuzina());
				}
			}
		}
		return graf;
	}

	private ZeljeznickaStanica pronadiStanicuPoNazivu(String nazivStanice) {
		for (Pruga pruga : pruge) {
			for (ZeljeznickaStanica stanica : pruga.getStanice()) {
				if (stanica.getNaziv().equalsIgnoreCase(nazivStanice)) {
					return stanica;
				}
			}
		}
		return null;
	}

	public List<Vlak> getVlakovi() {
		if (vozniRed instanceof VozniRed) {
			VozniRed vozniRedObjekt = (VozniRed) vozniRed;
			List<Vlak> vlakovi = new ArrayList<>();
			for (KomponentaVoznogReda komponenta : vozniRedObjekt.getVlakovi()) {
				if (komponenta instanceof Vlak) {
					vlakovi.add((Vlak) komponenta);
				}
			}
			return vlakovi;
		}
		return Collections.emptyList();
	}

	public List<Etapa> getEtapeZaVlak(String oznakaVlaka) {
		List<Etapa> etapeZaVlak = new ArrayList<>();
		if (vozniRed instanceof VozniRed) {
			VozniRed vozniRedObjekt = (VozniRed) vozniRed;
			for (KomponentaVoznogReda komponenta : vozniRedObjekt.getVlakovi()) {
				if (komponenta instanceof Vlak && ((Vlak) komponenta).getOznaka().equals(oznakaVlaka)) {
					Vlak vlak = (Vlak) komponenta;
					for (KomponentaVoznogReda etapa : vlak.getEtape()) {
						if (etapa instanceof Etapa) {
							etapeZaVlak.add((Etapa) etapa);
						}
					}
				}
			}
		}
		return etapeZaVlak;
	}

	public List<ZeljeznickaStanica> getStaniceZaVlak(String oznakaVlaka) {
		if (vozniRed instanceof VozniRed) {
			VozniRed vozniRedObjekt = (VozniRed) vozniRed;
			for (KomponentaVoznogReda komponenta : vozniRedObjekt.getVlakovi()) {
				if (komponenta instanceof Vlak && ((Vlak) komponenta).getOznaka().equals(oznakaVlaka)) {
					Vlak vlak = (Vlak) komponenta;
					List<ZeljeznickaStanica> staniceZaVlak = vlak.getStanice();
					return staniceZaVlak;

				}
			}
		}
		return null;
	}

	public List<Vlak> getVlakoveZaOdabraneDane(String daniZaPretragu) {
		List<Vlak> vlakoviZaDane = new ArrayList<>();

		if (vozniRed instanceof VozniRed) {
			VozniRed vozniRedObjekt = (VozniRed) vozniRed;

			for (KomponentaVoznogReda komponenta : vozniRedObjekt.getVlakovi()) {
				if (komponenta instanceof Vlak) {
					Vlak vlak = (Vlak) komponenta;
					boolean voziOdabranimDanima = false;

					for (KomponentaVoznogReda etapa : vlak.getEtape()) {
						if (etapa instanceof Etapa) {
							Etapa trenutnaEtapa = (Etapa) etapa;
							String daniEtape = trenutnaEtapa.getDaniUTjednu();

							if (sadrziSveOdabraneDane(daniEtape, daniZaPretragu)) {
								voziOdabranimDanima = true;
								break;
							}
						}
					}

					if (voziOdabranimDanima) {
						vlakoviZaDane.add(vlak);
					}
				}
			}
		}

		return vlakoviZaDane;
	}

	private boolean sadrziSveOdabraneDane(String daniEtape, String daniZaPretragu) {
		for (char dan : daniZaPretragu.toCharArray()) {
			if (!daniEtape.contains(String.valueOf(dan))) {
				return false;
			}
		}
		return true;
	}

	public List<ZeljeznickaStanica> getStaniceZaEtapu(Etapa etapa) {
		if (etapa == null || etapa.getStanice() == null || etapa.getStanice().isEmpty()) {
			System.err.println("Nema dostupnih stanica za ovu etapu.");
			return Collections.emptyList();
		}
		return etapa.getStanice();
	}

	public void dodajKorisnika(String imeIPrezime) {
		boolean korisnikPostoji = korisnici.stream().anyMatch(k -> ((Korisnik) k).getImeIPrezime().equals(imeIPrezime));
		if (korisnikPostoji) {
			System.out.println("Korisnik s imenom i prezimenom '" + imeIPrezime + "' već postoji.");
		} else {
			Observer noviKorisnik = new Korisnik(imeIPrezime);
			korisnici.add(noviKorisnik);
			System.out.println("Korisnik uspješno dodan: " + imeIPrezime);
		}
	}

	public List<Observer> getKorisnike() {
		return korisnici;
	}

	public void dodajKorisnikaZaPracenje(String imeIPrezime, String oznakaVlaka, String nazivStanice) {
		Korisnik korisnik = null;
		for (Observer observer : korisnici) {
			if (observer instanceof Korisnik && ((Korisnik) observer).getImeIPrezime().equals(imeIPrezime)) {
				korisnik = (Korisnik) observer;
				break;
			}
		}

		if (korisnik == null) {
			System.out.println("Korisnik nije pronađen, dodajte korisnika prije praćenja.");
			return;
		}

		Vlak vlakZaPracenje = null;
		for (Vlak vlak : getVlakovi()) {
			if (vlak.getOznaka().equals(oznakaVlaka)) {
				vlakZaPracenje = vlak;
				break;
			}
		}

		if (vlakZaPracenje == null) {
			System.out.println("Vlak s oznakom " + oznakaVlaka + " nije pronađen.");
			return;
		}

		if (nazivStanice == null) {
			vlakZaPracenje.dodajPretplatnika(korisnik);
			System.out.println("Korisnik " + imeIPrezime + " je pretplaćen na vlak " + oznakaVlaka + ".");
		} else {
			ZeljeznickaStanica stanicaZaPracenje = null;
			for (ZeljeznickaStanica stanica : vlakZaPracenje.getStanice()) {
				if (stanica.getNaziv().equalsIgnoreCase(nazivStanice.trim())) {
					stanicaZaPracenje = stanica;
					break;
				}
			}

			if (stanicaZaPracenje == null) {
				System.out
						.println("Stanica s nazivom " + nazivStanice + " nije pronađena za vlak " + oznakaVlaka + ".");
				return;
			}

			stanicaZaPracenje.dodajPretplatnika(korisnik);
			System.out.println("Korisnik " + imeIPrezime + " je pretplaćen na stanicu " + nazivStanice
					+ " unutar vlaka " + oznakaVlaka + ".");
		}
	}

	public void pokreniSimulaciju(String oznakaVlaka, String dan, int koeficijent) {
		Vlak vlakZaSimulaciju = null;

		for (Vlak vlak : getVlakovi()) {
			if (vlak.getOznaka().equals(oznakaVlaka)) {
				vlakZaSimulaciju = vlak;
				break;
			}
		}

		if (vlakZaSimulaciju == null) {
			System.out.println("Vlak s oznakom " + oznakaVlaka + " nije pronađen.");
			return;
		}

		vlakZaSimulaciju.pokreniSimulaciju(dan, koeficijent);
	}

	public void postaviCijenik(double cijenaNormalni, double cijenaUbrzani, double cijenaBrzi, double popustSuN,
			double popustWebMob, double uvecanjeVlak) {
		cijenikKarte.setCijenaNormalni(cijenaNormalni);
		cijenikKarte.setCijenaUbrzani(cijenaUbrzani);
		cijenikKarte.setCijenaBrzi(cijenaBrzi);
		cijenikKarte.setPopustSuN(popustSuN);
		cijenikKarte.setPopustWebMob(popustWebMob);
		cijenikKarte.setUvecanjeVlak(uvecanjeVlak);

		cijenikKarte.ispisiCjenik();
	}

	public Vlak getVlakPoOznaci(String oznakaVlaka) {
		return getVlakovi().stream().filter(v -> v.getOznaka().equalsIgnoreCase(oznakaVlaka)).findFirst().orElse(null);
	}

	public void kreirajKartu(String oznakaVlaka, String polaznaStanica, String odredisnaStanica, String datumPutovanja,
			String nacinKupovine) {
		if (cijenikKarte.getCijenaNormalni() == 0 && cijenikKarte.getCijenaUbrzani() == 0
				&& cijenikKarte.getCijenaBrzi() == 0) {
			System.err.println("Greška: Cijenik nije definiran.");
			return;
		}

		Vlak vlak = getVlakPoOznaci(oznakaVlaka);
		if (vlak == null) {
			System.err.printf("Greška: Vlak s oznakom %s ne postoji.\n", oznakaVlaka);
			return;
		}

		List<ZeljeznickaStanica> stanice = vlak.getStanice();
		ZeljeznickaStanica pocetna = stanice.stream().filter(st -> st.getNaziv().equalsIgnoreCase(polaznaStanica))
				.findFirst().orElse(null);
		ZeljeznickaStanica odredisna = stanice.stream().filter(st -> st.getNaziv().equalsIgnoreCase(odredisnaStanica))
				.findFirst().orElse(null);

		if (pocetna == null || odredisna == null) {
			System.err.printf("Greška: Stanice %s ili %s ne postoje na ruti vlaka %s.\n", polaznaStanica,
					odredisnaStanica, oznakaVlaka);
			return;
		}

		int indefOfPocetna = stanice.indexOf(pocetna);
		int indefOfOdredisna = stanice.indexOf(odredisna);

		if (indefOfPocetna > indefOfOdredisna) {
			System.err.printf("Greška: Vlak %s ide u smjeru %s-%s.\n", oznakaVlaka, odredisnaStanica, polaznaStanica);
			return;
		}
		
		if (!vlak.jeLiDozvoljenaVoznja(pocetna, odredisna)) {
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		LocalDate datum;
		try {
			datum = LocalDate.parse(datumPutovanja, formatter);
		} catch (DateTimeParseException e) {
			System.err.printf("Greška: Neispravan format datuma %s. Očekivan format: dd.MM.yyyy.\n", datumPutovanja);
			return;
		}

		PlacanjeStartegy strategijaPlacanja;
		switch (nacinKupovine.toUpperCase()) {
		case "B":
			strategijaPlacanja = new PlacanjeBlagajnaStrategy();
			break;
		case "V":
			strategijaPlacanja = new VlakPlacanjeStrategy();
			break;
		case "WM":
			strategijaPlacanja = new WebMobPlacanjeStrategy();
			break;
		default:
			System.err.printf("Greška: Neispravan način kupovine %s. Očekivane vrijednosti: B, V, WM.\n",
					nacinKupovine);
			return;
		}

		upraviteljKartama.kreirajKartu(cijenikKarte, vlak, pocetna, odredisna, datum, strategijaPlacanja);
	}

	public void ispisiSveKarte() {
		upraviteljKartama.ispisiSveKarte();
	}

	public void ispisiPoslijednihNKarata(int n) {
		upraviteljKartama.ispisiPosljednjihN(n);
	}

	public void usporediKarteZaPutovanje(String polaznaStanica, String odredisnaStanica, LocalDate datum,
			String odVrijeme, String doVrijeme, String nacinKupovine) {
		upraviteljKartama.usporediKarteZaPutovanje(polaznaStanica, odredisnaStanica, datum, odVrijeme, doVrijeme,
				nacinKupovine);
	}

	public void dodajStatusRelacijiPruge(String oznakaPruge, String polaznaStanica, String odredisnaStanica,
			String status) {
		Pruga ciljnaPruga = pruge.stream().filter(pruga -> pruga.getOznaka().equalsIgnoreCase(oznakaPruge)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Pruga s oznakom %s nije pronađena.", oznakaPruge)));

		List<ZeljeznickaStanica> staniceNaPrugi = ciljnaPruga.getStanice();

		boolean postojiPolazna = staniceNaPrugi.stream()
				.anyMatch(stanica -> stanica.getNaziv().equalsIgnoreCase(polaznaStanica));
		boolean postojiOdredisna = staniceNaPrugi.stream()
				.anyMatch(stanica -> stanica.getNaziv().equalsIgnoreCase(odredisnaStanica));

		if (!postojiPolazna || !postojiOdredisna) {
			throw new IllegalArgumentException(
					String.format("Stanice %s i %s nisu obje prisutne na pruzi s oznakom %s.", polaznaStanica,
							odredisnaStanica, oznakaPruge));
		}

		ZeljeznickaStanica pocetna = staniceNaPrugi.stream()
				.filter(stanica -> stanica.getNaziv().equalsIgnoreCase(polaznaStanica)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Polazna stanica nije pronađena."));

		ZeljeznickaStanica odredisna = staniceNaPrugi.stream()
				.filter(stanica -> stanica.getNaziv().equalsIgnoreCase(odredisnaStanica)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Odredišna stanica nije pronađena."));

		ciljnaPruga.dodajRelaciju(pocetna, odredisna, status);
		azurirajPruge();
	}

	public void ispisSvihRelacijaUStanju(String stanje) {
		boolean pronađenaRelacija = false;

		for (Pruga pruga : pruge) {
			List<Relacija> relacije = pruga.getRelacije();

			if (relacije != null && !relacije.isEmpty()) {
				for (Relacija relacija : relacije) {
					if (relacija.getStatus().getStatus().equalsIgnoreCase(stanje)) {
						System.out.printf("Pruga: %s | Relacija: %s - %s | Status: %s\n", pruga.getOznaka(),
								relacija.getPocetnaStanica().getNaziv(), relacija.getZavrsnaStanica().getNaziv(),
								stanje);
						pronađenaRelacija = true;
					}
				}
			}
		}

		if (!pronađenaRelacija) {
			System.out.printf("Nema relacija sa statusom '%s' na svim prugama.\n", stanje);
		}
	}

	public void ispisSvihRelacijaUStanjuZaPrugu(String stanje, String oznakaPruge) {
		Pruga ciljnaPruga = pruge.stream().filter(pruga -> pruga.getOznaka().equalsIgnoreCase(oznakaPruge)).findFirst()
				.orElse(null);

		if (ciljnaPruga == null) {
			System.out.printf("Pruga s oznakom '%s' nije pronađena.\n", oznakaPruge);
			return;
		}

		List<Relacija> relacije = ciljnaPruga.getRelacije();
		boolean pronađenaRelacija = false;

		if (relacije != null && !relacije.isEmpty()) {
			for (Relacija relacija : relacije) {
				if (relacija.getStatus().getStatus().equalsIgnoreCase(stanje)) {
					System.out.printf("Pruga: %s | Relacija: %s - %s | Status: %s\n", ciljnaPruga.getOznaka(),
							relacija.getPocetnaStanica().getNaziv(), relacija.getZavrsnaStanica().getNaziv(), stanje);
					pronađenaRelacija = true;
				}
			}
		}

		if (!pronađenaRelacija) {
			System.out.printf("Nema relacija sa statusom '%s' na pruzi '%s'.\n", stanje, oznakaPruge);
		}
	}

}
