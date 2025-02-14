package imilanovi20_zadaca_3.entiteti;

import java.util.List;

public class Kompozicija {
    private String oznaka;
    private List<String> oznakeVozila;
    private List<String> uloge;

    public Kompozicija(String oznaka, List<String> oznakeVozila, List<String> uloge) {
        this.oznaka = oznaka;
        this.oznakeVozila = oznakeVozila;
        this.uloge = uloge;
    }

    public String getOznaka() {
        return oznaka;
    }

    public List<String> getOznakeVozila() {
        return oznakeVozila;
    }

    public List<String> getUloge() {
        return uloge;
    }

    @Override
    public String toString() {
        return "Kompozicija{" +
                "oznaka='" + oznaka + '\'' +
                ", oznakeVozila=" + oznakeVozila +
                ", uloge=" + uloge +
                '}';
    }
}
