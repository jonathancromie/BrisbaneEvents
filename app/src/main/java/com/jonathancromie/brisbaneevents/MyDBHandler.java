package com.jonathancromie.brisbaneevents;

/**
 * Created by jonathancromie on 7/01/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "highscores.db";
    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYERNAME = "playername";
    public static final String COLUMN_PLAYERSCORE = "playerscore";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_SCORES + " ( " +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + " , " +
                        COLUMN_PLAYERNAME + " TEXT " + " , " +
                        COLUMN_PLAYERSCORE + " INT " + ");";

        db.execSQL(query);
        //Log.i(TAG, "done");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_SCORES);
        onCreate(db);
    }

    // Add new row to the database
    public void addScore(Scores scores) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYERNAME, scores.getPlayerName());
        values.put(COLUMN_PLAYERSCORE, scores.getScore());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SCORES, null, values);
        db.close();
    }

//    // Delete  product from the database
//    public void deleteProduct(String productName) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\";" );
//    }

//    // Print out database as a string
//    public String databaseToString() {
//        String dbString = "";
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
//
//        Cursor c = db.rawQuery(query, null);
//        c.moveToFirst();
//
//        while (!c.isAfterLast()) {
//            if (c.getString(c.getColumnIndex("productname")) != null) {
//                dbString += c.getString(c.getColumnIndex("productname"));
//                dbString += "\n";
//            }
//        }
//        db.close();
//        return dbString;
//    }
}
