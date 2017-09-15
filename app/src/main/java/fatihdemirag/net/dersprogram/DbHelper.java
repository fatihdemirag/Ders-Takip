/**
 * Created by fxd on 10.06.2017.
 */
package fatihdemirag.net.dersprogram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbHelper extends SQLiteOpenHelper{
    public static final String dbName="ders_programi";
    private static final String table="dersler";
    private static final int version=4;
    public static final String col_1="ders_adi";
    public static final String col_2="ders_gunu";
    public static final String col_3="ders_baslangic_saati";
    public static final String col_4="ders_bitis_saati";

    private static final String table_2="ders_notlari"; ;
    public static final String col_1_2="ders_konusu";
    public static final String col_2_2="ders_notu";
    public static final String col_3_2="not_resmi";
    public static final String col_4_2="ders";

    SQLiteDatabase db;
    DbHelper dbHelper;



    private void TostMesaj(String tost)
    {
        Toast.makeText(null,tost,Toast.LENGTH_SHORT).show();
    }
    public DbHelper(Context context)
    {
        super(context,dbName,null,version);
        db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table "+table+" (id integer primary key autoincrement,"+col_1+" text not null,"+col_2+" text,"+col_3+" text,"+col_4+" text)");
            db.execSQL("create table "+table_2+" (id integer primary key autoincrement,"+col_1_2+" text not null,"+col_2_2+" text not null,"+col_3_2+" blob,"+col_4_2+" text)");

        }catch (Exception e)
        {
            try {
                TostMesaj("Hata Oluştu");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }
    long result;
    public boolean insertData(String dersAdi,String ders_gunu,String dersBaslangicSaati,String dersBitisSaati)
    {

            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(col_1,dersAdi);
            contentValues.put(col_2,ders_gunu);
            contentValues.put(col_3,dersBaslangicSaati);
            contentValues.put(col_4,dersBitisSaati);

            result=db.insert(table,null,contentValues);
            if (result==1)
                return false;
            else
                return true;

    }
    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from dersler order by ders_baslangic_saati",null);
        return cursor;
    }
    public Cursor getAllDataT()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select distinct ders_adi from dersler",null);
        return cursor;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+table);
        db.execSQL("drop table if exists "+table_2);
        onCreate(db);
    }
    public Integer deleteData(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(table,"id = ?",new String[] {id});
    }
    public boolean updateData(int id,String dersAdi,String dersBaslangic,String dersBitis)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //contentValues.put("id",id);
        contentValues.put(col_1,dersAdi);
        contentValues.put(col_2,dersBaslangic);
        contentValues.put(col_2,dersBitis);
        String updateQuery="update '"+table+"' set ders_adi='"+dersAdi+"',ders_baslangic_saati='"+dersBaslangic+"',ders_bitis_saati='"+dersBitis+"' where id='"+id+"'";
        //db.update(table,contentValues,"id"+"=?",new String[]{String.valueOf(id)});
        db.execSQL(updateQuery);
        db.close();
        return true;
    }

    //--------Tablo 2---------//

    public Integer deleteData2(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(table_2,"id = ?",new String[] {id});
    }
    public boolean updateData2(int id,String konu,String dersNotu)
    {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("id",id);
            contentValues.put(col_1_2,konu);
            contentValues.put(col_2_2,dersNotu);
            db.update(table,contentValues,id+"=?",new String[]{String.valueOf(id),String.valueOf(konu),String.valueOf(dersNotu)});
            return true;
    }
    long result2;

    public boolean insertData2(String konu,String ders,byte[] notResmi,String dersNotu)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_1_2,konu);
        contentValues.put(col_2_2,dersNotu);
        contentValues.put(col_3_2,notResmi);
        contentValues.put(col_4_2,ders);
        result2=db.insert(table_2,null,contentValues);
        if (result2==1)
            return false;
        else
            return true;
    }
    public Cursor getAllData2(String ders)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="select * from ders_notlari where ders=?";
        Cursor cursor=db.rawQuery(sql,new String[]{ders});
        return cursor;

    }

    public Cursor ResimBul(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="select not_resmi from ders_notlari where id=?";
        Cursor cursor=db.rawQuery(sql,new String[]{id});
        return cursor;

    }
    public void NotSil(String ders)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="delete from '"+table_2+"' where ders='"+ders+"'";
        db.execSQL(query);
    }

}
