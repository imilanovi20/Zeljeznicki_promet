package imilanovi20_zadaca_3.stanjaRelacija;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PravilaPrijelaza {
    private static final Map<String, Set<String>> pravila = new HashMap<>();

    static {
        dodajPravilo("I", "K");
        dodajPravilo("I", "Z");
        dodajPravilo("K", "T");
        dodajPravilo("T", "I");
        dodajPravilo("Z", "T");
    }

    private static void dodajPravilo(String iz, String u) {
        pravila.computeIfAbsent(iz, k -> new HashSet<>()).add(u);
    }

    public static boolean jePrijelazDozvoljen(String iz, String u) {
        return pravila.containsKey(iz) && pravila.get(iz).contains(u);
    }
}

