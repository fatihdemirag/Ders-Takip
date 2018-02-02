package fatihdemirag.net.dersprogram.DbHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by fxd on 02.02.2018.
 */

public class DbHelperLiseNotlar extends SQLiteOpenHelper {
    public static final String dbName = "ders_programi";
    private static final String table = "notlar";
    private static final int version = 4;
    public static final String col_1 = "ders_adi";
    public static final String col_2 = "ders_notu";


    SQLiteDatabase db;
    DbHelperLiseNotlar dbHelper;

    private void TostMesaj(String tost) {
        Toast.makeText(null, tost, Toast.LENGTH_SHORT).show();
    }

    public DbHelperLiseNotlar(Context context) {
        super(context, dbName, null, version);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + table + " (id integer primary key autoincrement," + col_1 + " text not null," + col_2 + ")");

        } catch (Exception e) {
            try {
                TostMesaj("Hata Oluştu");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    long result;

    public boolean insertData(String dersAdi, String dersNotu) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, dersAdi);
        contentValues.put(col_2, dersNotu);

        result = db.insert(table, null, contentValues);
        if (result == 1)
            return false;
        else
            return true;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from notlar order by ders_adi", null);
        return cursor;
    }

    public Cursor getAllDataT() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select distinct ders_adi from notlar", null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + table);
        onCreate(db);
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, "id = ?", new String[]{id});
    }

    public boolean updateData(int id, String dersNotu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put("id",id);
        contentValues.put(col_2, dersNotu);
        String updateQuery = "update '" + table + "' set ders_notu='" + dersNotu + "' where id='" + id + "'";
        //db.update(table,contentValues,"id"+"=?",new String[]{String.valueOf(id)});
        db.execSQL(updateQuery);
        db.close();
        return true;
    }
}

