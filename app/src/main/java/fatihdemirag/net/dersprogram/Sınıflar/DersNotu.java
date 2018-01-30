package fatihdemirag.net.dersprogram.Sınıflar;

/**
 * Created by fxd on 24.08.2017.
 */

public class DersNotu {
    String dersKonusu;
    String dersNotu;
    byte[] notResmi;
    String ders;
    int id;



    public DersNotu(){}

    public DersNotu(String dersKonusu,String dersNotu,byte[] notResmi,String ders,int id)
    {
        this.dersKonusu=dersKonusu;
        this.dersNotu=dersNotu;
        this.notResmi=notResmi;
        this.ders=ders;
        this.id=id;
    }

    public String getDers() {
        return ders;
    }

    public void setDers(String ders) {
        this.ders = ders;
    }

    public String getDersKonusu() {
        return dersKonusu;
    }

    public void setDersKonusu(String dersKonusu) {
        this.dersKonusu = dersKonusu;
    }

    public String getDersNotu() {
        return dersNotu;
    }

    public void setDersNotu(String dersNotu) {
        this.dersNotu = dersNotu;
    }

    public byte[] getNotResmi() {
        return notResmi;
    }

    public void setNotResmi(byte[] notResmi) {
        this.notResmi = notResmi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
