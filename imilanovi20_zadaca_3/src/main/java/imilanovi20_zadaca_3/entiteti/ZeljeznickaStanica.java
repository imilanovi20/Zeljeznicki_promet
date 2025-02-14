package imilanovi20_zadaca_3.entiteti;

import imilanovi20_zadaca_3.entiteti.korisnik_simulacije.Subject;

public class ZeljeznickaStanica extends Subject {
    private String naziv;
    private String vrstaStanice;
    private String statusStanice;
    private String putniciUlIiz;
    private String robaUtIist;
    private int brojPerona;
    private int duzina;
    private int vrijemeNormalniVlak;
    private Integer vrijemeUbrzaniVlak;
    private Integer vrijemeBrziVlak;

    private ZeljeznickaStanica(ZeljeznickaStanicaBuilder builder) {
        this.naziv = builder.naziv;
        this.vrstaStanice = builder.vrstaStanice;
        this.statusStanice = builder.statusStanice;
        this.putniciUlIiz = builder.putniciUlIiz;
        this.robaUtIist = builder.robaUtIist;
        this.brojPerona = builder.brojPerona;
        this.duzina = builder.duzina;
        this.vrijemeNormalniVlak = builder.vrijemeNormalniVlak;
        this.vrijemeUbrzaniVlak = builder.vrijemeUbrzaniVlak;
        this.vrijemeBrziVlak = builder.vrijemeBrziVlak;
    }

    public String getNaziv() { return naziv; }
    public String getVrstaStanice() { return vrstaStanice; }
    public String getStatusStanice() { return statusStanice; }
    public String getPutniciUlIiz() { return putniciUlIiz; }
    public String getRobaUtIist() { return robaUtIist; }
    public int getBrojPerona() { return brojPerona; }
    public int getDuzina() { return duzina; }
    public int getVrijemeNormalniVlak() { return vrijemeNormalniVlak; }
    public Integer getVrijemeUbrzaniVlak() { return vrijemeUbrzaniVlak; }
    public Integer getVrijemeBrziVlak() { return vrijemeBrziVlak; }
    
    public void setVrijemeNormalniVlak(int vrijeme) { this.vrijemeNormalniVlak = vrijeme; }
    public void setVrijemeUbrzaniVlak(int vrijeme) { this.vrijemeUbrzaniVlak = vrijeme; }
    public void setVrijemeBrziVlak(int vrijeme) { this.vrijemeBrziVlak = vrijeme; }

    public static class ZeljeznickaStanicaBuilder {
        private String naziv;
        private String vrstaStanice;
        private String statusStanice;
        private String putniciUlIiz;
        private String robaUtIist;
        private int brojPerona;
        private int duzina;
        private int vrijemeNormalniVlak;
        private Integer vrijemeUbrzaniVlak;
        private Integer vrijemeBrziVlak;

        public ZeljeznickaStanicaBuilder setNaziv(String naziv) {
            this.naziv = naziv;
            return this;
        }

        public ZeljeznickaStanicaBuilder setVrstaStanice(String vrstaStanice) {
            this.vrstaStanice = vrstaStanice;
            return this;
        }

        public ZeljeznickaStanicaBuilder setStatusStanice(String statusStanice) {
            this.statusStanice = statusStanice;
            return this;
        }

        public ZeljeznickaStanicaBuilder setPutniciUlIiz(String putniciUlIiz) {
            this.putniciUlIiz = putniciUlIiz;
            return this;
        }

        public ZeljeznickaStanicaBuilder setRobaUtIist(String robaUtIist) {
            this.robaUtIist = robaUtIist;
            return this;
        }

        public ZeljeznickaStanicaBuilder setBrojPerona(int brojPerona) {
            this.brojPerona = brojPerona;
            return this;
        }

        public ZeljeznickaStanicaBuilder setDuzina(int duzina) {
            this.duzina = duzina;
            return this;
        }

        public ZeljeznickaStanicaBuilder setVrijemeNormalniVlak(int vrijemeNormalniVlak) {
            this.vrijemeNormalniVlak = vrijemeNormalniVlak;
            return this;
        }

        public ZeljeznickaStanicaBuilder setVrijemeUbrzaniVlak(Integer vrijemeUbrzaniVlak) {
            this.vrijemeUbrzaniVlak = vrijemeUbrzaniVlak;
            return this;
        }

        public ZeljeznickaStanicaBuilder setVrijemeBrziVlak(Integer vrijemeBrziVlak) {
            this.vrijemeBrziVlak = vrijemeBrziVlak;
            return this;
        }

        public ZeljeznickaStanica build() {
            return new ZeljeznickaStanica(this);
        }
    }
}
