package imilanovi20_zadaca_3.strategijePlacanja;

import imilanovi20_zadaca_3.entiteti.CijenikKarte;

public class WebMobPlacanjeStrategy implements PlacanjeStartegy {

	@Override
	public double izracunajCijenuKarte(double osnovnaCijena, int kilometri, CijenikKarte cijenik, double popustSuNe) {
		double cijena = (osnovnaCijena - (osnovnaCijena*(popustSuNe/100)))*kilometri;
		return cijena - (cijena * (cijenik.getPopustWebMob()/100));
	}

	@Override
	public String getNazivStrategije() {
		return "Plaćanje putem web/mobilne aplikacije";
	}

}
