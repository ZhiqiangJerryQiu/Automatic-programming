
package aad.app.e10;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class HelloWidgetActivity extends ListActivity {

    private static final String TAG = HelloWidgetActivity.class.getSimpleName();
    
    private BooksDatabaseHelper mDBHelper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);

        mDBHelper = new BooksDatabaseHelper(this);
        
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        
        //String[] projection = { "_id", "name", "isbn" };
        Cursor c = db.query("books", null, null, null, null, null, null);
        
        String[] from = new String[] { "name", "isbn" };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

        SimpleCursorAdapter contactAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to);
        this.setListAdapter(contactAdapter);        
        
        db.close();
        
    }
    
    
}
