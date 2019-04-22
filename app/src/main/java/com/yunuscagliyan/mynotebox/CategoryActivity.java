package com.yunuscagliyan.mynotebox;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.yunuscagliyan.mynotebox.data.MyNoteBoxQueryHandler;
import com.yunuscagliyan.mynotebox.data.ToDoBoxContract.CategoryEntry;

import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;

public class CategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView lvNotes;
    EditText etCategoryName;
    Cursor cursor;
    CategoryCursorAdapter adapter;
    long selectedCategoryID=-1;
    Context mContext;
    MyNoteBoxQueryHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mContext=this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getLoaderManager().initLoader(5,null,  this);

        lvNotes=findViewById(R.id.listView);
        etCategoryName=findViewById(R.id.etCategoryName);


        adapter=new CategoryCursorAdapter(this,cursor,false);
        lvNotes.setAdapter(adapter);
        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryID=id;
                Cursor cursor=(Cursor)lvNotes.getItemAtPosition(position);
                //Toasty.info(mContext,"id:"+id+" Position:"+position,Toasty.LENGTH_LONG,true).show();
                etCategoryName.setText(cursor.getString(1));
            }
        });
        handler=new MyNoteBoxQueryHandler(this.getContentResolver());

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

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        if(i==5){
            String[] projection={CategoryEntry.ID,CategoryEntry.COLUMN_CATEGORY};
            return new CursorLoader(this,CategoryEntry.CONTENT_URI,projection,null,null,"_id desc");
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);

    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        adapter.swapCursor(null);

    }

    public void addNewCategory(View view) {
        etCategoryName.setText("");
        selectedCategoryID=-1;

    }

    public void saveCategory(View view) {
        String newCategoryName=etCategoryName.getText().toString();
        ContentValues values=new ContentValues();
        values.put(CategoryEntry.COLUMN_CATEGORY,newCategoryName);
        if(selectedCategoryID==-1){

            handler.startInsert(1,null,CategoryEntry.CONTENT_URI,values);
            etCategoryName.setText("");
            Toasty.success(mContext,"Category was inserted",Toasty.LENGTH_LONG,true).show();
        }else{
            String selection=CategoryEntry.ID+"=?";
            String[] args={String.valueOf(selectedCategoryID)};
            handler.startUpdate(1,null,CategoryEntry.CONTENT_URI,values,selection,args);
            Toasty.info(mContext,"Category was updated",Toasty.LENGTH_LONG,true).show();

        }

    }

    public void deleteCategory(View view) {
        String selection=CategoryEntry.ID+"=?";
        String[] args={String.valueOf(selectedCategoryID)};
        handler.startDelete(1,null,CategoryEntry.CONTENT_URI,selection,args);
        Toasty.warning(mContext,"Category was deleted",Toasty.LENGTH_LONG,true).show();
        etCategoryName.setText("");

    }
}
