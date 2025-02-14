package imilanovi20_zadaca_3.entiteti;

import java.util.ArrayList;
import java.util.List;

import imilanovi20_zadaca_3.stanjaRelacija.Ispravna;
import imilanovi20_zadaca_3.stanjaRelacija.Kvar;
import imilanovi20_zadaca_3.stanjaRelacija.RelacijaStatus;
import imilanovi20_zadaca_3.stanjaRelacija.Testiranje;
import imilanovi20_zadaca_3.stanjaRelacija.Zatvorena;

public class Pruga {
	private String oznaka;
	private String kategorija;
	private String nacinPrijevoza;
	private int brojKolosijeka;
	private int duzina;
	private double dopustenoOpterecenjeOsovina;
	private double dopustenoOpterecenjeDuzniMetar;
	private String status;
	private List<ZeljeznickaStanica> stanice;
	private List<Relacija> relacije = new ArrayList<Relacija>();

	public void dodajRelaciju(ZeljeznickaStanica pocetna, ZeljeznickaStanica zavrsna, String statusRelacije) {
		RelacijaStatus status = dohvatiStatus(statusRelacije);
		int indexPocetna = stanice.indexOf(pocetna);
		int indexZavrsna = stanice.indexOf(zavrsna);

		if (indexPocetna == -1 || indexZavrsna == -1) {
			throw new IllegalArgumentException("Početna ili završna stanica nisu dio ove pruge.");
		}

		Relacija postojecaRelacija = relacije.stream()
				.filter(rel -> rel.getPocetnaStanica().equals(pocetna) && rel.getZavrsnaStanica().equals(zavrsna))
				.findFirst().orElse(null);

		List<ZeljeznickaStanica> segmentStanica;
		if (indexPocetna < indexZavrsna) {
			segmentStanica = stanice.subList(indexPocetna, indexZavrsna + 1);
		} else {
			segmentStanica = new ArrayList<>(stanice.subList(indexZavrsna, indexPocetna + 1));
			java.util.Collections.reverse(segmentStanica);
		}

		if (postojecaRelacija != null) {
			postojecaRelacija.promijeniStatus(status);
		} else {
			if (!provjeriPreklapanjaZaSveRelacije(segmentStanica)) {
				Relacija relacija = new Relacija(segmentStanica, oznaka);
				relacija.promijeniStatus(status);
				relacije.add(relacija);
				if (jeLiSegmentUObicnomSmjeru(relacija.getStanice()))
					izbrisiIspravnuObicnuRelaciju();
				else
					izbrisiIspravnuObrnutuRelaciju();
				izbrisiIspravnuObicnuRelaciju();
			}
		}
		if (brojKolosijeka == 1) {
			Relacija obrnutaRelacija = relacije.stream()
					.filter(rel -> rel.getPocetnaStanica().equals(zavrsna) && rel.getZavrsnaStanica().equals(pocetna))
					.findFirst().orElse(null);

			if (obrnutaRelacija != null) {
				obrnutaRelacija.promijeniStatus(status);
			} else {
				List<ZeljeznickaStanica> obrnutiSegment = new ArrayList<>(segmentStanica);
				java.util.Collections.reverse(obrnutiSegment);

				if (!provjeriPreklapanjaZaSveRelacije(obrnutiSegment)) {
					Relacija novaObrnutaRelacija = new Relacija(obrnutiSegment, oznaka);
					novaObrnutaRelacija.promijeniStatus(status);
					relacije.add(novaObrnutaRelacija);
					izbrisiIspravnuObrnutuRelaciju();
					if (jeLiSegmentUObicnomSmjeru(novaObrnutaRelacija.getStanice()))
						izbrisiIspravnuObicnuRelaciju();
					else
						izbrisiIspravnuObrnutuRelaciju();
					izbrisiIspravnuObicnuRelaciju();
				}
			}
		}
		kreirajIspravneZaSmjer();
	}

