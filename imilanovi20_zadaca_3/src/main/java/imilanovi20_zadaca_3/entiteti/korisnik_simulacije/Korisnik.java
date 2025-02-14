package imilanovi20_zadaca_3.entiteti.korisnik_simulacije;

import imilanovi20_zadaca_3.podatci.CitacPodataka;

public class Korisnik extends Observer {
	private String ime_i_prezime;
	
    public Korisnik(String ime_i_prezime) {
        this.ime_i_prezime = ime_i_prezime;
    }
    
    @Override
	protected void obavijesti(String poruke) {
		System.out.println("Obavijest za korisnika " + ime_i_prezime + ": " + poruke);	
	}
    
    public String getImeIPrezime() { return ime_i_prezime; }
	

}
