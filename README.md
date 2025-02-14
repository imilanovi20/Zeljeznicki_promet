# Željeznički promet

Na ovom repozitoriju nalazi se projekt iz kolegija *Uzorci dizajna* na Fakultetu organizacije i informatike.

Ova aplikacija služi za upravljanje željezničkim prometom i omogućuje korisnicima pregled pruga, vlakova, voznog reda, stanica i ostalih povezanih podataka.
Aplikacija se izvršava u terminalu i omogućuje interaktivno pretraživanje i simulaciju vožnje vlakova.

## 🚀 Pokretanje aplikacije

Za pokretanje aplikacije potrebno je prvo izgraditi projekt i generirati .jar datoteku koristeći Maven:
```bash
  mvn clean package
```
Nakon uspješnog generiranja .jar datoteke, aplikacija se pokreće pomoću sljedeće naredbe:

```bash
  java -jar {putanja do jar dokumenta} --zs DZ_3_stanice.csv --zps DZ_3_vozila.csv --zk DZ_3_kompozicije.csv --zvr DZ_3_vozni_red.csv --zod DZ_3_oznake_dana.csv
```
### 📂 Učitavanje podataka

Aplikacija učitava podatke iz CSV datoteka koje sadrže informacije o:

+  **Željezničkim stanicama** (DZ_3_stanice.csv)

+ **Prijevoznim sredstvima** (DZ_3_vozila.csv)

+ **Kompozicijama vlakova** (DZ_3_kompozicije.csv)

+ **Voznom redu** (DZ_3_vozni_red.csv)

+ **Oznakama dana u tjednu** (DZ_3_oznake_dana.csv)

Sustav koristi ove podatke za generiranje izvještaja, simulaciju vožnje vlakova i analizu željezničkog prometa.

## 📌 Popis svih komandi


Nakon pokretanja aplikacije u terminal se mogu unijeti neke od slijedećih komandi:



| **Komanda** | **Primjer** | **Ispis** |
|------------|------------|---------|
| **IP** | - | Ispis tablice s prugama (oznaka, početna i završna željeznička stanica, ukupan broj kilometara). |
| **ISP oznakaPruge redoslijed** | ISP M501 N | Ispis tablice sa željezničkim stanicama na odabranoj pruzi u normalnom redoslijedu. |
| **ISP oznakaPruge O** | ISP M501 O | Ispis tablice sa željezničkim stanicama na odabranoj pruzi u obrnutom redoslijedu. |
| **ISI2S polaznaStanica - odredišnaStanica** | ISI2S Donji Kraljevec - Čakovec | Ispis tablice sa željezničkim stanicama između dviju stanica. |
| **IK oznaka** | IK 8001 | Ispis tablice sa prijevoznim sredstvima u kompoziciji. |
| **IV** | - | Ispis tablice s vlakovima i njihovim podacima. |
| **IEV oznaka** | IEV 3609 | Ispis tablice sa etapama vlaka. |
| **IEVD dani** | IEVD PoSrPeN | Ispis tablice sa vlakovima i njihovim etapama koje voze na određene dane. |
| **IVRV oznaka** | IVRV 3609 | Ispis tablice sa svim željezničkim stanicama na kojima staje vlak. |
| **IVI2S polaznaStanica - odredišnaStanica - dan - odVr - doVr - prikaz** | IVI2S Donji Kraljevec - Čakovec - N - 0:00 - 23:59 - SPKV | Ispis tablice s vlakovima koji prometuju na određeni dan u zadanom vremenu. |
| **DK ime prezime** | DK Pero Kos | Dodaje se korisnik u registar korisnika. |
| **PK** | - | Ispis svih korisnika iz registra. |
| **DPK ime prezime - oznakaVlaka [- stanica]** | DPK Pero Kos - 3301 | Dodavanje korisnika za praćenje vlaka ili stanice. |
| **SVV oznaka - dan - koeficijent** | SVV 3609 - Po - 60 | Simulacija vožnje vlaka uz ubrzano virtualno vrijeme. |
| **CVP cijenaNormalni cijenaUbrzani cijenaBrzi popustSuN popustWebMob uvecanjeVlak** | CVP 0,10 0,12 0,15 20,0 10,0 10,0 | Postavljanje cijena vožnje vlakom i popusta. |
| **KKPV2S oznaka - polaznaStanica - odredišnaStanica - datum - načinKupovine** | KKPV2S 3609 - Donji Kraljevec - Čakovec - 10.01.2025. - WM | Kupovina karte za putovanje vlakom. |
| **IKKPV [n]** | IKKPV 3 | Ispis kupljenih karata za putovanje vlakom. |
| **UKP2S polaznaStanica - odredišnaStanica - datum - odVr - doVr - načinKupovine** | UKP2S Donji Kraljevec - Čakovec - 10.01.2025. - 0:00 - 23:59 - WM | Usporedba karata za putovanje vlakom. |
| **PSP2S oznaka - polaznaStanica - odredišnaStanica - status** | PSP2S M501 - Donji Kraljevec – Mala Subotica - K | Promjena statusa pruge između dviju stanica. |
| **IRPS status [oznaka]** | IRPS Z M501 | Ispis relacija pruga sa zadanim statusom. |
| **Q** | - | Prekid rada programa. |
