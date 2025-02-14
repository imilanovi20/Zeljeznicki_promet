package imilanovi20_zadaca_3.stanjaRelacija;

import imilanovi20_zadaca_3.entiteti.Relacija;

public class Zatvorena implements RelacijaStatus {

    @Override
    public void promijeniStatus(Relacija relacija, RelacijaStatus noviStatus) {
        if (PravilaPrijelaza.jePrijelazDozvoljen(this.getStatus(), noviStatus.getStatus())) {
            relacija.setStatus(noviStatus);
            System.out.println("Zatvorena relacija postavljena na testiranje.");
        } else {
            System.out.println("Zatvorena relacija može prijeći samo u testiranje.");
        }
    }

    @Override
    public boolean dozvoljenoPutovanje() {
        return false;
    }

    @Override
    public String getStatus() {
        return "Z";
    }

}
