package com.yunuscagliyan.mynotebox;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yunuscagliyan.mynotebox.data.ToDoBoxContract;

public class NoteCursorAdapter extends CursorAdapter {
    public NoteCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.one_row_note,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView note=view.findViewById(R.id.tvNote);
        int noteColumnIndex=cursor.getColumnIndex(ToDoBoxContract.NotesEntry.COLUMN_NOTE_CONTENT);
        String noteContent=cursor.getString(noteColumnIndex);
        note.setText(noteContent);

    }
}
