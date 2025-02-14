package imilanovi20_zadaca_3.entiteti;

public class CijenikKarte {
    private double cijenaNormalni;
    private double cijenaUbrzani;
    private double cijenaBrzi;
    private double popustSuN;
    private double popustWebMob;
    private double uvecanjeVlak;

    public void setCijenaNormalni(double cijenaNormalni) {
        this.cijenaNormalni = cijenaNormalni;
    }

    public double getCijenaNormalni() {
        return cijenaNormalni;
    }

    public void setCijenaUbrzani(double cijenaUbrzani) {
        this.cijenaUbrzani = cijenaUbrzani;
    }

    public double getCijenaUbrzani() {
        return cijenaUbrzani;
    }

    public void setCijenaBrzi(double cijenaBrzi) {
        this.cijenaBrzi = cijenaBrzi;
    }

    public double getCijenaBrzi() {
        return cijenaBrzi;
    }

    public void setPopustSuN(double popustSuN) {
        this.popustSuN = popustSuN;
    }

    public double getPopustSuN() {
        return popustSuN;
    }

    public void setPopustWebMob(double popustWebMob) {
        this.popustWebMob = popustWebMob;
    }

    public double getPopustWebMob() {
        return popustWebMob;
    }

    public void setUvecanjeVlak(double uvecanjeVlak) {
        this.uvecanjeVlak = uvecanjeVlak;
    }

    public double getUvecanjeVlak() {
        return uvecanjeVlak;
    }
    
    public void ispisiCjenik() {
        System.out.println("Cjenik karata:");
		System.out.println("------------------------------------------------");
        System.out.printf("- Cijena normalni vlak: %.2f kn\n", cijenaNormalni);
        System.out.printf("- Cijena ubrzani vlak: %.2f kn\n", cijenaUbrzani);
        System.out.printf("- Cijena brzi vlak: %.2f kn\n", cijenaBrzi);
        System.out.printf("- Popust studenti i umirovljenici: %.2f%%\n", popustSuN);
        System.out.printf("- Popust za online kupovinu: %.2f%%\n", popustWebMob);
        System.out.printf("- Uvećanje za luksuzni vlak: %.2f%%\n", uvecanjeVlak);
		System.out.println("------------------------------------------------");
    }
}
