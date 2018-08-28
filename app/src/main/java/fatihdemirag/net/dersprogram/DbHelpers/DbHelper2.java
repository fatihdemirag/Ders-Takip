///**
// * Created by fxd on 10.06.2017.
// */
//package fatihdemirag.net.dersprogram;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.widget.Toast;
//
//public class DbHelper2 extends SQLiteOpenHelper{
//    public static final String dbName="ders_programi";
//    private static final String table="ders_notlari";
//    private static final int version=3;
//    public static final String col_1="ders_konusu";
//    public static final String col_2="ders_notu";
//    public static final String col_3="not_resmi";
//    public static final String col_4="ders";
//
//    SQLiteDatabase db;
//    DbHelper dbHelper;
//
//
//
//    private void TostMesaj(String tost)
//    {
//        Toast.makeText(null,tost,Toast.LENGTH_SHORT).show();
//    }
//    public DbHelper2(Context context)
//    {
//        super(context,dbName,null,version);
//        db=this.getWritableDatabase();
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        try {
//            db.execSQL("create table "+table+" (id integer primary key autoincrement,"+col_1+" text not null,"+col_2+" text not null,"+col_3+" blob,"+col_4+" text)");
//        }catch (Exception e)
//        {
//            try {
//                TostMesaj("Hata Oluştu");
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//    long result;
//    public boolean insertData(String konu,String ders,byte[] notResmi,String dersNotu)
//    {
//
//        SQLiteDatabase db=this.getWritableDatabase();
//        ContentValues contentValues=new ContentValues();
//        contentValues.put(col_1,konu);
//        contentValues.put(col_2,dersNotu);
//        contentValues.put(col_3,notResmi);
//        contentValues.put(col_4,ders);
//        result=db.insert(table,null,contentValues);
//        if (result==1)
//            return false;
//        else
//            return true;
//    }
//    public Cursor getAllData(String ders)
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        String sql="select * from ders_notlari where ders=?";
//        Cursor cursor=db.rawQuery(sql,new String[]{ders});
//        return cursor;
//
//    }
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists "+table);
//        onCreate(db);
//    }
//    public DbHelper2 open() throws SQLException
//    {
//        db=dbHelper.getReadableDatabase();
//        return  this;
//    }
//    public void close()
//    {
//        dbHelper.close();
//    }
//    public Integer deleteData(String id)
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        return db.delete(table,"id = ?",new String[] {id});
//    }
//    public boolean updateData(String id,String konu,String dersNotu)
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        ContentValues contentValues=new ContentValues();
//        contentValues.put("id",id);
//        contentValues.put(col_1,konu);
//        contentValues.put(col_2,dersNotu);
//        db.update(table,contentValues,id+"=?",new String[]{String.valueOf(id),String.valueOf(konu),String.valueOf(dersNotu)});
//        return true;
//    }
//
//}
