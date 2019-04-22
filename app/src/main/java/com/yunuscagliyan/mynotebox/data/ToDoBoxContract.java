package com.yunuscagliyan.mynotebox.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class ToDoBoxContract {
    public static final String CONTENT_AUTHORITY="com.yunuscagliyan.mynotebox.data.mynoteboxprovider";
    public static final String PATH_NOTES="notes";
    public static final String PATH_CATEGORIES="categories";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    //com.yunuscagliyan.mynotebox.data.mynoteboxprovider

    public static final class NotesEntry implements BaseColumns {
        //content://com.yunuscagliyan.mynotebox.data.mynoteboxprovider/notes
        public static Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NOTES);

        public static final String TABLE_NAME="notes";

        public static final String ID=BaseColumns._ID;
        public static final String COLUMN_NOTE_CONTENT="note";
        public static final String COLUMN_CREATED_DATE="createdDate";
        public static final String COLUMN_END_DATE="endDate";
        public static final String COLUMN_CHECK="checked";
        public static final String COLUMN_CATEGORY_ID="categoryID";
    }
    public static final class CategoryEntry implements BaseColumns{
        //content://com.yunuscagliyan.mynotebox.data.mynoteboxprovider/categories
        public static Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_CATEGORIES);

        public static final String TABLE_NAME="categories";
        public static final String ID=BaseColumns._ID;
        public static final String COLUMN_CATEGORY="category";
    }
}
