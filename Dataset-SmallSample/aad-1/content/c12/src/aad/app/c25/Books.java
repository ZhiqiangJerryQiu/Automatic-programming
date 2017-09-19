package aad.app.c25;

import aad.app.c25.providers.BooksContentProvider;
import android.net.Uri;
import android.provider.BaseColumns;

public class Books implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + BooksContentProvider.AUTHORITY + "/books");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aad.app.c25.books";

    public static final class Book {
        
        public static final String TABLE_NAME = "book";

        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String ISBN = "isbn";

        public static final String[] PROJECTION = new String[] {
        /* 0 */ Books.Book.ID,
        /* 1 */ Books.Book.NAME,
        /* 2 */ Books.Book.ISBN };

    }

}