	private void kreirajIspravneZaSmjer() {
		if (provjeriJesuLiSveIspravneRelacijeUObicnomSmjeru()) {
			izbrisiDjelomicnoIspravneObicniSmjer();
			kreirajIspravnuObicnuRelaciju();
		}
		if (provjeriJesuLiSveIspravneRelacijeUObrnutomSmjeru()) {
			izbrisiDjelomicnoIspravneObrnutiSmjer();
			kreirajIspravnuObrnutuRelaciju();
		}

	}

	private boolean postojiLiIspravnaUObicnomSmjeru() {
		for (Relacija relacija : relacije) {
			if (relacija.getPocetnaStanica().getNaziv().equals(stanice.get(0).getNaziv())
					&& relacija.getZavrsnaStanica().getNaziv().equals(stanice.get(stanice.size() - 1).getNaziv())
					&& relacija.getStatus().getStatus().equals("I"))
				return true;
		}
		return false;
	}

	private boolean postojiLiIspravnaUObrnutomSmjeru() {
		for (Relacija relacija : relacije) {
			if (relacija.getPocetnaStanica().getNaziv().equals(stanice.get(stanice.size() - 1).getNaziv())
					&& relacija.getZavrsnaStanica().getNaziv().equals(stanice.get(0).getNaziv())
					&& relacija.getStatus().getStatus().equals("I"))
				return true;
		}
		return false;
	}

	public void izbrisiDjelomicnoIspravneObicniSmjer() {
		for (int i = 0; i < relacije.size(); i++) {
			Relacija relacija = relacije.get(i);
			int indexPocetnaUSegmneu = -1;
			int indexZavrsneUSegmentu = -1;

			ZeljeznickaStanica pocetna = relacija.getPocetnaStanica();
			ZeljeznickaStanica zavrsna = relacija.getZavrsnaStanica();

			for (ZeljeznickaStanica stanicaSegment : stanice) {
				if (stanicaSegment.getNaziv().equals(pocetna.getNaziv())) {
					indexPocetnaUSegmneu = stanice.indexOf(stanicaSegment);
					break;
				}
			}

			for (ZeljeznickaStanica stanicaSegment : stanice) {
				if (stanicaSegment.getNaziv().equals(zavrsna.getNaziv())) {
					indexZavrsneUSegmentu = stanice.indexOf(stanicaSegment);
					break;
				}
			}

			if (relacija.getStatus().getStatus().equals("I") && indexZavrsneUSegmentu > indexPocetnaUSegmneu
					&& (indexPocetnaUSegmneu != 0 || indexZavrsneUSegmentu != stanice.size() - 1)) {
				relacije.remove(i);
			}
		}
	}

	public void izbrisiDjelomicnoIspravneObrnutiSmjer() {
		for (int i = 0; i < relacije.size(); i++) {
			Relacija relacija = relacije.get(i);

			int indexPocetnaUSegmneu = -1;
			int indexZavrsneUSegmentu = -1;

			ZeljeznickaStanica pocetna = relacija.getPocetnaStanica();
			ZeljeznickaStanica zavrsna = relacija.getZavrsnaStanica();

			for (ZeljeznickaStanica stanicaSegment : stanice) {
				if (stanicaSegment.getNaziv().equals(pocetna.getNaziv())) {
					indexPocetnaUSegmneu = stanice.indexOf(stanicaSegment);
					break;
				}
			}

			for (ZeljeznickaStanica stanicaSegment : stanice) {
				if (stanicaSegment.getNaziv().equals(zavrsna.getNaziv())) {
					indexZavrsneUSegmentu = stanice.indexOf(stanicaSegment);
					break;
				}
			}

			if (relacija.getStatus().getStatus().equals("I") && indexZavrsneUSegmentu < indexPocetnaUSegmneu
					&& (indexPocetnaUSegmneu != stanice.size() - 1 || indexZavrsneUSegmentu != 0)) {
				relacije.remove(i);
			}
		}
	}

