package com.example.morten.fiskebanken.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.morten.fiskebanken.utility.Fisk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morten on 24.02.2016.
 */
public class FishDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allFishColumns = {
            FishTable.COLUMN_ID,
            FishTable.COLUMN_TYPE,
            FishTable.COLUMN_VEKT,
            FishTable.COLUMN_LENGDE,
            FishTable.COLUMN_BILDE,
            FishTable.COLUMN_LAT,
            FishTable.COLUMN_LONG
    };

    public FishDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public Fisk createFisk(String type, double vekt, double lengde, String bilde, double lat, double lng){
        ContentValues values = new ContentValues();
        values.put(FishTable.COLUMN_TYPE, type);
        values.put(FishTable.COLUMN_VEKT, vekt);
        values.put(FishTable.COLUMN_LENGDE, lengde);
        values.put(FishTable.COLUMN_BILDE, bilde);
        values.put(FishTable.COLUMN_LAT, lat);
        values.put(FishTable.COLUMN_LONG, lng);

        long insertId = database.insert(FishTable.TABLE_FISH, null, values);

        return getFisk(insertId);
    }

    public static Fisk cursorToFisk(Cursor cursor){
        Fisk fisk = new Fisk();
        fisk.setId(cursor.getInt(0));
        fisk.setType(cursor.getString(1));
        fisk.setVekt(cursor.getDouble(2));
        fisk.setLengde(cursor.getDouble(3));
        fisk.setBilde(cursor.getString(4));
        fisk.setLat(cursor.getDouble(5));
        fisk.setLng(cursor.getDouble(6));

        return fisk;
    }

    private Fisk getFisk(long id){
        Cursor cursor = database.query(FishTable.TABLE_FISH,
                allFishColumns,
                FishTable.COLUMN_ID + " = " + id,
                null, null, null, null);

        cursor.moveToFirst();
        Fisk fisk = cursorToFisk(cursor);
        cursor.close();

        return fisk;
    }

    public List<Fisk> getAllFisk(){
        List<Fisk> fisker = new ArrayList<>();

        Cursor cursor = database.query(FishTable.TABLE_FISH,
                allFishColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Fisk fisk = cursorToFisk(cursor);
            fisker.add(fisk);
            cursor.moveToNext();
        }

        cursor.close();

        return fisker;
    }

    public void deleteFisk(Fisk fisk){
        int id = fisk.getId();

        database.delete(FishTable.TABLE_FISH, FishTable.COLUMN_ID + " = " + id, null);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }
}