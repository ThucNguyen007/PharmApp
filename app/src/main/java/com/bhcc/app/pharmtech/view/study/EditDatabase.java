package com.bhcc.app.pharmtech.view.study;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Senay tekle on 12/11/2017.
 */

public class EditDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PharmApp DataBase";
    public static final String TABLE_NAME = "MEDICINE_DATA";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "GENERIC_NAME";
    public static final String COL_3 = "BRAND_NAME";
    public static final String COL_4 = "PURPOSE";
    public static final String COL_5 = "DEA_SCH";
    public static final String COL_6 = "SPECIAL";
    public static final String COL_7 = "CATEGORY";
    public static final String COL_8 = "STUDY_TOPIC";


    public EditDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);



    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT ,GENERIC_NAME TEXT," +
                " BRAND_NAME TEXT,PURPOSE TEXT, DEA_SCH TEXT, SPECIAL TEXT,CATEGORY TEXT, STUDY_TOPIC TEXT)");//ID INTEGER WILL AUTO INCREMENT ITSELF UPON INSERTION

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);// don't create a database if it exists
        onCreate(sqLiteDatabase); // create new data base if it doesn't exist.


    }

    public boolean insertToDataBase(String generic, String brand, String purpose, String dea, String special, String catag,
                                    String study){


        ContentValues contentValues = new ContentValues();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        contentValues.put(COL_2, generic);
        contentValues.put(COL_3,brand);
        contentValues.put(COL_4, purpose);
        contentValues.put(COL_5, dea);
        contentValues.put(COL_6,special);
        contentValues.put(COL_7, catag);
        contentValues.put(COL_8, study);


        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor displayData(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor resource = sqLiteDatabase.rawQuery("select * from "+ TABLE_NAME,null);

        return resource;
    }
    public Integer deleteData(String id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,"ID = ?", new String[]{id});

    }
    public boolean updateDataBase(String generic, String brand, String purpose, String dea, String special, String catag,
                                  String study, String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, generic);
        contentValues.put(COL_3,brand);
        contentValues.put(COL_4, purpose);
        contentValues.put(COL_5, dea);
        contentValues.put(COL_6,special);
        contentValues.put(COL_7, catag);
        contentValues.put(COL_8, study);

        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = ?", new String[]{id});

        return true;
    }//END ON UPDATE DATABASE
}
