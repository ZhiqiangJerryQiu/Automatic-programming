package aad.app.c08;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class DatabaseListActivity extends ListActivity {

    private ExampleSQLiteOpenHelper mExampleHelper;
    private SQLiteDatabase mReadableDB;
    
    private String[] mProjection = { ExampleSQLiteOpenHelper.TABLE_ROW_ID, ExampleSQLiteOpenHelper.TABLE_ROW_ONE, ExampleSQLiteOpenHelper.TABLE_ROW_TWO };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Create our database helper
        mExampleHelper = new ExampleSQLiteOpenHelper(this);
        SQLiteDatabase wdb = mExampleHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(ExampleSQLiteOpenHelper.TABLE_ROW_ONE, "First Item One");
        values.put(ExampleSQLiteOpenHelper.TABLE_ROW_TWO, "First Item Two");
        wdb.insert(ExampleSQLiteOpenHelper.TABLE_NAME, null, values);
        values.put(ExampleSQLiteOpenHelper.TABLE_ROW_ONE, "Second Item One");
        values.put(ExampleSQLiteOpenHelper.TABLE_ROW_TWO, "Second Item Two");
        wdb.insert(ExampleSQLiteOpenHelper.TABLE_NAME, null, values);
        
        wdb.close();
        
        mReadableDB = mExampleHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("content");
        Cursor c = qb.query(mReadableDB, mProjection, null, null, null, null, null);
        
        startManagingCursor(c);

        // Columns from the data to bind
        String[] from = new String[] { ExampleSQLiteOpenHelper.TABLE_ROW_ONE, ExampleSQLiteOpenHelper.TABLE_ROW_TWO };

        // The views to which the data will be bound
        int[] to = new int[] { R.id.rowOneTextView, R.id.rowTwoTextView };

        SimpleCursorAdapter contactAdapter = new SimpleCursorAdapter(this, R.layout.example_list_view_item, c, from, to);
        this.setListAdapter(contactAdapter);
    }

    @Override
    protected void onDestroy() {

        if (mReadableDB != null)
            mReadableDB.close();
        
        super.onDestroy();
    }
    
    

}
