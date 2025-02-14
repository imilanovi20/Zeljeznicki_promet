package imilanovi20_zadaca_3.strategijePlacanja;

import imilanovi20_zadaca_3.entiteti.CijenikKarte;

public interface PlacanjeStartegy {
	double izracunajCijenuKarte(double osnovnaCijena, int kilometri, CijenikKarte cijenik, double popustSuNe);
	String getNazivStrategije();
}
