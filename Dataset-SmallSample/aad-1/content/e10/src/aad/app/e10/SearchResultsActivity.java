
package aad.app.e10;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class SearchResultsActivity extends ListActivity {

    private static final String TAG = SearchResultsActivity.class.getSimpleName();
    private ArrayList<String> mArrayList = new ArrayList<String>();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        
        // Extract the search action information
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
   
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList); 
        this.setListAdapter(arrayAdapter);
    }
    
    private void search(String query) {
        Log.d(TAG, "search()");
        
        SearchDatabaseHelper dbHelper = new SearchDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor c = db.rawQuery("SELECT * FROM books WHERE name MATCH '" + query + "'", null);

        mArrayList.clear();
        while(c.moveToNext()) {
            mArrayList.add(c.getString(c.getColumnIndex("name")));
         }
        
    }
    
}
