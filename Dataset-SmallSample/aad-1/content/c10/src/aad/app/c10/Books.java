package aad.app.c10;

import android.provider.BaseColumns;

public class Books implements BaseColumns {

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
    
    public static final class Authors {
    	
    	public static final String TABLE_NAME = "authors";
    	
    }

}
