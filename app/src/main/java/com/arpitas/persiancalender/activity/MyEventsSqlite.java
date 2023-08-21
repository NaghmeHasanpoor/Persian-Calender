package com.arpitas.persiancalender.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyEventsSqlite {
    SQLiteDatabase db;
    public static final String DATABASE_TABLE = "myEvents";
    private static final String DATABSE_NAME = "MyEvents.db";
    static final String KEY_ROWID="id";
    static final String KEY_NAME="event";
    static final String KEY_DATE="date";

    private String[] MAHSUL_TABLE_COLUMNS = {KEY_DATE,KEY_NAME};
    static final String DATABASE_CREATE= "create table " + DATABASE_TABLE
            + "(" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null, "+KEY_DATE+" text not null);";
    DatabaseHelper dbhelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context,DATABSE_NAME,null,1);
        }
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DATABASE_CREATE);
        }
        public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
    public MyEventsSqlite(Context ctx){

        dbhelper=new DatabaseHelper(ctx);
    }
    public MyEventsSqlite open(){
        db=dbhelper.getWritableDatabase();
        return this;
    }
    public void close(){
        dbhelper.close();
    }
    public Mahsul insert(String day,String name){

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, day);
        values.put(KEY_NAME, name);
        long studId = db.insert(DATABASE_TABLE, null, values);
        Cursor cursor = db.query(DATABASE_TABLE,
                MAHSUL_TABLE_COLUMNS, KEY_ROWID + " = "
                        + studId, null, null, null, null);
        cursor.moveToFirst();
        Mahsul newComment = parseMahsul(cursor);
        cursor.close();
        return newComment;
    }
    public void deleteMAhsul(String name) {
        db.delete(DATABASE_TABLE, KEY_NAME + " like '" + name + "'", null);
    }

    private Mahsul parseMahsul(Cursor cursor) {
        Mahsul mahsul = new Mahsul();
        mahsul.setId((cursor.getInt(0)));
        mahsul.setName(cursor.getString(1));
        return mahsul;
    }
    public void update(String name,String string)
    {
        ContentValues values=new ContentValues();
        values.put(KEY_NAME, string);
        System.out.println("Comment updated with id: " + name);
        db.update(DATABASE_TABLE, values, KEY_NAME
                + " like '" + name + "'", null);
    }
    public List getEvent(String date)
    {
        List Tasks = new ArrayList();
        Cursor cursor= db.query(DATABASE_TABLE, MAHSUL_TABLE_COLUMNS, KEY_DATE +" like '"+date+"'", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Mahsul tsk = parseMahsul(cursor);
            Tasks.add(tsk);
            cursor.moveToNext();
        }
        cursor.close();
        return Tasks;
    }
    public class Mahsul  {
        private int id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }
}
