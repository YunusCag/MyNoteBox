package com.yunuscagliyan.mynotebox.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MyNoteBoxProvider extends ContentProvider {
    private static final String TAG = "MyNoteBoxProvider";

    private static final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    private static final int URICODE_NOTES=1;
    private static final int URICODE_CATEGORIES=2;
    static {
        matcher.addURI(ToDoBoxContract.CONTENT_AUTHORITY,ToDoBoxContract.PATH_NOTES,URICODE_NOTES);
        matcher.addURI(ToDoBoxContract.CONTENT_AUTHORITY,ToDoBoxContract.PATH_CATEGORIES,URICODE_CATEGORIES);

    }
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    @Override
    public boolean onCreate() {
        helper=new DatabaseHelper(getContext());
        db=helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;
        SQLiteQueryBuilder builder;
        String combineTables="notes inner join categories on notes.categoryID=categories._id";

        switch (matcher.match(uri)){
            case URICODE_NOTES:
                builder=new SQLiteQueryBuilder();
                builder.setTables(combineTables);
                cursor=builder.query(db,projection,selection,selectionArgs,null,null,null);
                break;


            case URICODE_CATEGORIES:
                cursor=db.query(ToDoBoxContract.CategoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
                default:
                    new IllegalArgumentException("UNKNOWN QUERY URI:"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Override
    public String getType( Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri, ContentValues values) {
        switch (matcher.match(uri)){
            case URICODE_NOTES:
                return addRecord(uri,values,ToDoBoxContract.NotesEntry.TABLE_NAME);
            case URICODE_CATEGORIES:
                return addRecord(uri,values,ToDoBoxContract.CategoryEntry.TABLE_NAME);

                default:
                    throw new IllegalArgumentException("UNKNOWN INSERT URI"+uri);
        }
    }

    @Override
    public int delete( Uri uri,String selection,String[] selectionArgs) {
        switch (matcher.match(uri)){
            case URICODE_NOTES:
                return deleteRecord(uri,selection,selectionArgs,ToDoBoxContract.NotesEntry.TABLE_NAME);
            case URICODE_CATEGORIES:
                return deleteRecord(uri,selection,selectionArgs,ToDoBoxContract.CategoryEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("UNKNOWN DELETE URI"+uri);
        }
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)){
            case URICODE_NOTES:
                return updateRecord(uri,values,selection,selectionArgs,ToDoBoxContract.NotesEntry.TABLE_NAME);
            case URICODE_CATEGORIES:
                return updateRecord(uri,values,selection,selectionArgs,ToDoBoxContract.CategoryEntry.TABLE_NAME);
                default:
                    throw new IllegalArgumentException("UNKNOWN UPDATE URI"+uri);
        }

    }
    private Uri addRecord(Uri uri,ContentValues values,String tableName){
        long id=db.insert(tableName,null,values);
        if(id==-1){
            Log.e(TAG,"INSERT ERROR");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);

    }
    private int updateRecord(Uri uri,ContentValues values, String selection, String[] selectionArgs,String tableName){
       int id= db.update(tableName,values,selection,selectionArgs);
       if(id==0){
           Log.e(TAG,"Update Error"+uri);
           return -1;
       }
        getContext().getContentResolver().notifyChange(uri,null);
       return id;

    }
    private int deleteRecord(Uri uri, String selection, String[] selectionArgs,String tableName){
        int id= db.delete(tableName,selection,selectionArgs);
        if(id==0){
            Log.e(TAG,"Update Error"+uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return id;

    }
}