	private boolean provjeriJesuLiSveIspravneRelacijeUObicnomSmjeru() {
		List<Relacija> relacijeUObicnomSmjeru = dohvatiRelacijeUObicnomSmjeru();
		for (Relacija relacija : relacijeUObicnomSmjeru) {
			if (!relacija.getStatus().getStatus().equals("I"))
				return false;
		}
		return true;

	}

	private List<Relacija> dohvatiRelacijeUObicnomSmjeru() {
		List<Relacija> obicneRelacije = new ArrayList<Relacija>();
		for (Relacija relacija : relacije) {
			List<ZeljeznickaStanica> segment = this.stanice;
			int indexPocetnaUSegmneu = -1;
			int indexZavrsneUSegmentu = -1;

			ZeljeznickaStanica pocetna = relacija.getPocetnaStanica();
			ZeljeznickaStanica zavrsna = relacija.getZavrsnaStanica();

			for (ZeljeznickaStanica stanicaSegment : segment) {
				if (stanicaSegment.getNaziv().equals(pocetna.getNaziv())) {
					indexPocetnaUSegmneu = segment.indexOf(stanicaSegment);
					break;
				}
			}

			for (ZeljeznickaStanica stanicaSegment : segment) {
				if (stanicaSegment.getNaziv().equals(zavrsna.getNaziv())) {
					indexZavrsneUSegmentu = segment.indexOf(stanicaSegment);
					break;
				}
			}

			if (indexZavrsneUSegmentu > indexPocetnaUSegmneu) {
				obicneRelacije.add(relacija);
			}
		}
		return obicneRelacije;
	}

	private boolean provjeriJesuLiSveIspravneRelacijeUObrnutomSmjeru() {
		List<Relacija> relacijeUObrnutomSmjeru = dohvatiRelacijeUObrnutomSmjeru();
		for (Relacija relacija : relacijeUObrnutomSmjeru) {
			if (!relacija.getStatus().getStatus().equals("I"))
				return false;
		}
		return true;
	}

	private List<Relacija> dohvatiRelacijeUObrnutomSmjeru() {
		List<Relacija> obrnuteRelacije = new ArrayList<Relacija>();
		for (Relacija relacija : relacije) {
			List<ZeljeznickaStanica> segment = this.stanice;
			int indexPocetnaUSegmneu = -1;
			int indexZavrsneUSegmentu = -1;

			ZeljeznickaStanica pocetna = relacija.getPocetnaStanica();
			ZeljeznickaStanica zavrsna = relacija.getZavrsnaStanica();

			for (ZeljeznickaStanica stanicaSegment : segment) {
				if (stanicaSegment.getNaziv().equals(pocetna.getNaziv())) {
					indexPocetnaUSegmneu = segment.indexOf(stanicaSegment);
					break;
				}
			}

			for (ZeljeznickaStanica stanicaSegment : segment) {
				if (stanicaSegment.getNaziv().equals(zavrsna.getNaziv())) {
					indexZavrsneUSegmentu = segment.indexOf(stanicaSegment);
					break;
				}
			}

			if (indexZavrsneUSegmentu < indexPocetnaUSegmneu) {
				obrnuteRelacije.add(relacija);
			}
		}
		return obrnuteRelacije;
	}

	public List<Relacija> getRelacije() {
		return relacije;
	}

	public void kreirajIspravnuObicnuRelaciju() {
		if (!postojiLiIspravnaUObicnomSmjeru()) {
			Relacija relacija = new Relacija(this.stanice, this.oznaka);
			relacije.add(relacija);
		}
	}

	public void kreirajIspravnuObrnutuRelaciju() {
		if (!postojiLiIspravnaUObrnutomSmjeru()) {
			List<ZeljeznickaStanica> obrnuteStanice = new ArrayList<>(this.stanice);
			java.util.Collections.reverse(obrnuteStanice);
			Relacija relacija = new Relacija(obrnuteStanice, this.oznaka);
			relacije.add(relacija);
		}
	}

