package fatihdemirag.net.dersprogram.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class DbHelper extends SQLiteOpenHelper{

    public static final String dbName="ders_programi";

    private static final String table = "dersler_programi";
    private static final int version = 11;
    public static final String col_1="ders_adi";
    public static final String col_2="ders_gunu";
    public static final String col_3="ders_baslangic_saati";
    public static final String col_4="ders_bitis_saati";
    public static final String col_5 = "ders_pozisyon";
    public static final String col_6 = "ders_tenefus_suresi";

    private static final String table_2 = "ders_notlari";
    public static final String col_1_2="ders_konusu";
    public static final String col_2_2="ders_notu";
    public static final String col_3_2="not_resmi";
    public static final String col_4_2="ders";
    public static final String col_5_2="tarih";

    private static final String table_3 = "dersler";
    public static final String col_1_3 = "ders_adi";

    SQLiteDatabase db;

    long result;
    long result2;
    long result3;

    public DbHelper(Context context)
    {
        super(context,dbName,null,version);
        db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table + " (id integer primary key autoincrement," + col_1 + " text not null," + col_2 + " text," + col_3 + " text," + col_4 + " text," + col_5 + " int," + col_6 + " text)");
        db.execSQL("create table "+table_2+" (id integer primary key autoincrement,"+col_1_2+" text not null,"+col_2_2+" text not null,"+col_3_2+" blob,"+col_4_2+" text,"+col_5_2+" date)");
        db.execSQL("create table " + table_3 + " (id integer primary key autoincrement," + col_1_3 + " text not null unique)");

    }

    public boolean dersEkle(String dersAdi, String ders_gunu, String dersBaslangicSaati, String dersBitisSaati, int dersPoziyon, String dersTenefusSuresi)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, dersAdi);
        contentValues.put(col_2, ders_gunu);
        contentValues.put(col_3, dersBaslangicSaati);
        contentValues.put(col_4, dersBitisSaati);
        contentValues.put(col_5, dersPoziyon);
        contentValues.put(col_6, dersTenefusSuresi);
        result = db.insert(table, null, contentValues);
        if (result != -1)
            return true;
        else
            return false;
    }

    public Cursor tumDersler()
    {
        db = getWritableDatabase();
        return db.rawQuery("select * from dersler_programi ", null);
    }

    public Cursor dersler()
    {
        db = getWritableDatabase();
        return db.rawQuery("select distinct ders_adi from dersler", null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+table);
        db.execSQL("drop table if exists "+table_2);
        db.execSQL("drop table if exists " + table_3);
        onCreate(db);
    }

    public Integer dersSil(String id)
    {
        db = this.getWritableDatabase();
        return db.delete(table,"id = ?",new String[] {id});
    }

    public boolean dersGuncelle(int dersId, String dersAdi, int dersPozisyon, String dersTenefusSuresi) {
        try {
            db = this.getWritableDatabase();
            String updateQuery = "update '" + table + "' set '" + col_1 + "'='" + dersAdi + "','" + col_5 + "'='" + dersPozisyon + "','" + col_6 + "'='" + dersTenefusSuresi + "' where id='" + dersId + "'";
            db.execSQL(updateQuery);
        }catch (Exception e)
        {

        }
        return true;
    }

    //--------Tablo 2---------//

    public boolean dersNotuGuncelle(int id, String not) {
        db = this.getWritableDatabase();
        String updateQuery = "update '" + table_2 + "' set '" + col_2_2 + "'='" + not + "' where id='" + id + "'";
        db.execSQL(updateQuery);
        return true;
    }

    public boolean dersNotuEkle(String konu, String ders, byte[] notResmi, String dersNotu,String tarih)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_1_2,konu);
        contentValues.put(col_2_2,dersNotu);
        contentValues.put(col_3_2,notResmi);
        contentValues.put(col_4_2,ders);
        contentValues.put(col_5_2,tarih);
        result2=db.insert(table_2,null,contentValues);
        if (result2 != -1)
            return true;
        else
            return false;
    }

    public Cursor dersNotlari(String ders)
    {
        db = this.getWritableDatabase();
        String sql="select * from ders_notlari where ders=?";
        Cursor cursor=db.rawQuery(sql,new String[]{ders});
        return cursor;
    }
    public Cursor dersNotlariTumu()
    {
        db = this.getWritableDatabase();
        String sql="select * from ders_notlari";
        Cursor cursor=db.rawQuery(sql,null);
        return cursor;
    }
    public Cursor dersNotlariSon()
    {
        db = this.getWritableDatabase();
        String sql="select * from ders_notlari order by tarih desc limit 10";
        return db.rawQuery(sql,null);
    }

    public Cursor ResimBul(String id)
    {
        db = this.getWritableDatabase();
        String sql="select not_resmi from ders_notlari where id=?";
        Cursor cursor=db.rawQuery(sql,new String[]{id});
        return cursor;
    }
    public void NotSil(String ders)
    {
        db = this.getWritableDatabase();
        String query="delete from '"+table_2+"' where ders='"+ders+"'";
        db.execSQL(query);
        db.close();

    }

    public void NotSilTekli(int id) {
        db = this.getWritableDatabase();
        String query = "delete from '" + table_2 + "' where id='" + id + "'";
        db.execSQL(query);
        db.close();

    }

    //  ---------------------Dersler Tablosu----------------------------------

    public Integer dersSilTekli(String ders) {
        db = this.getWritableDatabase();
        return db.delete(table_3, "ders_adi = ?", new String[]{ders});
    }


    public boolean dersEkle(String dersAdi) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1_3, dersAdi);
        result3 = db.insert(table_3, null, contentValues);

        System.out.println("ders : "+result3);
        if (result3 != -1)
            return true;
        else
            return false;
    }

    //    ---------------------------------------------------
    private String gun = "";
    public Cursor dersKontrol(String saat) {
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) {
            case 0:
                gun = "Pazar";
                break;
            case 1:
                gun = "Pazartesi";
                break;
            case 2:
                gun = "Salı";
                break;
            case 3:
                gun = "Çarşamba";
                break;
            case 4:
                gun = "Perşembe";
                break;
            case 5:
                gun = "Cuma";
                break;
            case 6:
                gun = "Cumartesi";
                break;
        }
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from dersler_programi where ders_baslangic_saati='" + saat + "' and ders_gunu='" + gun + "'", null);
        return cursor;
    }

    public void dersProgramiSil() {
        db = this.getWritableDatabase();
        String query = "delete from dersler_programi";
        db.execSQL(query);
        db.close();
    }


}
