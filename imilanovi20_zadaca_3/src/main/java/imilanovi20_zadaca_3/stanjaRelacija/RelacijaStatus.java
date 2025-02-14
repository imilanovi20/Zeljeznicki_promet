package imilanovi20_zadaca_3.stanjaRelacija;

import imilanovi20_zadaca_3.entiteti.Relacija;

public interface RelacijaStatus {
    void promijeniStatus(Relacija relacija, RelacijaStatus noviStatus);
    boolean dozvoljenoPutovanje();
    String getStatus();
}

