package aad.app.c08;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ExampleSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "example";
    private final static int DB_VERSION = 1;

    public final static String TABLE_NAME = "content";
    public final static String TABLE_ROW_ID = "_id";
    public final static String TABLE_ROW_ONE = "row_one";
    public final static String TABLE_ROW_TWO = "row_two";
    
    public ExampleSQLiteOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQueryString = "CREATE TABLE " + TABLE_NAME + " (" + TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TABLE_ROW_ONE + " TEXT," + TABLE_ROW_TWO + " TEXT" + ");";
        db.execSQL(createTableQueryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
