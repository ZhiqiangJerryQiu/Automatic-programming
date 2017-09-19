package aad.app.c23;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendsSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "friends";
    private final static int DB_VERSION = 1;

    public final static String TABLE_NAME = "content";
    public final static String TABLE_ROW_ID = "_id";
    public final static String TABLE_FB_NAME = "fb_name";
    public final static String TABLE_FB_ID = "fb_id";
    
    public FriendsSQLiteOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQueryString = "CREATE TABLE " + TABLE_NAME + " (" + TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TABLE_FB_NAME + " TEXT," + TABLE_FB_ID + " TEXT" + ");";
        db.execSQL(createTableQueryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
