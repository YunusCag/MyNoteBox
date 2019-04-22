package com.yunuscagliyan.mynotebox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.yunuscagliyan.mynotebox.data.ToDoBoxContract.CategoryEntry;
import com.yunuscagliyan.mynotebox.data.ToDoBoxContract.NotesEntry;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="todobox.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_CATEGORIES_CREATE=
            "CREATE TABLE "+CategoryEntry.TABLE_NAME+" ("+CategoryEntry.ID
            +" INTEGER PRIMARY KEY,"+CategoryEntry.COLUMN_CATEGORY+" TEXT)";
    private static final String TABLE_NOTES_CREATE=
            "CREATE TABLE "+ NotesEntry.TABLE_NAME + " ("+
                    NotesEntry._ID + " INTEGER PRIMARY KEY, "+
                    NotesEntry.COLUMN_NOTE_CONTENT+ " TEXT, " +
                    NotesEntry.COLUMN_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, "+
                    NotesEntry.COLUMN_END_DATE +  " TEXT,"+
                    NotesEntry.COLUMN_CHECK + " INTEGER," +
                    NotesEntry.COLUMN_CATEGORY_ID + " INTEGER,"+
                    " FOREIGN KEY ("+ NotesEntry.COLUMN_CATEGORY_ID + ")" + " REFERENCES "+ CategoryEntry.TABLE_NAME +
                    "("+ CategoryEntry._ID + ") " + ")";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CATEGORIES_CREATE);
        db.execSQL(TABLE_NOTES_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+NotesEntry.TABLE_NAME);
        onCreate(db);

    }
}
