package fatihdemirag.net.dersprogram.Sınıflar;

/**
 * Created by fxd on 07.07.2017.
 */

public class Ders {
    String dersAdi;
    String dersGun;
    String dersBaslangicSaati;
    String dersBitisSaati;
    int dersId;
    int dersPozisyon;
    String dersTenefusSuresi;
    boolean tenefusAktifMi;
    String butonYazisi;
    String sira;
    int onayAktifMi;
    int notEkleAktifMi;
    String tenefusSuresiBaslik;

    public String getTenefusSuresiBaslik() {
        return tenefusSuresiBaslik;
    }

    public void setTenefusSuresiBaslik(String tenefusSuresiBaslik) {
        this.tenefusSuresiBaslik = tenefusSuresiBaslik;
    }

    public int getNotEkleAktifMi() {
        return notEkleAktifMi;
    }

    public void setNotEkleAktifMi(int notEkleAktifMi) {
        this.notEkleAktifMi = notEkleAktifMi;
    }

    public int getOnayAktifMi() {
        return onayAktifMi;
    }

    public void setOnayAktifMi(int onayAktifMi) {
        this.onayAktifMi = onayAktifMi;
    }

    public Ders(String dersAdi, String dersGun, String dersBaslangicSaati, String dersBitisSaati, int dersId, int dersPozisyon, String dersTenefusSuresi, String butonYazisi, String sira, int onayAktifMi, int notEkleAktifMi, String tenefusSuresiBaslik) {
        this.dersAdi = dersAdi;
        this.dersGun = dersGun;
        this.dersBaslangicSaati = dersBaslangicSaati;
        this.dersBitisSaati = dersBitisSaati;
        this.dersId = dersId;
        this.dersPozisyon = dersPozisyon;
        this.dersTenefusSuresi = dersTenefusSuresi;
        this.butonYazisi = butonYazisi;
        this.sira = sira;
        this.onayAktifMi = onayAktifMi;
        this.notEkleAktifMi = notEkleAktifMi;
        this.tenefusSuresiBaslik = tenefusSuresiBaslik;
    }


    public String getSira() {
        return sira;
    }

    public void setSira(String sira) {
        this.sira = sira;
    }


    public String getButonYazisi() {
        return butonYazisi;
    }

    public void setButonYazisi(String butonYazisi) {
        this.butonYazisi = butonYazisi;
    }

    public boolean isTenefusAktifMi() {
        return tenefusAktifMi;
    }

    public void setTenefusAktifMi(boolean tenefusAktifMi) {
        this.tenefusAktifMi = tenefusAktifMi;
    }

    public String getDersTenefusSuresi() {
        return dersTenefusSuresi;
    }

    public void setDersTenefusSuresi(String dersTenefusSuresi) {
        this.dersTenefusSuresi = dersTenefusSuresi;
    }


    public Ders() {

    }
    public int getDersId() {
        return dersId;
    }

    public void setDersId(int dersId) {
        this.dersId = dersId;
    }
    public String getDersGun() {
        return dersGun;
    }

    public void setDersGun(String dersGun) {
        this.dersGun = dersGun;
    }


    public String getDersAdi() {
        return dersAdi;
    }

    public void setDersAdi(String dersAdi) {
        this.dersAdi = dersAdi;
    }

    public String getDersBaslangicSaati() {
        return dersBaslangicSaati;
    }

    public void setDersBaslangicSaati(String dersBaslangicSaati) {
        this.dersBaslangicSaati = dersBaslangicSaati;
    }

    public String getDersBitisSaati() {
        return dersBitisSaati;
    }

    public void setDersBitisSaati(String dersBitisSaati) {
        this.dersBitisSaati = dersBitisSaati;
    }

    public int getDersPozisyon() {
        return dersPozisyon;
    }

    public void setDersPozisyon(int dersPozisyon) {
        this.dersPozisyon = dersPozisyon;
    }
}
