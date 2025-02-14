package imilanovi20_zadaca_3.entiteti;

import imilanovi20_zadaca_3.stanjaRelacija.Ispravna;
import imilanovi20_zadaca_3.stanjaRelacija.RelacijaStatus;

import java.util.List;

public class Relacija {
    private List<ZeljeznickaStanica> stanice;
    private String oznakaPruge;
    private RelacijaStatus status;

    public Relacija(List<ZeljeznickaStanica> stanice, String oznakaPruge) {
        this.stanice = stanice;
        this.oznakaPruge = oznakaPruge;
        this.status = new Ispravna();
    }

    public List<ZeljeznickaStanica> getStanice() {
        return stanice;
    }

    public RelacijaStatus getStatus() {
        return status;
    }

    public void setStatus(RelacijaStatus status) {
        this.status = status;
    }

    public boolean dozvoljenoPutovanje() {
        return status.dozvoljenoPutovanje();
    }

    public void promijeniStatus(RelacijaStatus noviStatus) {
        status.promijeniStatus(this, noviStatus);
        ispisRelacije();
    }

	public ZeljeznickaStanica getPocetnaStanica() {
		return stanice.get(0);
	}

	public ZeljeznickaStanica getZavrsnaStanica() {
		return stanice.get(stanice.size() - 1);
	}
	
	public void ispisRelacije() {
        System.out.println("[Relacija " + getPocetnaStanica().getNaziv() + "-" + getZavrsnaStanica().getNaziv() + " pruge " +
        		oznakaPruge + "]: status " + getStatus().getStatus());	
	}

}
