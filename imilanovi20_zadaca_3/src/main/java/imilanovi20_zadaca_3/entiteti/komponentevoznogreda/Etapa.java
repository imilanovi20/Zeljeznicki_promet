package imilanovi20_zadaca_3.entiteti.komponentevoznogreda;

import java.util.List;

import imilanovi20_zadaca_3.entiteti.Pruga;
import imilanovi20_zadaca_3.entiteti.ZeljeznickaStanica;

public class Etapa implements KomponentaVoznogReda {
	private String oznakaPruge;
	private String oznakaVlaka;
	private String vrstaVlaka;
	private String smjer;
	private List<ZeljeznickaStanica> stanice;
	private String vrijemePolaska;
	private String vrijemeDolaska;
	private String daniUTjednu;
	private Pruga pruga;

	public Etapa(String oznakaPruge, String smjer, String oznakaVlaka, String vrstaVlaka,
			List<ZeljeznickaStanica> stanice, String vrijemePolaska, String vrijemeDolaska, String daniUTjednu) {
		this.oznakaPruge = oznakaPruge;
		this.oznakaVlaka = oznakaVlaka;
		this.vrstaVlaka = vrstaVlaka;
		this.smjer = smjer;
		this.stanice = stanice;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.daniUTjednu = daniUTjednu;
	}
	
	public void setPruga(Pruga pruga) {
		this.pruga = pruga;
	}
	
	public Pruga getPruga() {
		return this.pruga;
	}

	@Override
	public int getDuzina() {
		int ukupnaDuzina = 0;
		for (ZeljeznickaStanica stanica : stanice) {
			ukupnaDuzina += stanica.getDuzina();
		}
		return ukupnaDuzina;
	}

	public String getOznakaPruge() {
		return oznakaPruge;
	}

	public List<ZeljeznickaStanica> getStanice() {
		return stanice;
	}

	public String getVrijemePolaska() {
		return vrijemePolaska;
	}

	public String getVrijemeDolaska() {
		return vrijemeDolaska;
	}

	public String getDaniUTjednu() {
		return daniUTjednu;
	}

	public String getVrtstaVlaka() {
		return vrstaVlaka;
	}

	public String getOznakaVlaka() {
		return oznakaVlaka;
	}

	public ZeljeznickaStanica getPolaznaStanica() {
		return stanice.get(0);
	}

	public ZeljeznickaStanica getOdredisnaStanica() {
		return stanice.get(stanice.size() - 1);
	}

	public int dohvatiVrijemeZaStanicu(ZeljeznickaStanica stanica) {
		switch (getVrtstaVlaka()) {
		case "N":
			return stanica.getVrijemeNormalniVlak();
		case "U":
			return stanica.getVrijemeUbrzaniVlak() != null ? stanica.getVrijemeUbrzaniVlak() : 0;
		case "B":
			return stanica.getVrijemeBrziVlak() != null ? stanica.getVrijemeBrziVlak() : 0;
		default:
			return 0;
		}
	}

	public String dohvatiPolazakSaStanice(ZeljeznickaStanica trazenaStanica) {
		int ukupnoVrijeme = 0;
		
		if("O".equals(smjer)) {
			for (int i = 1; i<stanice.size()+1; i++) {
				ukupnoVrijeme += dohvatiVrijemeZaStanicu(stanice.get(i-1));
				if (stanice.get(i-1).equals(trazenaStanica)) {
					break;
				}
			}
		}
		else {
			for (ZeljeznickaStanica stanica : stanice) {
				ukupnoVrijeme += dohvatiVrijemeZaStanicu(stanica);
				if (stanica.equals(trazenaStanica)) {
					break;
				}
			}
		}

		String[] poljeVrijeme = vrijemePolaska.split(":");
		int sati = Integer.parseInt(poljeVrijeme[0]);
		int minute = Integer.parseInt(poljeVrijeme[1]);
		
		minute += ukupnoVrijeme;
		sati += minute / 60;
		minute %= 60;
		sati %= 24;

		return String.format("%02d:%02d", sati, minute);
	}
}
