package com.yunuscagliyan.mynotebox.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

public class MyNoteBoxQueryHandler extends AsyncQueryHandler {
    public MyNoteBoxQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
