package aad.app.e10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BooksDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = BooksDatabaseHelper.class.getSimpleName();
    
    private static final String DB_NAME = "books.db";
    private static final int DB_VERSION = 1;
    private Context mContext;    

    public BooksDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "OnCreate()");
        
        InputStreamReader isr = new InputStreamReader(mContext.getResources().openRawResource(R.raw.default_db));
        BufferedReader sql = new BufferedReader(isr);
        StringBuilder statement = new StringBuilder();
        String line = null;
        try {
            while ((line = sql.readLine()) != null) {
                statement.append(line);
                if (line.endsWith(";")) {
                    db.execSQL(statement.toString());
                    statement = new StringBuilder();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int fromVersion, int toVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }
}
