package com.yunuscagliyan.mynotebox;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.yunuscagliyan.mynotebox.data.DatabaseHelper;
import com.yunuscagliyan.mynotebox.data.MyNoteBoxQueryHandler;
import com.yunuscagliyan.mynotebox.data.ToDoBoxContract.NotesEntry;
import com.yunuscagliyan.mynotebox.data.ToDoBoxContract.CategoryEntry;

import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    private static final int All_NOTES=-1;
    private static final int All_CATEGORIES=-1;

    Spinner spinner;
    ListView lvNotes;
    NoteCursorAdapter adapter;
    Cursor cursor;
    Cursor cursorCategory;
    SimpleCursorAdapter categoryCursorAdapter;
    MyNoteBoxQueryHandler queryHandler;

    long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Loader initialized and trigged to onCreateLoader method
        //getLoaderManager().initLoader(100,null, MainActivity.this);
        getLoaderManager().initLoader(150,null,MainActivity.this);



        /*
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        SQLiteDatabase db=databaseHelper.getReadableDatabase();
        */
        //createNote();
        //editNote();
        //deleteNote();
        //readNote();
        //addCategory();
        //showCategory();
        //addNote();
        //updateNote();
        //deleteNotes();

        /*
        addCategory();
        addCategory();
        addCategory();

        showCategory();


        addNote();
        addNote();
        addNote();

        showNotes();
        */
        //deleteCategory(4);
        //showNotes();
        //deleteNotes(4);
        //showCategory();



        spinner=findViewById(R.id.spinner);
        lvNotes=findViewById(R.id.lvNotes);
        cursor=showNotes();


        String[] projection={CategoryEntry._ID,CategoryEntry.COLUMN_CATEGORY};
        queryHandler=new MyNoteBoxQueryHandler(getContentResolver());
        adapter=new NoteCursorAdapter(this,cursor,false);
        queryHandler.startQuery(1,null,CategoryEntry.CONTENT_URI,projection,null,null,CategoryEntry._ID+" DESC");
        categoryCursorAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_dropdown_item_1line,cursorCategory,new String[]{CategoryEntry.COLUMN_CATEGORY},new int[]{android.R.id.text1},0);

        spinner.setAdapter(categoryCursorAdapter);
        lvNotes.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mID=id;
                getLoaderManager().restartLoader(100,null,MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,NoteActivity.class);

                Cursor clickedNote=(Cursor)lvNotes.getItemAtPosition(position);
                intent.putExtra(NotesEntry._ID, clickedNote.getString(0));
                intent.putExtra(NotesEntry.COLUMN_NOTE_CONTENT, clickedNote.getString(1));
                intent.putExtra(NotesEntry.COLUMN_CREATED_DATE, clickedNote.getString(2));
                intent.putExtra(NotesEntry.COLUMN_END_DATE, clickedNote.getString(3));
                intent.putExtra(NotesEntry.COLUMN_CHECK, clickedNote.getString(4));
                intent.putExtra(NotesEntry.COLUMN_CATEGORY_ID, clickedNote.getString(5));
                intent.putExtra(CategoryEntry.COLUMN_CATEGORY, clickedNote.getString(6));
                startActivity(intent);
                CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_categories) {
            Intent intent=new Intent(this,CategoryActivity.class);
            startActivity(intent);
            CustomIntent.customType(this,"fadein-to-fadeout");
            return true;
        }
        switch (id){
            case R.id.action_deleteAllNotes:
                deleteNotes(All_NOTES);
                //showNotes();

                break;
            case R.id.action_deleteAllCategories:
                deleteCategory(All_CATEGORIES);
                //showNotes();
                break;
            case R.id.action_test_category:
                createTestCategories();
                break;
            case R.id.action_test_notes:
                createTestNotes();
                break;
        }

        return true;
    }
    private void createNote(){
        DatabaseHelper helper=new DatabaseHelper(this);
        SQLiteDatabase db=helper.getWritableDatabase();
        String insertQuery="INSERT INTO notes("
                +NotesEntry.COLUMN_NOTE_CONTENT+","
                +NotesEntry.COLUMN_CATEGORY_ID+","
                +NotesEntry.COLUMN_CREATED_DATE+","
                +NotesEntry.COLUMN_END_DATE+","
                +NotesEntry.COLUMN_CHECK+")"
                +"VALUES(\"Spora GIT\",1,\"22-03-2019\",\"\",0)";
        db.execSQL(insertQuery);
        ContentValues newRecord=new ContentValues();
        newRecord.put(NotesEntry.COLUMN_NOTE_CONTENT,"Go to the school");
        newRecord.put(NotesEntry.COLUMN_CATEGORY_ID,1);
        newRecord.put(NotesEntry.COLUMN_CREATED_DATE,"19-03-2019");
        newRecord.put(NotesEntry.COLUMN_CHECK,0);

        long id=db.insert(NotesEntry.TABLE_NAME,null,newRecord);


    }
    private void readNote() {
        DatabaseHelper helper=new DatabaseHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        String[] projection={
                NotesEntry.ID,
                NotesEntry.COLUMN_NOTE_CONTENT,
                NotesEntry.COLUMN_CREATED_DATE,
                NotesEntry.COLUMN_END_DATE,
                NotesEntry.COLUMN_CHECK,
                NotesEntry.COLUMN_CATEGORY_ID
        };
        String selection=NotesEntry.COLUMN_CATEGORY_ID+" =?";
        String[] selectionArgs={"1"};
        Cursor c=db.query(NotesEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        int i=c.getCount();
        Toasty.info(this,"Count:"+i,Toasty.LENGTH_LONG,true).show();

        String allNotes="";
        while (c.moveToNext()){
            for(int j=0;j<=4;j++){
                allNotes+=c.getString(j)+"-";
            }
            allNotes+="\n";
        }
        Log.e("Result",allNotes);
        c.close();
        db.close();
    }
    private void editNote(){
        DatabaseHelper helper=new DatabaseHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        ContentValues updatedValues= new ContentValues();
        updatedValues.put(NotesEntry.COLUMN_NOTE_CONTENT,"NEW UPDATED VALUE");
        String[] args={"3"};

        int updatedRowCount=db.update(NotesEntry.TABLE_NAME,updatedValues,NotesEntry.ID+"=?",args);
        Toasty.error(this,"Updated Row count:"+updatedRowCount,Toasty.LENGTH_LONG,true).show();
        db.close();
        helper.close();
    }
    private void deleteNote(){
        DatabaseHelper helper=new DatabaseHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        for (int i=5;i<10;i++){
            String[] args={String.valueOf(i)};
            db.delete(NotesEntry.TABLE_NAME,NotesEntry.ID+"=?",args);
        }
        //Toasty.error(this,"Updated Row count:"+updatedRowCount,Toasty.LENGTH_LONG,true).show();

        db.close();
        helper.close();
    }

    private void addCategory() {
        ContentValues values=new ContentValues();
        values.put(CategoryEntry.COLUMN_CATEGORY,"Trial Category");

        Uri uri=getContentResolver().insert(CategoryEntry.CONTENT_URI,values);
        Toasty.info(this,"was added:"+uri,Toasty.LENGTH_LONG,true).show();
    }
    private void showCategory() {
        String[] projection={"_id","category"};
        Cursor cursor=getContentResolver().query(CategoryEntry.CONTENT_URI,projection,null,null,null,null);
        String allCategory="";
        while(cursor.moveToNext()){
            String id=cursor.getString(0);
            String category=cursor.getString(1);

            allCategory+="id:"+id+" category:"+category+"\n";
        }
        Toasty.info(this,allCategory,Toasty.LENGTH_LONG,true).show();
        Log.d(TAG,allCategory);
    }
    private void addNote() {
        ContentValues newRecord=new ContentValues();
        newRecord.put(NotesEntry.COLUMN_NOTE_CONTENT,"New Record");
        newRecord.put(NotesEntry.COLUMN_CATEGORY_ID,2);
        newRecord.put(NotesEntry.COLUMN_CREATED_DATE,"16-04-2019");
        newRecord.put(NotesEntry.COLUMN_CHECK,0);
        Uri uri=getContentResolver().insert(NotesEntry.CONTENT_URI,newRecord);
        Toasty.success(this,"Note was added Uri:"+uri,Toasty.LENGTH_LONG,true).show();
    }
    private Cursor showNotes(){
        String[] projection={"notes._id","notes.note","categories._id","categories.category"};
        Cursor cursor=getContentResolver().query(NotesEntry.CONTENT_URI,projection,null,null,null);
       /*
        String allNotes="";
        while(cursor.moveToNext()){
            for (int i=0;i<=3;i++){
                allNotes+=cursor.getString(i)+"-";
            }
            allNotes+="\n";
        }
        Toasty.info(this,allNotes,Toasty.LENGTH_LONG,true).show();
        Log.d(TAG,allNotes);
        */
       return cursor;

    }
    private void updateNote() {
        ContentValues values=new ContentValues();
        values.put(NotesEntry.COLUMN_NOTE_CONTENT,"updated new value");
        int id=getContentResolver().update(NotesEntry.CONTENT_URI,values,"_id=?",new String[]{"6"});
        Toasty.success(this,"Record is updated:"+id,Toasty.LENGTH_LONG,true).show();
    }
    private void deleteNotes(int deletedId){
        MyNoteBoxQueryHandler handler=new MyNoteBoxQueryHandler(this.getContentResolver());
        String selection="_id=?";
        String[] args={String.valueOf(deletedId)};
        if(deletedId==All_NOTES){
            handler.startDelete(1,null,NotesEntry.CONTENT_URI,null,null);
        }
        //int id=getContentResolver().delete(NotesEntry.CONTENT_URI,selection,args);

        handler.startDelete(1,null,NotesEntry.CONTENT_URI,selection,args);

        Toasty.success(this,"Record is deleted:",Toasty.LENGTH_LONG,true).show();


    }
    private void deleteCategory(int deletedId){
        String selection="_id=?";
        String[] args={String.valueOf(deletedId)};
        if(deletedId==All_CATEGORIES){
            args=null;
            selection=null;
        }
        int id=getContentResolver().delete(CategoryEntry.CONTENT_URI,selection,args);
        Toasty.success(this,"Record is deleted:"+id,Toasty.LENGTH_LONG,true).show();


    }
    private void createTestCategories(){
        for(int i=1;i<=20;i++){
            ContentValues values=new ContentValues();
            values.put(CategoryEntry.COLUMN_CATEGORY,"Trial Category #"+i);
            //Uri uri=getContentResolver().insert(ToDoBoxContract.CategoryEntry.CONTENT_URI,values);
            MyNoteBoxQueryHandler handler=new MyNoteBoxQueryHandler(this.getContentResolver());
            handler.startInsert(1,null, CategoryEntry.CONTENT_URI,values);

            Toasty.info(this,"was added:",Toasty.LENGTH_LONG,true).show();



        }
        //showCategory();

    }
    private void createTestNotes(){

        for(int i=1;i<=100;i++){
            ContentValues newRecord=new ContentValues();
            newRecord.put(NotesEntry.COLUMN_NOTE_CONTENT,"New Record #"+i);
            newRecord.put(NotesEntry.COLUMN_CATEGORY_ID,(i%4));
            newRecord.put(NotesEntry.COLUMN_CREATED_DATE,"16-04-2019");
            newRecord.put(NotesEntry.COLUMN_END_DATE,"20-04.2019");
            newRecord.put(NotesEntry.COLUMN_CHECK,(i%2==0) ? 1:0);

            //Uri uri=getContentResolver().insert(NotesEntry.CONTENT_URI,newRecord);
            MyNoteBoxQueryHandler handler=new MyNoteBoxQueryHandler(this.getContentResolver());
            handler.startInsert(1,null,NotesEntry.CONTENT_URI,newRecord);
        }
        //showCategory();
        Toasty.success(this,"Note was added Uri:",Toasty.LENGTH_LONG,true).show();


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        if(id==100){
            String selection=NotesEntry.COLUMN_CATEGORY_ID+"=?";
            String[] selectionArgs={String.valueOf(mID)};
            String[] projection = {NotesEntry.TABLE_NAME + "." + NotesEntry._ID,
                    NotesEntry.COLUMN_NOTE_CONTENT, NotesEntry.COLUMN_CREATED_DATE,
                    NotesEntry.COLUMN_END_DATE, NotesEntry.COLUMN_CHECK,
                    NotesEntry.COLUMN_CATEGORY_ID, CategoryEntry.COLUMN_CATEGORY};
            return new CursorLoader(this,NotesEntry.CONTENT_URI,projection,selection,selectionArgs,null);
        }
        if(id==150){
            String[] projection={CategoryEntry._ID,CategoryEntry.COLUMN_CATEGORY};
            return new CursorLoader(this,CategoryEntry.CONTENT_URI,projection,null,null,null);

        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId()==100){
            adapter.swapCursor(data);
        }if(loader.getId()==150){
            categoryCursorAdapter.swapCursor(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
        categoryCursorAdapter.swapCursor(null);

    }
}
