package imilanovi20_zadaca_3.entiteti.korisnik_simulacije;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private List<Observer> pretplatniciNaVlak = new ArrayList<>();
    private List<Observer> pretplatniciNaStanicu = new ArrayList<>();

    public void dodajPretplatnika(Observer observer) {
        pretplatniciNaVlak.add(observer);
    }

    public void izbrisiPretplatnike(Observer observer) {
        pretplatniciNaVlak.remove(observer);
    }

    public void obavijestiPretplatnike(String poruka) {
        for (Observer pretplatnik : pretplatniciNaVlak) {
            pretplatnik.obavijesti(poruka);
        }
    }
}