	public void izbrisiIspravnuObicnuRelaciju() {
		for (int i = 0; i < relacije.size(); i++) {
			Relacija relacija = relacije.get(i);
			if (relacija.getPocetnaStanica() == this.stanice.get(0)
					&& relacija.getZavrsnaStanica() == this.stanice.get(this.stanice.size() - 1)
					&& relacija.getStatus().getStatus().equals("I")) {
				relacije.remove(i);
				break;
			}
		}
	}

	public void izbrisiIspravnuObrnutuRelaciju() {
		for (int i = 0; i < relacije.size(); i++) {
			Relacija relacija = relacije.get(i);
			if (relacija.getPocetnaStanica() == this.stanice.get(this.stanice.size() - 1)
					&& relacija.getZavrsnaStanica() == this.stanice.get(0)
					&& relacija.getStatus().getStatus().equals("I")) {
				relacije.remove(i);
				break;
			}
		}
	}

	private boolean provjeriPreklapanjaZaSveRelacije(List<ZeljeznickaStanica> segment) {
		boolean obicanSmjer = jeLiSegmentUObicnomSmjeru(segment);
		List<Relacija> potrebneRelacije = null;
		if (obicanSmjer)
			potrebneRelacije = dohvatiRelacijeUObicnomSmjeru();
		else
			potrebneRelacije = dohvatiRelacijeUObrnutomSmjeru();
		for (Relacija relacija : potrebneRelacije) {
			boolean provjeriPreklapanje = provjeriPreklapanjeZaRelaciju(segment, relacija);
			if (provjeriPreklapanje && !relacija.getStatus().getStatus().equals("I")) {
				System.out.println("Preklapanje s relacijom--: ");
				relacija.ispisRelacije();
				return true;
			}
		}
		return false;
	}

	private boolean jeLiSegmentUObicnomSmjeru(List<ZeljeznickaStanica> segment) {
		List<ZeljeznickaStanica> sveStanice = this.stanice;

		int indexPocetnaUSegmneu = -1;
		int indexZavrsneUSegmentu = -1;

		ZeljeznickaStanica pocetna = segment.get(0);
		ZeljeznickaStanica zavrsna = segment.get(segment.size() - 1);

		for (ZeljeznickaStanica stanicaSegment : sveStanice) {
			if (stanicaSegment.getNaziv().equals(pocetna.getNaziv())) {
				indexPocetnaUSegmneu = sveStanice.indexOf(stanicaSegment);
				break;
			}
		}

		for (ZeljeznickaStanica stanicaSegment : sveStanice) {
			if (stanicaSegment.getNaziv().equals(zavrsna.getNaziv())) {
				indexZavrsneUSegmentu = sveStanice.indexOf(stanicaSegment);
				break;
			}
		}

		if (indexPocetnaUSegmneu < indexZavrsneUSegmentu)
			return true;
		return false;
	}

	private boolean provjeriPreklapanjeZaRelaciju(List<ZeljeznickaStanica> segment, Relacija relacija) {
		List<ZeljeznickaStanica> staniceRelacije = relacija.getStanice();

		for (ZeljeznickaStanica stanicaSegment : segment) {
			for (ZeljeznickaStanica stanicaRelacija : staniceRelacije) {
				if (stanicaSegment.getNaziv().equals(stanicaRelacija.getNaziv())) {
					return true;
				}
			}
		}
		return false;

	}

	public static RelacijaStatus dohvatiStatus(String oznakaStatusa) {
		switch (oznakaStatusa.toUpperCase()) {
		case "I":
			return new Ispravna();
		case "K":
			return new Kvar();
		case "T":
			return new Testiranje();
		case "Z":
			return new Zatvorena();
		default:
			throw new IllegalArgumentException("Nevažeći status: " + oznakaStatusa);
		}
	}

