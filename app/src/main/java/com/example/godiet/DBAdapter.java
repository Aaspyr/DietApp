package com.example.godiet;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.util.Log;
import android.content.ContentValues;

public class DBAdapter {
    /*------------*/
    private static final String databaseName = "dietapp";
    private static final int databaseVersion = 1;

    /*------------*/
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /*------------*/
    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    /*DatabaseHelper------------*/

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            try {
                //Create tables
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){

            //db.execSQL("Drop table if exists" + databaseTableNotes);
            onCreate(db);

            String TAG = "Tag";
            Log.w(TAG, "Upgrade db form version" + oldVersion + "to" + newVersion + " which will destrou all old data");
        }
    }

    /* Open database---------*/

    public DBAdapter open() throws SQLException{
        db= DBHelper.getReadableDatabase();
        return this;
    }

    /* close database---------*/

    public void close() {
      DBHelper.close();
    }
}
