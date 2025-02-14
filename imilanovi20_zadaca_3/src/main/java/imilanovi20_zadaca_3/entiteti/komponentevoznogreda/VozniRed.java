package imilanovi20_zadaca_3.entiteti.komponentevoznogreda;

import java.util.ArrayList;
import java.util.List;

public class VozniRed implements KomponentaVoznogReda {
	private List<KomponentaVoznogReda> vlakovi;

	public VozniRed() {
		this.vlakovi = new ArrayList<>();
	}

	public void dodajVlak(KomponentaVoznogReda vlak) {
		vlakovi.add(vlak);
	}

	@Override
	public int getDuzina() {
		int ukupnaDuzina = 0;
		for (KomponentaVoznogReda vlak : vlakovi) {
			ukupnaDuzina += vlak.getDuzina();
		}
		return ukupnaDuzina;
	}
	
    public List<KomponentaVoznogReda> getVlakovi() {
        return vlakovi;
    }
}
