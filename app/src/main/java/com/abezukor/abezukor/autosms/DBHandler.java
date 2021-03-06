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
    private static final int DATABASE_VERSION = 5;
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
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_HOMESCREENNAME + " TEXT ," + COLUMN_NUMBER + " TEXT ," + COLUMN_MESSAGE + " TEXT " +
                ");";
        db.execSQL(createquery);
    }

    //whenever the database is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //autoSMSObject[] autoSMSes = this.getAllData(db);//gets all the previous data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTOSMSTABLE);//Deletes the old table
        onCreate(db);// Makes a new Table
        /*for(autoSMSObject autoSMS: autoSMSes){// Re-adds all the data
            this.addautosms(autoSMS);
        }*/

    }
    //add new row / automs message
    public int addautosms (AutoSMSObject autosmsObject){
        //sets up the list
        ContentValues values = new ContentValues();

        //adds values to the list
        values.put(COLUMN_HOMESCREENNAME , autosmsObject.get_homescreenname());
        values.put(COLUMN_NUMBER, autosmsObject.get_number());
        values.put(COLUMN_MESSAGE, autosmsObject.get_message());

        //gets the databse and adds row
        SQLiteDatabase db = getWritableDatabase();
        int toReturn = (int) db.insert(TABLE_AUTOSMSTABLE, null, values);
        db.close();
        return(toReturn);
    }
    public void modifyautosmssms (int id, String number, String message){
        //sets up the list
        ContentValues values = new ContentValues();

        //adds values to the list
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_MESSAGE, message);

        //gets the databse and adds row
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_AUTOSMSTABLE,values, "_id=" + (id),null);
        db.close();
    }

    //delete autosms
    public void delete (int id){
        //System.out.println("DB Handler delete Id is " + id);
        SQLiteDatabase db = getWritableDatabase();
        //int deleted = db.delete(TABLE_AUTOSMSTABLE, COLUMN_ID +  "=" + (String.valueOf(id)), null);
        //System.out.println("Deleted = " + deleted);
        db.execSQL("DELETE FROM " + TABLE_AUTOSMSTABLE + " WHERE " + COLUMN_ID + "=\"" + (id) + "\";");
        db.close();
    }
    //gets the total number of rows in the database
    public int getnumberofrows(){
        SQLiteDatabase db = getWritableDatabase();
        return ((int)DatabaseUtils.queryNumEntries(db, TABLE_AUTOSMSTABLE));
    }
    //Not used anymore
    /*public int getNextID(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT LAST_INSERT_ID()";
            Cursor c = db.rawQuery(query, null);
            c.moveToLast();

            //System.out.println("DB Handeler New ID should be " + (c.getInt(c.getColumnIndex(COLUMN_ID)) + 1));

            return ((c.getInt(c.getColumnIndex(COLUMN_ID)) + 1));
        }catch (Exception e){
            return 1;
        }
    }*/
    //returns all data
    public AutoSMSObject[] getAllData(SQLiteDatabase db){
        //gets the database
        if(db==null) {
            db = getWritableDatabase();
        }
        //sets up query
        String query = "SELECT "+COLUMN_ID+","+ COLUMN_HOMESCREENNAME+"," + COLUMN_NUMBER+"," + COLUMN_MESSAGE +  " FROM " + TABLE_AUTOSMSTABLE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        //String[] columns = {COLUMN_ID, COLUMN_HOMESCREENNAME, COLUMN_NUMBER, COLUMN_MESSAGE};
        //Cursor c = db.query(TABLE_AUTOSMSTABLE, columns,null,null,null,null,null);
        c.moveToFirst();

        int rownumber = 0;
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_AUTOSMSTABLE);

        //System.out.println("Number of Rows = " + numRows);
        //System.out.println("Cursor is: " + c);

        //get sting 2d arrayi
        AutoSMSObject[] results = new AutoSMSObject[(int)numRows+1];

        while (!c.isAfterLast()){
            AutoSMSObject row = new AutoSMSObject(c.getString(c.getColumnIndex(COLUMN_HOMESCREENNAME)), c.getString(c.getColumnIndex(COLUMN_NUMBER)),
                    c.getString(c.getColumnIndex(COLUMN_MESSAGE)), c.getInt(c.getColumnIndex(COLUMN_ID)));

            //System.out.println("Cursor is: " + c);
            //System.out.println("getAll id is " + c.getInt(c.getColumnIndex(COLUMN_ID)));

            results[rownumber] = row;
            c.moveToNext();
            rownumber++;
        }
        db.close();
        //System.out.println("Results array  is " + results);
        return results;
    }
    //gets the message and number from an id
    public String[] getMessageAndNumberFromId(int id){

        //System.out.println("Get single id is " + (id));

        String[] results = {"defaultnumber", "defaultmessage", "defaultname"};

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_HOMESCREENNAME, COLUMN_NUMBER, COLUMN_MESSAGE};
        //actually retrives the data
        Cursor c = db.query(TABLE_AUTOSMSTABLE, columns, "_id=" + (id), null, null,null, null);
        //moves to the first part of the dataset
        c.moveToFirst();
        //checks i result is null
        if( c != null && c.moveToFirst()) {
            results[0] = c.getString(c.getColumnIndex(COLUMN_NUMBER));
            results[1] = c.getString(c.getColumnIndex(COLUMN_MESSAGE));
            results[2] = c.getString(c.getColumnIndex(COLUMN_HOMESCREENNAME));
            db.close();
            //System.out.println("Data base returns Values");
        }

        return results;
    }
}
