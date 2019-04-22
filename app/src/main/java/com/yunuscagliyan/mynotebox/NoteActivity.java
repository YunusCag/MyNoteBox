package com.yunuscagliyan.mynotebox;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.yunuscagliyan.mynotebox.data.MyNoteBoxQueryHandler;
import com.yunuscagliyan.mynotebox.data.ToDoBoxContract.NotesEntry;

import com.yunuscagliyan.mynotebox.data.ToDoBoxContract.CategoryEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;

public class NoteActivity extends AppCompatActivity {

    Context mContext;
    EditText etNote;
    EditText etCreatedDate;
    EditText etEndDate;

    Button btnDelete;
    FloatingActionButton fab;

    CheckBox cbDone;
    Spinner spinner;
    SimpleCursorAdapter categoryAdapter;
    Cursor categoryCursor;
    String isNew;
    String noteID,noteContent,noteCreated, noteDone,noteEnd,noteCategoryID,noteCategory;

    MyNoteBoxQueryHandler queryHandler;

    int mYear;
    int mMonth;
    int mDay;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mContext=NoteActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        String[] projection={CategoryEntry.ID,CategoryEntry.COLUMN_CATEGORY};

        categoryCursor=getContentResolver().query(CategoryEntry.CONTENT_URI,projection,null,null,NotesEntry._ID+" ASC");
        categoryAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_dropdown_item_1line,categoryCursor
                ,new String[]{CategoryEntry.COLUMN_CATEGORY},new int[]{android.R.id.text1},0);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        etNote=findViewById(R.id.etNote);
        etCreatedDate=findViewById(R.id.txtDate);
        etEndDate=findViewById(R.id.txtDateFinish);

        btnDelete=findViewById(R.id.btnDeleteNote);

        cbDone=findViewById(R.id.checkBox);

        spinner=findViewById(R.id.spinnerCat);
        spinner.setAdapter(categoryAdapter);

        Intent receivedNote=getIntent();
        isNew=receivedNote.getStringExtra(NotesEntry._ID);

        if(isNew!=null){
            // update the note which is exists
            noteID=receivedNote.getStringExtra(NotesEntry._ID);
            noteContent=receivedNote.getStringExtra(NotesEntry.COLUMN_NOTE_CONTENT);
            noteCreated=receivedNote.getStringExtra(NotesEntry.COLUMN_CREATED_DATE);
            noteEnd =receivedNote.getStringExtra(NotesEntry.COLUMN_END_DATE);
            noteDone=receivedNote.getStringExtra(NotesEntry.COLUMN_CHECK);
            noteCategoryID=receivedNote.getStringExtra(NotesEntry.COLUMN_CATEGORY_ID);
            Log.e("NoteActivity","CategoryID:"+noteCategoryID);
            noteCategory=receivedNote.getStringExtra(CategoryEntry.COLUMN_CATEGORY);


            etNote.setText(noteContent);
            etCreatedDate.setText(noteCreated);
            etEndDate.setText(noteEnd);
            if(noteCategoryID.equals("0")){
                spinner.setSelection(Integer.parseInt(noteCategoryID));
            }else {
                spinner.setSelection(Integer.parseInt(noteCategoryID)-1);
            }

            if(noteDone.equals("1")){
                cbDone.setChecked(true);
            }else{
                cbDone.setChecked(false);
            }


        }else{
            //create a new note process
            etNote.setHint("add new Note");

        }
        queryHandler=new MyNoteBoxQueryHandler(mContext.getContentResolver());
        fab=findViewById(R.id.fabSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteContent=etNote.getText().toString();
                String startDate=etCreatedDate.getText().toString();
                String endDate=etEndDate.getText().toString();
                String check;
                if (cbDone.isSelected()){
                    check="1";
                }else {
                    check="0";
                }
                ContentValues newRecord=new ContentValues();
                newRecord.put(NotesEntry.COLUMN_NOTE_CONTENT,noteContent);
                Log.e("NoteActivity","Spinner ID:"+spinner.getSelectedItemId()+"item:"+spinner.getSelectedItem().toString());
                newRecord.put(NotesEntry.COLUMN_CATEGORY_ID,spinner.getSelectedItemId());
                newRecord.put(NotesEntry.COLUMN_CREATED_DATE,startDate);
                newRecord.put(NotesEntry.COLUMN_END_DATE,endDate);
                newRecord.put(NotesEntry.COLUMN_CHECK,check);
                if(isNew==null){
                    //adding new Note
                    queryHandler.startInsert(1,null,NotesEntry.CONTENT_URI,newRecord);
                    Log.e("NoteActivity","insert");
                    Toasty.success(mContext,"Note was inserted",Toasty.LENGTH_LONG,true).show();

                }else{
                    String selection=NotesEntry._ID+"=?";
                    String[] args={noteID};
                    //update the note which is exist in database
                    queryHandler.startUpdate(1,null,NotesEntry.CONTENT_URI,newRecord,selection,args);
                    Log.e("NoteActivity","update");
                    Toasty.info(mContext,"Note was updated",Toasty.LENGTH_LONG,true).show();
                }

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"fadein-to-fadeout");
    }

    public void selectStartDate(View view) {
        showCalender(view);

    }

    public void selectEndDate(View view) {
        showCalender(view);

    }

    public void deleteNote(View view) {
        String selection=NotesEntry._ID+"=?";
        String[] args={noteID};
        queryHandler.startDelete(1,null,NotesEntry.CONTENT_URI,selection,args);
        etNote.setText("");
        etEndDate.setText("");
        etCreatedDate.setText("");
        Toasty.info(mContext,"Note was deleted",Toasty.LENGTH_LONG,true).show();

    }
    private void showCalender(final View mView){
        calendar=Calendar.getInstance();
        mYear=calendar.get(Calendar.YEAR);
        mMonth =calendar.get(Calendar.MONTH);
        mDay =calendar.get(Calendar.DAY_OF_MONTH);
        int cYear=mYear;
        int cMonth=mMonth;
        int cDat=mDay;

        DatePickerDialog calenderDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM YYYY",new Locale("tr"));
                if(mYear<=year&&mMonth<=month){
                    if(mDay<=dayOfMonth){
                        etCreatedDate.setText(dayOfMonth +"/"+month+"/"+year);
                        if(mView.getId()==R.id.btnEndDate){
                                etEndDate.setText(dayOfMonth+"/"+month+"/"+year);
                        }

                    }else{
                        Toasty.error(mContext,"You can not select the past date",Toasty.LENGTH_LONG,true).show();
                    }

                }else {
                    Toasty.error(mContext,"You can not select the past date",Toasty.LENGTH_LONG,true).show();
                }

            }
        },mYear, mMonth, mDay);
        calenderDialog.show();
    }
}
