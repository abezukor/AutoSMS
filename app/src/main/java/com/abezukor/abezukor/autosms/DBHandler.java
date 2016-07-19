package com.abezukor.abezukor.autosms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

/**
 * Created by abezu on 7/19/2016.
 */
public class DBHandler extends SQLiteOpenHelper {
    //sets all the basic values
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "autosms.db";
    private static final String TABLE_AUTOSMSTABLE = "autosmstable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HOMESCREENNAME = "homeScreenName";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_MESSAGE = "message";

    //Sql constructor
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //first time the code is run
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createquery = "CREATE TABLE " + TABLE_AUTOSMSTABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_HOMESCREENNAME + " TEXT ," + COLUMN_NUMBER + " INTEGER ," + COLUMN_MESSAGE + " TEXT " +
                ");";
        db.execSQL(createquery);
    }

    //whenever the database is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTOSMSTABLE);
        onCreate(db);

    }
    //add new row / automs message
    public void addautosms (autosmsObject autosmsObject){
        //sets up the list
        ContentValues values = new ContentValues();

        //adds values to the list
        values.put(COLUMN_HOMESCREENNAME , autosmsObject.get_homescreenname());
        values.put(COLUMN_NUMBER, autosmsObject.get_number());
        values.put(COLUMN_MESSAGE, autosmsObject.get_message());

        //gets the databse and adds row
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_AUTOSMSTABLE, null, values);
        db.close();
    }

    //delete autosms
    public void delete (int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AUTOSMSTABLE + "WHERE " + COLUMN_ID + "=\"" + id + "\";");
        db.close();
    }
    //gets the total number of rows in the database
    public int getnumberofrows(){
        SQLiteDatabase db = getWritableDatabase();
        return ((int)DatabaseUtils.queryNumEntries(db, TABLE_AUTOSMSTABLE));
    }
    //returns all data
    public String[][] getAllData(){
        //gets the database
        SQLiteDatabase db = getWritableDatabase();
        //sets up query
        String query = "SELECT * FROM " + TABLE_AUTOSMSTABLE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        int rownumber = 0;
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_AUTOSMSTABLE);
        System.out.println(numRows);
        //get sting 2d arrayi
        String[][] results = new String[((int)numRows+1)][3];
        while (!c.isAfterLast()){
            results[rownumber][0] = c.getString(c.getColumnIndex(COLUMN_HOMESCREENNAME));
            results[rownumber][1] = c.getString(c.getColumnIndex(COLUMN_NUMBER));
            results[rownumber][2] = c.getString(c.getColumnIndex(COLUMN_MESSAGE));
            c.moveToNext();
            rownumber+=1;
        }
        db.close();
        System.out.println(results);
        return results;
    }
    //gets the message and number from an id
    public String[] getMessageAndNumberFromId(int id){

        String[] results = {"number", "message"};

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_HOMESCREENNAME, COLUMN_NUMBER, COLUMN_MESSAGE};
        //actually retrives the data
        Cursor c = db.query(TABLE_AUTOSMSTABLE, columns, "_id=" + (id+1), null, null,null, null);
        //moves to the first part of the dataset
        c.moveToFirst();
        //checks i result is null
        if( c != null && c.moveToFirst()) {
            results[0] = Integer.toString(c.getInt(c.getColumnIndex(COLUMN_NUMBER)));
            results[1] = c.getString(c.getColumnIndex(COLUMN_MESSAGE));
            db.close();
            System.out.println("if loop runs");
        }

        return results;
    }
}
