package imilanovi20_zadaca_3.entiteti;

public class PrijevoznoSredstvo {

    private String oznaka;
    private String opis;
    private String proizvodac;
    private int godinaProizvodnje;
    private String namjena;
    private String vrstaPrijevoza;
    private String vrstaPogona;
    private int maksimalnaBrzina;
    private double maksimalnaSnaga;
    private int brojSjedecihMjesta;
    private int brojStajacihMjesta;
    private int brojBicikala;
    private int brojKreveta;
    private int brojAutomobila;
    private double nosivost;
    private double povrsina;
    private double zapremina;
    private String status;

    private PrijevoznoSredstvo(PrijevoznoSredstvoBuilder builder) {
        this.oznaka = builder.oznaka;
        this.opis = builder.opis;
        this.proizvodac = builder.proizvodac;
        this.godinaProizvodnje = builder.godinaProizvodnje;
        this.namjena = builder.namjena;
        this.vrstaPrijevoza = builder.vrstaPrijevoza;
        this.vrstaPogona = builder.vrstaPogona;
        this.maksimalnaBrzina = builder.maksimalnaBrzina;
        this.maksimalnaSnaga = builder.maksimalnaSnaga;
        this.brojSjedecihMjesta = builder.brojSjedecihMjesta;
        this.brojStajacihMjesta = builder.brojStajacihMjesta;
        this.brojBicikala = builder.brojBicikala;
        this.brojKreveta = builder.brojKreveta;
        this.brojAutomobila = builder.brojAutomobila;
        this.nosivost = builder.nosivost;
        this.povrsina = builder.povrsina;
        this.zapremina = builder.zapremina;
        this.status = builder.status;
    }

    public static class PrijevoznoSredstvoBuilder {
        private String oznaka;
        private String opis;
        private String proizvodac;
        private int godinaProizvodnje;
        private String namjena;
        private String vrstaPrijevoza;
        private String vrstaPogona;
        private int maksimalnaBrzina = 1;
        private double maksimalnaSnaga = -1;
        private int brojSjedecihMjesta;
        private int brojStajacihMjesta;
        private int brojBicikala;
        private int brojKreveta;
        private int brojAutomobila;
        private double nosivost;
        private double povrsina;
        private double zapremina;
        private String status = "ispravno";

        public PrijevoznoSredstvoBuilder setOznaka(String oznaka) {
            this.oznaka = oznaka;
            return this;
        }

        public PrijevoznoSredstvoBuilder setOpis(String opis) {
            this.opis = opis;
            return this;
        }

        public PrijevoznoSredstvoBuilder setProizvodac(String proizvodac) {
            this.proizvodac = proizvodac;
            return this;
        }

        public PrijevoznoSredstvoBuilder setGodinaProizvodnje(int godinaProizvodnje) {
            this.godinaProizvodnje = godinaProizvodnje;
            return this;
        }

        public PrijevoznoSredstvoBuilder setNamjena(String namjena) {
            this.namjena = namjena;
            return this;
        }

        public PrijevoznoSredstvoBuilder setVrstaPrijevoza(String vrstaPrijevoza) {
            this.vrstaPrijevoza = vrstaPrijevoza;
            return this;
        }

        public PrijevoznoSredstvoBuilder setVrstaPogona(String vrstaPogona) {
            this.vrstaPogona = vrstaPogona;
            return this;
        }

        public PrijevoznoSredstvoBuilder setMaksimalnaBrzina(int maksimalnaBrzina) {
            this.maksimalnaBrzina = maksimalnaBrzina;
            return this;
        }

        public PrijevoznoSredstvoBuilder setMaksimalnaSnaga(double maksimalnaSnaga) {
            this.maksimalnaSnaga = maksimalnaSnaga;
            return this;
        }

        public PrijevoznoSredstvoBuilder setBrojSjedecihMjesta(int brojSjedecihMjesta) {
            this.brojSjedecihMjesta = brojSjedecihMjesta;
            return this;
        }

        public PrijevoznoSredstvoBuilder setBrojStajacihMjesta(int brojStajacihMjesta) {
            this.brojStajacihMjesta = brojStajacihMjesta;
            return this;
        }

        public PrijevoznoSredstvoBuilder setBrojBicikala(int brojBicikala) {
            this.brojBicikala = brojBicikala;
            return this;
        }

        public PrijevoznoSredstvoBuilder setBrojKreveta(int brojKreveta) {
            this.brojKreveta = brojKreveta;
            return this;
        }

        public PrijevoznoSredstvoBuilder setBrojAutomobila(int brojAutomobila) {
            this.brojAutomobila = brojAutomobila;
            return this;
        }

        public PrijevoznoSredstvoBuilder setNosivost(double nosivost) {
            this.nosivost = nosivost;
            return this;
        }

        public PrijevoznoSredstvoBuilder setPovrsina(double povrsina) {
            this.povrsina = povrsina;
            return this;
        }

        public PrijevoznoSredstvoBuilder setZapremina(double zapremina) {
            this.zapremina = zapremina;
            return this;
        }

        public PrijevoznoSredstvoBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public PrijevoznoSredstvo build() {
            return new PrijevoznoSredstvo(this);
        }
    }

    public String getOznaka() { return oznaka; }
    public String getOpis() { return opis; }
    public String getProizvodac() { return proizvodac; }
    public int getGodinaProizvodnje() { return godinaProizvodnje; }
    public String getNamjena() { return namjena; }
    public String getVrstaPrijevoza() { return vrstaPrijevoza; }
    public String getVrstaPogona() { return vrstaPogona; }
    public int getMaksimalnaBrzina() { return maksimalnaBrzina; }
    public double getMaksimalnaSnaga() { return maksimalnaSnaga; }
    public int getBrojSjedecihMjesta() { return brojSjedecihMjesta; }
    public int getBrojStajacihMjesta() { return brojStajacihMjesta; }
    public int getBrojBicikala() { return brojBicikala; }
    public int getBrojKreveta() { return brojKreveta; }
    public int getBrojAutomobila() { return brojAutomobila; }
    public double getNosivost() { return nosivost; }
    public double getPovrsina() { return povrsina; }
    public double getZapremina() { return zapremina; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "PrijevoznoSredstvo{" +
                "oznaka='" + oznaka + '\'' +
                ", opis='" + opis + '\'' +
                ", proizvodac='" + proizvodac + '\'' +
                ", godinaProizvodnje=" + godinaProizvodnje +
                ", namjena='" + namjena + '\'' +
                ", vrstaPrijevoza='" + vrstaPrijevoza + '\'' +
                ", vrstaPogona='" + vrstaPogona + '\'' +
                ", maksimalnaBrzina=" + maksimalnaBrzina +
                ", maksimalnaSnaga=" + maksimalnaSnaga +
                ", brojSjedecihMjesta=" + brojSjedecihMjesta +
                ", brojStajacihMjesta=" + brojStajacihMjesta +
                ", brojBicikala=" + brojBicikala +
                ", brojKreveta=" + brojKreveta +
                ", brojAutomobila=" + brojAutomobila +
                ", nosivost=" + nosivost +
                ", povrsina=" + povrsina +
                ", zapremina=" + zapremina +
                ", status='" + status + '\'' +
                '}';
    }
}
