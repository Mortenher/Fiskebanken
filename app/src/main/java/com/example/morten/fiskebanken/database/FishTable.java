package com.example.morten.fiskebanken.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Morten on 24.02.2016.
 */
public class FishTable {

    public static final String TABLE_FISH = "fish";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_VEKT = "vekt";
    public static final String COLUMN_LENGDE = "lengde";
    public static final String COLUMN_BILDE = "bilde";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LONG = "long";

    public static final String DATABASE_CREATE_FISH =
            "create table " + TABLE_FISH + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TYPE + " TEXT NOT NULL, " +
                    COLUMN_VEKT + " REAL, " +
                    COLUMN_LENGDE + " REAL, " +
                    COLUMN_BILDE + " TEXT, " +
                    COLUMN_LAT + " REAL, " +
                    COLUMN_LONG + " REAL );";

    public static void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE_FISH);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_FISH);
        onCreate(database);
    }


}
