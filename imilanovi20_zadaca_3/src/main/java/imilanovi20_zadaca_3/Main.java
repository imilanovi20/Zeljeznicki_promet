package imilanovi20_zadaca_3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import imilanovi20_zadaca_3.entiteti.Kompozicija;
import imilanovi20_zadaca_3.entiteti.PrijevoznoSredstvo;
import imilanovi20_zadaca_3.entiteti.Pruga;
import imilanovi20_zadaca_3.entiteti.ZeljeznickaStanica;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Etapa;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Vlak;
import imilanovi20_zadaca_3.entiteti.korisnik_simulacije.Korisnik;
import imilanovi20_zadaca_3.entiteti.korisnik_simulacije.Observer;

public class Main {

	private static ZeljeznickaTvrtka tvrtka = ZeljeznickaTvrtka.getInstance();

	public static void main(String[] args) throws IOException {

		Map<String, String> opcijeDatoteke = new HashMap<>();
		Scanner scanner = new Scanner(System.in);

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "--zs":
				if (i + 1 < args.length) {
					opcijeDatoteke.put("zs", args[++i]);
				} else {
					System.err.println("Greška: očekuje se naziv datoteke za opciju --zs");
				}
				break;
			case "--zps":
				if (i + 1 < args.length) {
					opcijeDatoteke.put("zps", args[++i]);
				} else {
					System.err.println("Greška: očekuje se naziv datoteke za opciju --zps");
				}
				break;
			case "--zk":
				if (i + 1 < args.length) {
					opcijeDatoteke.put("zk", args[++i]);
				} else {
					System.err.println("Greška: očekuje se naziv datoteke za opciju --zk");
				}
				break;
			case "--zvr":
				if (i + 1 < args.length) {
					opcijeDatoteke.put("zvr", args[++i]);
				} else {
					System.err.println("Greška: očekuje se naziv datoteke za opciju --zvr");
				}
				break;
			case "--zod":
				if (i + 1 < args.length) {
					opcijeDatoteke.put("zod", args[++i]);
				} else {
					System.err.println("Greška: očekuje se naziv datoteke za opciju --zod");
				}
				break;
			default:
				System.err.println("Nepoznata opcija: " + args[i]);
			}
		}

		if (!opcijeDatoteke.containsKey("zs") || !opcijeDatoteke.containsKey("zps") || !opcijeDatoteke.containsKey("zk")
				|| !opcijeDatoteke.containsKey("zvr") || !opcijeDatoteke.containsKey("zod")) {
			System.err.println("Greška: Sve opcije (--zs, --zps, --zk, --zvr, --zod) moraju biti zadane.");
			System.exit(1);
		}

		try {
			provjeriDatoteke(opcijeDatoteke);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		Path putanjaStanicePruge = Paths.get(opcijeDatoteke.get("zs"));
		Path putanjaPrijevoznaSredstva = Paths.get(opcijeDatoteke.get("zps"));
		Path putanjaKompozicije = Paths.get(opcijeDatoteke.get("zk"));
		Path putanjaVozniRed = Paths.get(opcijeDatoteke.get("zvr"));
		Path putanjaOznakeDana = Paths.get(opcijeDatoteke.get("zod"));

		tvrtka.inicijalizirajTvrtku(putanjaPrijevoznaSredstva, putanjaStanicePruge, putanjaKompozicije, putanjaVozniRed,
				putanjaOznakeDana);

		while (true) {
			System.out.print("Unesite komandu: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(" ");
			String command = parts[0].toUpperCase();

			switch (command) {
			case "IP":
				ispisiPruge();
				break;
			case "ISP":
				if (parts.length < 3) {
					System.out.println("Pogrešna sintaksa. Primjer: ISP <oznaka_pruge> <smjer>");
					break;
				}
				String oznakaPruge = parts[1];
				String redoslijed = parts[2].toUpperCase();
				ispisiStaniceZaPrugu(tvrtka, oznakaPruge, redoslijed);
				break;
			case "ISI2S":
				if (!input.contains("-")) {
					System.out.println("Pogrešna sintaksa. Primjer: ISI2S <pocetna_stanica> - <odredisna_stanica>");
					break;
				}
				String[] dijelovi = input.substring(6).split(" - ");
				if (dijelovi.length != 2) {
					System.out.println("Pogrešna sintaksa. Primjer: ISI2S <pocetna_stanica> - <odredisna_stanica>");
					break;
				}
				String polaznaStanica = dijelovi[0].trim();
				String odredisnaStanica = dijelovi[1].trim();
				ispisiStaniceIzmeduDvijuStanica(tvrtka, polaznaStanica, odredisnaStanica);
				break;
			case "IK":
				if (parts.length < 2) {
					System.out.println("Pogrešna sintaksa. Primjer: IK <oznaka_kompozicije>");
				} else {
					String oznakaKompozicije = parts[1];
					ispisiKompoziciju(tvrtka, oznakaKompozicije);
				}
				break;
			case "IV":
				ispisiVlakove();
				break;
			case "IEV":
				if (parts.length < 2) {
					System.out.println("Pogrešna sintaksa. Primjer: IEV <oznaka_vlaka>");
				} else {
					String oznakaVlaka = input.substring("IEV".length()).trim();
					ispisiEtapeVlaka(oznakaVlaka);
				}
				break;

			case "IEVD":
				if (parts.length < 2) {
					System.out.println("Pogrešna sintaksa. Primjer: IEVD <dani>");
				} else {
					ispisiVlakoveZaOdabraneDane(parts[1]);
				}
				break;

			case "IVRV":
				if (parts.length < 2) {
					System.out.println("Pogrešna sintaksa. Primjer: IVRV <oznaka_vlaka>");
				} else {
					String oznakaVlaka = input.substring(5).trim();
					ispisiVozniRedVlaka(oznakaVlaka);
				}
				break;
			case "DK":
				if (parts.length < 3) {
					System.out.println("Pogrešna sintaksa. Primjer: DK <ime> <prezime>");
				} else {
					String imeIPrezime = parts[1] + " " + parts[2];
					tvrtka.dodajKorisnika(imeIPrezime);
				}
				break;
			case "PK":
				pregledKorisnika();
				break;
			case "DPK":
				if (parts.length < 4) {
					System.out.println(
							"Pogrešna sintaksa. Primjer: DPK <ime> <prezime> - <oznaka_vlaka> [- <oznaka_stanice>]");
					break;
				}

				String imePrezime = parts[1] + " " + parts[2];

				int vlakStart = input.indexOf("- ") + 2;
				if (vlakStart == 1) {
					System.out.println("Pogrešna sintaksa. Nedostaje oznaka vlaka.");
					break;
				}

				int stanicaStart = input.indexOf("- ", vlakStart);
				String vlakID;
				String stanica = null;

				if (stanicaStart != -1) {
					vlakID = input.substring(vlakStart, stanicaStart).trim();
					stanica = input.substring(stanicaStart + 2).trim();
				} else {
					vlakID = input.substring(vlakStart).trim();
				}

				tvrtka.dodajKorisnikaZaPracenje(imePrezime, vlakID, stanica);
				break;

			case "SVV":
				if (parts.length < 4) {
					System.out.println("Pogrešna sintaksa. Primjer: SVV <oznaka_vlaka> - <dan> - <koeficjent>");
					break;
				}
				int vlakZaSimulacijuStart = input.indexOf(" ") + 1;
				int danStart = input.indexOf("- ", vlakZaSimulacijuStart);
				if (vlakZaSimulacijuStart == 0 || danStart == -1) {
					System.out.println("Pogrešna sintaksa. Primjer: SVV <oznaka_vlaka> - <dan> - <koeficjent>");
					break;
				}

				String vlakZaSimulaciju = input.substring(vlakZaSimulacijuStart, danStart).trim();
				int koeficijentStart = input.indexOf("- ", danStart + 2);
				if (koeficijentStart == -1) {
					System.out.println("Pogrešna sintaksa. Primjer: SVV <oznaka_vlaka> - <dan> - <koeficjent>");
					break;
				}

				String danSimulacije = input.substring(danStart + 2, koeficijentStart).trim();

				int koeficijentSimulacije;
				try {
					koeficijentSimulacije = Integer.parseInt(input.substring(koeficijentStart + 2).trim());
				} catch (NumberFormatException e) {
					System.out.println("Koeficijent mora biti broj. SVV <oznaka_vlaka> - <dan> - <koeficjent>");
					break;
				}

				tvrtka.pokreniSimulaciju(vlakZaSimulaciju, danSimulacije, koeficijentSimulacije);
				break;
			case "CVP":
				if (parts.length < 7) {
					System.out.println(
							"Pogrešna sintaksa. Primjer: CVP <cijenaNormalni> <cijenaUbrzani> <cijenaBrzi> <popustSuN> <popustWebMob> <uvecanjeVlak>");
					break;
				}

				try {
					double cijenaNormalni = Double.parseDouble(parts[1].replace(",", "."));
					double cijenaUbrzani = Double.parseDouble(parts[2].replace(",", "."));
					double cijenaBrzi = Double.parseDouble(parts[3].replace(",", "."));
					double popustSuN = Double.parseDouble(parts[4].replace(",", "."));
					double popustWebMob = Double.parseDouble(parts[5].replace(",", "."));
					double uvecanjeVlak = Double.parseDouble(parts[6].replace(",", "."));

					tvrtka.postaviCijenik(cijenaNormalni, cijenaUbrzani, cijenaBrzi, popustSuN, popustWebMob,
							uvecanjeVlak);
				} catch (NumberFormatException e) {
					System.out.println(
							"Greška u unosu. Provjerite da ste ispravno unijeli brojeve. Primjer: CVP <cijenaNormalni> <cijenaUbrzani> <cijenaBrzi> <popustSuN> <popustWebMob> <uvecanjeVlak>");
				}
				break;

			case "KKPV2S":
				String[] dijeloviKKPV2S = input.substring(7).split(" - ");
				if(dijeloviKKPV2S.length < 5) {
					System.out.println(
							"Greška u unosu. KKPV2S <oznakaVlaka> - <polaznaStaniva> - <odredisnaStanica> - <datumPutovanja> - <nacinKupovine>");
					break;
				}
				try {
					String oznakaVlaka = dijeloviKKPV2S[0].trim();
					String polStanica = dijeloviKKPV2S[1].trim();
					String odrStanica = dijeloviKKPV2S[2].trim();
					String datumPutovanja = dijeloviKKPV2S[3].trim();
					String nacinKupovine = dijeloviKKPV2S[4].trim();

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
					LocalDate datum;
					try {
						datum = LocalDate.parse(datumPutovanja, formatter);
					} catch (Exception e) {
						System.err.printf("Greška: Neispravan format datuma %s. Očekivan format: dd.MM.yyyy.\n",
								datumPutovanja);
						break;
					}

					tvrtka.kreirajKartu(oznakaVlaka, polStanica, odrStanica, datum.format(formatter), nacinKupovine);
				} catch (NumberFormatException e) {
					System.out.println(
							"Greška u unosu. KKPV2S <oznakaVlaka> - <polaznaStaniva> - <odredisnaStanica> - <datumPutovanja> - <nacinKupovine>");
				}
				break;
			case "IKKPV":
				if (parts.length == 1) {
					tvrtka.ispisiSveKarte();
				} else {
					try {
						int n = Integer.parseInt(parts[1]);
						if (n > 0) {
							tvrtka.ispisiPoslijednihNKarata(n);
						} else {
							System.out.println("Greška: Broj mora biti veći od 0. Primjer: IKKPV 3");
						}
					} catch (NumberFormatException e) {
						System.out.println("Greška: Argument mora biti broj. Primjer: IKKPV 3");
					}
				}
				break;
			case "UKP2S":
				try {
					String[] dijeloviKarte = input.substring(6).split(" - ");
					if (dijeloviKarte.length != 6) {
						System.out.println(
								"Pogrešna sintaksa. Primjer: UKP2S polaznaStanica - odredišnaStanica - datum - odVrijeme - doVrijeme - načinKupovine");
						break;
					}

					String polStanica = dijeloviKarte[0].trim();
					String odraStanica = dijeloviKarte[1].trim();
					LocalDate datum = LocalDate.parse(dijeloviKarte[2].trim(),
							DateTimeFormatter.ofPattern("dd.MM.yyyy."));
					String odVrijeme = dijeloviKarte[3].trim();
					String doVrijeme = dijeloviKarte[4].trim();
					String nacinKupovine = dijeloviKarte[5].trim();

					tvrtka.usporediKarteZaPutovanje(polStanica, odraStanica, datum, odVrijeme, doVrijeme,
							nacinKupovine);
				} catch (Exception e) {
					System.out.println(
							"Greška prilikom obrade unosa. Provjerite sintaksu. Primjer: UKP2S polaznaStanica - odredišnaStanica - datum - odVrijeme - doVrijeme - načinKupovine");
				}
				break;
			case "PSP2S":

				String[] dijeloviPSP2S = input.substring(5).split(" - ");
				if (dijeloviPSP2S.length < 4) {
					System.out.println(
							"Pogrešna sintaksa. Primjer: PSP2S oznaka - polaznaStanica - odredišnaStanica - status");
					break;
				}

				String oznPruge = dijeloviPSP2S[0].trim();
				String polStanica = dijeloviPSP2S[1].trim();
				String odrStanica = dijeloviPSP2S[2].trim();
				String status = dijeloviPSP2S[3].trim();

				tvrtka.dodajStatusRelacijiPruge(oznPruge, polStanica, odrStanica, status);

				break;
			case "IRPS":
				try {
					String[] dijeloviIRPS = input.substring(5).split(" ");
					if (dijeloviIRPS.length < 1 || dijeloviIRPS.length > 2) {
						System.out.println("Pogrešna sintaksa. Primjer: IRPS status [oznaka]");
						break;
					}

					String stanje = dijeloviIRPS[0].trim().toUpperCase();
					if (!stanje.matches("[IKTZ]")) {
						System.out.println("Greška: Neispravan status. Dozvoljeni statusi su: I, K, T, Z.");
						break;
					}

					if (dijeloviIRPS.length == 2) {
						String ozPruge = dijeloviIRPS[1].trim();
						tvrtka.ispisSvihRelacijaUStanjuZaPrugu(stanje, ozPruge);
					} else {
						tvrtka.ispisSvihRelacijaUStanju(stanje);
					}
				} catch (Exception e) {
					System.out.println(
							"Greška prilikom obrade unosa. Provjerite sintaksu. Primjer: IRPS status [oznaka]");
				}
				break;

			case "Q":
				System.out.println("Prekid rada programa.");
				scanner.close();
				System.exit(0);

			default:
				System.out.println("Nepoznata komanda. Pokušajte ponovno.");
				break;
			}
		}

	}

	private static void provjeriDatoteke(Map<String, String> opcijeDatoteke) {
		for (Map.Entry<String, String> opcija : opcijeDatoteke.entrySet()) {
			String nazivDatoteke = opcija.getValue();
			if (!Files.exists(Paths.get(nazivDatoteke))) {
				throw new IllegalArgumentException("Greška: Datoteka ne postoji: " + nazivDatoteke);
			}
			if (!Files.isReadable(Paths.get(nazivDatoteke))) {
				throw new IllegalArgumentException("Greška: Datoteka nije čitljiva: " + nazivDatoteke);
			}
		}
	}

	private static void ispisiKompoziciju(ZeljeznickaTvrtka tvrtka, String oznakaKompozicije) {
		Kompozicija kompozicija = tvrtka.getKompozicijaPoOznaci(oznakaKompozicije);

		if (kompozicija == null) {
			System.out.println("Kompozicija s oznakom " + oznakaKompozicije + " nije pronađena.");
			return;
		}

		System.out.printf("Detalji za kompoziciju %s:%n", oznakaKompozicije);
		System.out.printf("%-15s %-15s %-25s %-10s %-20s %-15s %-10s%n", "Oznaka vozila", "Uloga", "Opis", "Godina",
				"Namjena", "Vrsta pogona", "Brzina (km/h)");
		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------");

		for (int i = 0; i < kompozicija.getOznakeVozila().size(); i++) {
			String oznakaVozila = kompozicija.getOznakeVozila().get(i);
			String uloga = kompozicija.getUloge().get(i);

			PrijevoznoSredstvo vozilo = tvrtka.getVoziloPoOznaci(oznakaVozila);
			if (vozilo != null) {
				String opis = vozilo.getOpis();
				if (opis.length() > 20) {
					opis = opis.substring(0, 17) + "...";
				}
				System.out.printf("%-15s %-15s %-25s %-10d %-20s %-15s %-10d%n", vozilo.getOznaka(), uloga, opis,
						vozilo.getGodinaProizvodnje(), vozilo.getNamjena(), vozilo.getVrstaPogona(),
						vozilo.getMaksimalnaBrzina());
			} else {
				System.out.printf("%-15s %-15s %-25s%n", oznakaVozila, uloga, "Podaci o vozilu nisu dostupni");
			}
		}
	}

	private static void ispisiStaniceIzmeduDvijuStanica(ZeljeznickaTvrtka tvrtka, String polaznaStanica,
			String odredisnaStanica) {
		List<ZeljeznickaStanica> stanice = tvrtka.getStaniceIzmeduStanica(polaznaStanica, odredisnaStanica);
		if (stanice.isEmpty())
			return;

		boolean jeLiNormalanPoredak = stanice.get(0).getDuzina() == 0;

		System.out.printf("%-20s %-10s %-10s%n", "Naziv stanice", "Vrsta", "Kilometraža");
		int ukupnoKm = 0;
		System.out.println("-----------------------------------------------------------------");

		for (int i = 0; i < stanice.size(); i++) {
			ZeljeznickaStanica stanica = stanice.get(i);
			System.out.printf("%-20s %-10s %-10d%n", stanica.getNaziv(), stanica.getVrstaStanice(), ukupnoKm);
			if (i < stanice.size() - 1) {
				if (jeLiNormalanPoredak) {
					ZeljeznickaStanica slijedecaStanica = stanice.get(i + 1);
					ukupnoKm += slijedecaStanica.getDuzina();
				} else {
					ukupnoKm += stanica.getDuzina();
				}
			}
		}
	}

	private static void ispisiStaniceZaPrugu(ZeljeznickaTvrtka tvrtka, String oznakaPruge, String redoslijed) {
		List<ZeljeznickaStanica> stanice = tvrtka.getStaniceZaPrugu(oznakaPruge);
		if (stanice.isEmpty())
			return;

		System.out.printf("Prikaz stanica na pruzi %s (%s redoslijed):%n", oznakaPruge,
				redoslijed.equals("N") ? "normalni" : "obrnuti");

		System.out.printf("%-20s %-10s %-10s%n", "Naziv stanice", "Vrsta", "Kilometraža");
		System.out.println("-----------------------------------------------------------------");

		int akumuliranaKilometraža = 0;

		if (redoslijed.equals("N")) {
			for (int i = 0; i < stanice.size(); i++) {
				ZeljeznickaStanica stanica = stanice.get(i);
				System.out.printf("%-20s %-10s %-10d%n", stanica.getNaziv(), stanica.getVrstaStanice(),
						akumuliranaKilometraža);
				if (i < stanice.size() - 1) {
					akumuliranaKilometraža += stanice.get(i + 1).getDuzina();
				}
			}
		} else if (redoslijed.equals("O")) {
			for (int i = stanice.size() - 1; i >= 0; i--) {
				ZeljeznickaStanica stanica = stanice.get(i);
				System.out.printf("%-20s %-10s %-10d%n", stanica.getNaziv(), stanica.getVrstaStanice(),
						akumuliranaKilometraža);
				if (i > 0) {
					akumuliranaKilometraža += stanice.get(i).getDuzina();
				}
			}
		} else {
			System.out.println("Nepoznat redoslijed. Koristite 'N' za normalni ili 'O' za obrnuti.");
		}
	}

	private static void ispisiPruge() {
		List<Pruga> pruge = tvrtka.getPruge();

		if (pruge.isEmpty()) {
			System.out.println("Nema dostupnih pruga za prikaz.");
			return;
		}

		System.out.println("Prikaz pruga:");
		System.out.printf("%-12s %-25s %-25s %-15s%n", "Oznaka", "Početna stanica", "Završna stanica", "Dužina (km)");
		System.out.println("-----------------------------------------------------------------");
		for (Pruga pruga : pruge) {
			String pocetnaStanica = pruga.getStanice().isEmpty() ? "Nepoznata" : pruga.getStanice().get(0).getNaziv();
			String zavrsnaStanica = pruga.getStanice().size() < 2 ? "Nepoznata"
					: pruga.getStanice().get(pruga.getStanice().size() - 1).getNaziv();
			System.out.printf("%-12s %-25s %-25s %-15d%n", pruga.getOznaka(), pocetnaStanica, zavrsnaStanica,
					pruga.getDuzina());
		}
	}

	private static void ispisiVlakove() {
		List<Vlak> vlakovi = tvrtka.getVlakovi();

		System.out.printf("%-10s %-20s %-20s %-15s %-15s %-10s\n", "Vlak", "Polazna stanica", "Odredišna stanica",
				"Vrijeme polaska", "Vrijeme dolaska", "Ukupno km");
		System.out.println("-------------------------------------------------------------------------------");

		for (Vlak vlak : vlakovi) {
			String polaznaStanica = vlak.getPolaznaStanica().getNaziv();
			String odredisnaStanica = vlak.getOdredisnaStanica().getNaziv();
			if (polaznaStanica.length() > 17) {
				polaznaStanica = polaznaStanica.substring(0, 17) + "...";
			}

			if (odredisnaStanica.length() > 17) {
				odredisnaStanica = odredisnaStanica.substring(0, 17) + "...";
			}
			System.out.printf("%-10s %-20s %-20s %-15s %-15s %-10d\n", vlak.getOznaka(), polaznaStanica,
					odredisnaStanica, vlak.getVrijemePolaska(), vlak.getVrijemeDolaska(), vlak.getDuzina());
		}
	}

	private static void ispisiEtapeVlaka(String oznakaVlaka) {
		List<Etapa> etape = tvrtka.getEtapeZaVlak(oznakaVlaka);
		if (etape.isEmpty()) {
			System.out.println("Ne postoje etape za vlak" + oznakaVlaka);
			return;
		}

		System.out.printf("%-10s %-10s %-20s %-20s %-15s %-15s %-10s %-10s\n", "Vlak", "Pruga", "Polazna stanica",
				"Odredišna stanica", "Vrijeme polaska", "Vrijeme dolaska", "Ukupno km", "Dani");
		System.out.println("----------------------------------------------------------------------------------------");

		for (Etapa etapa : etape) {
			String polaznaStanica = etapa.getPolaznaStanica().getNaziv();
			String odredisnaStanica = etapa.getOdredisnaStanica().getNaziv();
			if (polaznaStanica.length() > 17) {
				polaznaStanica = polaznaStanica.substring(0, 17) + "...";
			}

			if (odredisnaStanica.length() > 17) {
				odredisnaStanica = odredisnaStanica.substring(0, 17) + "...";
			}
			System.out.printf("%-10s %-10s %-20s %-20s %-15s %-15s %-10d %-10s\n", oznakaVlaka, etapa.getOznakaPruge(),
					polaznaStanica, odredisnaStanica, etapa.getVrijemePolaska(), etapa.getVrijemeDolaska(),
					etapa.getDuzina(), etapa.getDaniUTjednu());
		}
	}

	private static void ispisiVlakoveZaOdabraneDane(String daniZaPretragu) {
		List<Vlak> vlakovi = tvrtka.getVlakoveZaOdabraneDane(daniZaPretragu);

		if (vlakovi.isEmpty()) {
			System.out.println("Nema vlakova koji voze na odabranim danima: " + daniZaPretragu);
			return;
		}

		System.out.printf("%-10s %-20s %-20s %-15s %-15s %-15s\n", "Vlak", "Polazna stanica", "Odredišna stanica",
				"Vrijeme polaska", "Vrijeme dolaska", "Dani");
		System.out.println("-------------------------------------------------------------------------------");

		for (Vlak vlak : vlakovi) {
			List<Etapa> etape = tvrtka.getEtapeZaVlak(vlak.getOznaka());
			Etapa etapa = etape.get(0);
			String polaznaStanica = vlak.getPolaznaStanica().getNaziv();
			String odredisnaStanica = vlak.getOdredisnaStanica().getNaziv();
			if (polaznaStanica.length() > 17) {
				polaznaStanica = polaznaStanica.substring(0, 17) + "...";
			}

			if (odredisnaStanica.length() > 17) {
				odredisnaStanica = odredisnaStanica.substring(0, 17) + "...";
			}
			System.out.printf("%-10s %-20s %-20s %-15s %-15s %-15s\n", vlak.getOznaka(), polaznaStanica,
					odredisnaStanica, vlak.getVrijemePolaska(), vlak.getVrijemeDolaska(), etapa.getDaniUTjednu());
		}
	}

	private static void ispisiVozniRedVlaka(String oznakaVlaka) {
		List<Etapa> etape = tvrtka.getEtapeZaVlak(oznakaVlaka);
		if (etape.isEmpty()) {
			System.out.println("Nema etapa za vlak s oznakom: " + oznakaVlaka);
			return;
		}

		List<ZeljeznickaStanica> stanice = tvrtka.getStaniceZaVlak(oznakaVlaka);
		if (stanice.isEmpty()) {
			System.out.println("Nema stanica za vlak s oznakom: " + oznakaVlaka);
			return;
		}

		System.out.printf("%-10s %-10s %-20s %-15s %-10s\n", "Vlak", "Pruga", "Stanica", "Vrijeme polaska", "Km");
		System.out.println("-----------------------------------------------------------------");

		int ukupnoKm = 0;

		for (int i = 0; i < stanice.size(); i++) {
			ZeljeznickaStanica trenutnaStanica = stanice.get(i);
			ZeljeznickaStanica slijedecaStanica = null;
			ZeljeznickaStanica prethodnaStanica = null;
			Etapa etapa = etape.get(0);
			boolean preskoci = false;

			for (int j = 0; j < etape.size(); j++) {
				if (etape.get(j).getStanice().contains(trenutnaStanica)) {
					etapa = etape.get(j);
					break;
				}
			}

			if (i + 1 < stanice.size()) {
				slijedecaStanica = stanice.get(i + 1);
			}
			if (i > 0) {
				prethodnaStanica = stanice.get(i - 1);

				String vrijemePrethodne = etapa.dohvatiPolazakSaStanice(prethodnaStanica);
				String vrijemeTrenutne = etapa.dohvatiPolazakSaStanice(trenutnaStanica);

				if (vrijemePrethodne != null && vrijemePrethodne.equals(vrijemeTrenutne))
					preskoci = true;
				else
					preskoci = false;
			}

			String stanicaNaziv = trenutnaStanica.getNaziv();
			if (stanicaNaziv.length() > 17) {
				stanicaNaziv = stanicaNaziv.substring(0, 17) + "...";
			}
			String vrijemeEtape = preskoci == true ? "-" : etapa.dohvatiPolazakSaStanice(trenutnaStanica);
			System.out.printf("%-10s %-10s %-20s %-15s %-10d\n", oznakaVlaka, etapa.getOznakaPruge(), stanicaNaziv,
					vrijemeEtape, ukupnoKm);

			if (i < stanice.size() - 1) {
				ukupnoKm += slijedecaStanica.getDuzina();
			}

		}
	}

	private static void pregledKorisnika() {
		List<Observer> korisnici = tvrtka.getKorisnike();
		if (korisnici.isEmpty()) {
			System.out.println("Nema korisnika u registru.");
		} else {
			System.out.println("Popis korisnika:");
			for (Observer komponenta : korisnici) {
				Korisnik korisnik = (Korisnik) komponenta;
				System.out.println("- " + korisnik.getImeIPrezime());
			}
		}
	}
}
