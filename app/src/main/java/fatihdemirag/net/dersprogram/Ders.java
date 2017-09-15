package fatihdemirag.net.dersprogram;

/**
 * Created by fxd on 07.07.2017.
 */

public class Ders {
    String dersAdi;
    String dersGun;
    String dersBaslangicSaati;
    String dersBitisSaati;
    int dersId;




    public Ders(String dersAdi,String dersBaslangicSaati,String dersBitisSaati,int dersId)
    {
        this.dersAdi=dersAdi;
        this.dersBaslangicSaati=dersBaslangicSaati;
        this.dersBitisSaati=dersBitisSaati;
        this.dersId=dersId;
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
}