	private Pruga(PrugaBuilder builder) {
		this.oznaka = builder.oznaka;
		this.kategorija = builder.kategorija;
		this.nacinPrijevoza = builder.nacinPrijevoza;
		this.brojKolosijeka = builder.brojKolosijeka;
		this.duzina = builder.duzina;
		this.dopustenoOpterecenjeOsovina = builder.dopustenoOpterecenjeOsovina;
		this.dopustenoOpterecenjeDuzniMetar = builder.dopustenoOpterecenjeDuzniMetar;
		this.status = builder.status;
		this.stanice = builder.stanice;

	}

	public void setStanice(List<ZeljeznickaStanica> stanice) {
		this.stanice = stanice;
	}

	public static class PrugaBuilder {
		private String oznaka;
		private String kategorija;
		private String nacinPrijevoza;
		private int brojKolosijeka = 1;
		private int duzina = 0;
		private double dopustenoOpterecenjeOsovina = 10.0;
		private double dopustenoOpterecenjeDuzniMetar = 2.0;
		private String status = "ispravna";
		private List<ZeljeznickaStanica> stanice;;

		public PrugaBuilder setOznaka(String oznaka) {
			this.oznaka = oznaka;
			return this;
		}

		public PrugaBuilder setKategorija(String kategorija) {
			this.kategorija = kategorija;
			return this;
		}

		public PrugaBuilder setNacinPrijevoza(String nacinPrijevoza) {
			this.nacinPrijevoza = nacinPrijevoza;
			return this;
		}

		public PrugaBuilder setBrojKolosijeka(int brojKolosijeka) {
			this.brojKolosijeka = brojKolosijeka;
			return this;
		}

		public PrugaBuilder setDuzina(int duzina) {
			this.duzina = duzina;
			return this;
		}

		public PrugaBuilder setDopustenoOpterecenjeOsovina(double dopustenoOpterecenjeOsovina) {
			this.dopustenoOpterecenjeOsovina = dopustenoOpterecenjeOsovina;
			return this;
		}

		public PrugaBuilder setDopustenoOpterecenjeDuzniMetar(double dopustenoOpterecenjeDuzniMetar) {
			this.dopustenoOpterecenjeDuzniMetar = dopustenoOpterecenjeDuzniMetar;
			return this;
		}

		public PrugaBuilder setStatus(String status) {
			this.status = status;
			return this;
		}

		public PrugaBuilder setPocetnaStanica(List<ZeljeznickaStanica> stanice) {
			this.stanice = stanice;
			return this;
		}

		public Pruga build() {
			return new Pruga(this);
		}

	}

	public String getOznaka() {
		return oznaka;
	}

	public String getKategorija() {
		return kategorija;
	}

	public String getNacinPrijevoza() {
		return nacinPrijevoza;
	}

	public int getBrojKolosijeka() {
		return brojKolosijeka;
	}

	public int getDuzina() {
		return duzina;
	}

	public double getDopustenoOpterecenjeOsovina() {
		return dopustenoOpterecenjeOsovina;
	}

	public double getDopustenoOpterecenjeDuzniMetar() {
		return dopustenoOpterecenjeDuzniMetar;
	}

	public String getStatus() {
		return status;
	}

	public List<ZeljeznickaStanica> getStanice() {
		return stanice;
	}

	@Override
	public String toString() {
		return "Pruga {" + "oznaka='" + oznaka + '\'' + ", kategorija='" + kategorija + '\'' + ", nacinPrijevoza='"
				+ nacinPrijevoza + '\'' + ", brojKolosijeka=" + brojKolosijeka + ", duzina=" + duzina
				+ ", dopustenoOpterecenjeOsovina=" + dopustenoOpterecenjeOsovina + ", dopustenoOpterecenjeDuzniMetar="
				+ dopustenoOpterecenjeDuzniMetar + ", status='" + status + '\'' + '}';
	}

}
