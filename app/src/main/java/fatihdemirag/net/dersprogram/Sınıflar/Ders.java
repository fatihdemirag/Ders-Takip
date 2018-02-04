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


    public Ders(String dersAdi, String dersGun, String dersBaslangicSaati, String dersBitisSaati, int dersId, int dersPozisyon) {
        this.dersAdi = dersAdi;
        this.dersGun = dersGun;
        this.dersBaslangicSaati = dersBaslangicSaati;
        this.dersBitisSaati = dersBitisSaati;
        this.dersId = dersId;
        this.dersPozisyon = dersPozisyon;
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
