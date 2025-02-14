package imilanovi20_zadaca_3.stanjaRelacija;

import imilanovi20_zadaca_3.entiteti.Relacija;

public class Kvar implements RelacijaStatus {

    @Override
    public void promijeniStatus(Relacija relacija, RelacijaStatus noviStatus) {
        if (PravilaPrijelaza.jePrijelazDozvoljen(this.getStatus(), noviStatus.getStatus())) {
            relacija.setStatus(noviStatus);
            System.out.println("Status relacije prebačen na: " + noviStatus.getStatus());
        } else {
            System.out.printf("Nevažeći prijelaz iz stanja '%s' u stanje '%s'.\n", this.getStatus(), noviStatus.getStatus());
        }
    }

    @Override
    public boolean dozvoljenoPutovanje() {
        return false;
    }

    @Override
    public String getStatus() {
        return "K"; 
    }

}
