package imilanovi20_zadaca_3.upravljanje_kartama;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import imilanovi20_zadaca_3.entiteti.CijenikKarte;
import imilanovi20_zadaca_3.entiteti.Karta;
import imilanovi20_zadaca_3.entiteti.ZeljeznickaStanica;
import imilanovi20_zadaca_3.entiteti.komponentevoznogreda.Vlak;
import imilanovi20_zadaca_3.strategijePlacanja.PlacanjeStartegy;

public class UpraviteljKartama {
	private final LinkedList<KartaMemento> karteMemento = new LinkedList<>();

	public void kreirajKartu(CijenikKarte cijenik, Vlak vlak, ZeljeznickaStanica polaznaStanica,
			ZeljeznickaStanica odresdisnaStanica, LocalDate datum, PlacanjeStartegy strategijaPlacanja) {
		Karta karta = new Karta(cijenik, vlak, polaznaStanica, odresdisnaStanica, datum, strategijaPlacanja);
		karta.ispisiKartu();
		KartaMemento kartaMemento = karta.spremiStanje();
		karteMemento.addFirst(kartaMemento);

	}

	public void ispisiPosljednjihN(int n) {
		if (n > karteMemento.size()) {
			System.out.printf("Prevelik broj traženih karata:\n");
			System.out.printf("Broj karata u sustavu: " + karteMemento.size() + "\n");
			System.out.printf("Broj traženih karata: " + n + "\n");
			return;
		}
		System.out.printf("Ispis posljednjih %d karata:\n", n);
		int brojKarata = Math.min(n, karteMemento.size());
		System.out.println("-------------------------------------------------------------------------------");
		System.out.printf("%-12s | %-25s | %-12s | %-5s | %-5s | %-8s | %-8s | %-8s | %-25s\n", "Oznaka vlaka",
				"Relacija", "Datum", "Polazak", "Dolazak", "Cijena", "Popust", "Konačna cijena", "Način plaćanja");
		System.out.println("-------------------------------------------------------------------------------");

		for (int i = 0; i < brojKarata; i++) {
			KartaMemento kartaMemento = karteMemento.get(i);
			kartaMemento.ispisiMemento();
		}
	}

	public void ispisiSveKarte() {
		if (karteMemento.size() == 0) {
			System.out.printf("U sustavu nema karata\n");
			return;
		}
		System.out.println("Ispis svih karata:");

		System.out.println("-------------------------------------------------------------------------------");
		System.out.printf("%-12s | %-25s | %-12s | %-5s | %-5s | %-5s | %-5s | %-5s | %-25s\n", "Oznaka vlaka",
				"Relacija", "Datum", "Polazak", "Dolazak", "Cijena", "Popust", "Konačna cijena", "Način plaćanja");
		System.out.println("-------------------------------------------------------------------------------");

		for (int i = 0; i < karteMemento.size(); i++) {
			KartaMemento kartaMemento = karteMemento.get(i);
			kartaMemento.ispisiMemento();
		}
	}

	public void usporediKarteZaPutovanje(String polaznaStanica, String odredisnaStanica, LocalDate datum,
			String odVrijeme, String doVrijeme, String nacinKupovine) {
		System.out.printf("Usporedba karata za relaciju %s - %s, datum: %s, vrijeme: %s - %s, način kupovine: %s\n",
				polaznaStanica, odredisnaStanica, datum, odVrijeme, doVrijeme, nacinKupovine);

		String mapiraniNacinKupovine;
		switch (nacinKupovine.toUpperCase()) {
		case "WM":
			mapiraniNacinKupovine = "Plaćanje putem web/mobilne aplikacije";
			break;
		case "V":
			mapiraniNacinKupovine = "Plaćanje u vlaku";
			break;
		case "B":
			mapiraniNacinKupovine = "Plaćanje na blagajni";
			break;
		default:
			System.out.println("Nepoznat način kupovine. Prihvaćene vrijednosti su: WM, V, B.");
			return;
		}

		int odVrijemeMin = pretvoriVrijemeUMinute(odVrijeme);
		int doVrijemeMin = pretvoriVrijemeUMinute(doVrijeme);

		List<KartaMemento> odgovarajuceKarte = new LinkedList<>();
		for (KartaMemento karta : karteMemento) {
			if (karta.getPocetnaStanica().equalsIgnoreCase(polaznaStanica)
					&& karta.getOdredisnaStanica().equalsIgnoreCase(odredisnaStanica) && karta.getDatum().equals(datum)
					&& karta.getNacinPlacanja().equalsIgnoreCase(mapiraniNacinKupovine)) {

				int vrijemePolaska = pretvoriVrijemeUMinute(karta.getVrijemePolaska());
				int vrijemeDolaska = pretvoriVrijemeUMinute(karta.getVrijemeDolaska());

				if (vrijemePolaska >= odVrijemeMin && vrijemeDolaska <= doVrijemeMin) {
					odgovarajuceKarte.add(karta);
				}
			}
		}

		if (odgovarajuceKarte.isEmpty()) {
			System.out.println("Nema dostupnih karata za zadane kriterije.");
		} else {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.printf("%-12s | %-25s | %-12s | %-5s | %-5s | %-5s | %-5s | %-5s | %-25s\n", "Oznaka vlaka",
					"Relacija", "Datum", "Polazak", "Dolazak", "Cijena", "Popust", "Konačna cijena", "Način plaćanja");
			System.out.println("-------------------------------------------------------------------------------");
			;

			for (KartaMemento karta : odgovarajuceKarte) {
				karta.ispisiMemento();
			}
		}
	}

	private int pretvoriVrijemeUMinute(String vrijeme) {
		String[] dijelovi = vrijeme.split(":");
		int sati = Integer.parseInt(dijelovi[0]);
		int minute = Integer.parseInt(dijelovi[1]);
		return sati * 60 + minute;
	}

}
