package imilanovi20_zadaca_3.upravljanje_kartama;

import java.time.LocalDate;


public class KartaMemento {
	private String oznakaVlaka;
	private String pocetnaStanica;
	private String odredisnaStanica;
	private LocalDate datum;
	private double osnovnaCijena;
	private double popustSuN;
	private double konacnaCijena;
	private String nacinPlacanja;
	private String vrijemePolaska;
	private String vrijemeDolaska;
	
    public KartaMemento(String oznakaVlaka, String pocetnaStanica, String odredisnaStanica,
            LocalDate datum, double osnovnaCijena, double popustSuN,
            double konacnaCijena, String nacinPlacanja, String vrijemePolaska, String vrijemeDolaska) {
		this.oznakaVlaka = oznakaVlaka;
		this.pocetnaStanica = pocetnaStanica;
		this.odredisnaStanica = odredisnaStanica;
		this.datum = datum;
		this.osnovnaCijena = osnovnaCijena;
		this.popustSuN = popustSuN;
		this.konacnaCijena = konacnaCijena;
		this.nacinPlacanja = nacinPlacanja;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
	}

    public String getOznakaVlaka() {
        return oznakaVlaka;
    }

    public String getPocetnaStanica() {
        return pocetnaStanica;
    }

    public String getOdredisnaStanica() {
        return odredisnaStanica;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public double getOsnovnaCijena() {
        return osnovnaCijena;
    }

    public double getPopustSuN() {
        return popustSuN;
    }

    public double getKonacnaCijena() {
        return konacnaCijena;
    }

    public String getNacinPlacanja() {
        return nacinPlacanja;
    }

    public String getVrijemePolaska() {
        return vrijemePolaska;
    }

    public String getVrijemeDolaska() {
        return vrijemeDolaska;
    }

    public void ispisiMemento() {
    	
    	String relacija = pocetnaStanica + " - " + odredisnaStanica;
        relacija = skratiTekst(relacija, 25); 
        
        System.out.printf("%-12s | %-25s | %-12s | %-7s | %-7s | %-4.2f € | %-4.2f %% | %-4.2f € | %-25s\n",
                oznakaVlaka,
                relacija,
                datum,
                vrijemePolaska,
                vrijemeDolaska,
                osnovnaCijena,
                popustSuN,
                konacnaCijena,
                nacinPlacanja);
    }
    
    private String skratiTekst(String tekst, int maxDuljina) {
        if (tekst.length() > maxDuljina) {
            return tekst.substring(0, maxDuljina - 3) + "...";
        }
        return tekst;
    }
}

