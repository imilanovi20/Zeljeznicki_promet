package imilanovi20_zadaca_3.strategijePlacanja;

import imilanovi20_zadaca_3.entiteti.CijenikKarte;

public class PlacanjeBlagajnaStrategy implements PlacanjeStartegy {

	@Override
	public double izracunajCijenuKarte(double osnovnaCijena, int kilometri, CijenikKarte cijenik, double popustSuNe) {		
		return (osnovnaCijena - (osnovnaCijena*(popustSuNe)/100))*kilometri;
	}

	@Override
	public String getNazivStrategije() {
		return "Plaćanje na blagajni";
	}



}
