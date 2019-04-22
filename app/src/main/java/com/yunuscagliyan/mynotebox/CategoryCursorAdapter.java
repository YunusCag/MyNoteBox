package com.yunuscagliyan.mynotebox;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yunuscagliyan.mynotebox.data.ToDoBoxContract;

public class CategoryCursorAdapter extends CursorAdapter {


    public CategoryCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_one_row,parent,false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView=view.findViewById(R.id.tvCategory);
        int columnIndex=cursor.getColumnIndex(ToDoBoxContract.CategoryEntry.COLUMN_CATEGORY);
        String categoryText=cursor.getString(columnIndex);
        textView.setText(categoryText);

    }
}
