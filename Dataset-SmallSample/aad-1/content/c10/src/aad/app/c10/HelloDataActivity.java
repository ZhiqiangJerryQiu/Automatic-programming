package aad.app.c10;

import aad.app.c10.R;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;


public class HelloDataActivity extends ListActivity {

    private static final String TAG = HelloDataActivity.class.getSimpleName();
    
    private Context mContext;
    
    private BooksSQLiteOpenHelper mDatabaseHelper; 
    private SQLiteDatabase mReadableDB;
    private SQLiteDatabase mWritableDB;
    
    private class PutBookAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            
            synchronized (mDatabaseHelper) {
                
                mWritableDB = mDatabaseHelper.getWritableDatabase();
                
                ContentValues cv = new ContentValues();
                cv.put(Books.Book.NAME, "Pro Android X");
                cv.put(Books.Book.ISBN, "1234567890");
                mWritableDB.insert(Books.Book.TABLE_NAME, null, cv);
                
                mWritableDB.close();
            }
            
            return null;
        }

    }
    
    private class GetBooksAsyncTask extends AsyncTask<Void, Void, Void> {

        Cursor c;
        
        @Override
        protected Void doInBackground(Void... params) {
            
            mReadableDB = mDatabaseHelper.getReadableDatabase();
            
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(Books.Book.TABLE_NAME);
            c = qb.query(mReadableDB, Books.Book.PROJECTION, null, null, null, null, null);
            startManagingCursor(c);
            
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {

            // Columns from the data to bind
            String[] from = new String[] { Books.Book.NAME, Books.Book.ISBN };

            // The views to which the data will be bound
            int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
            
            SimpleCursorAdapter bookAdapter = new SimpleCursorAdapter(mContext, android.R.layout.simple_list_item_2, c, from, to);
            setListAdapter(bookAdapter);  
            
            mReadableDB.close();
            
            super.onPostExecute(response);
        }
    }
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       
        mContext = this;        
        mDatabaseHelper = new BooksSQLiteOpenHelper(this);
        
        new PutBookAsyncTask().execute();
        new GetBooksAsyncTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         
        switch (item.getItemId()) {
            case R.id.menu_item_delete_all:
                
                // Delete all entries -- WHY IS THIS BAD?
                synchronized (mDatabaseHelper) {
                    
                    mWritableDB = mDatabaseHelper.getWritableDatabase();
                    mWritableDB.delete(Books.Book.TABLE_NAME, null, null);
                    mWritableDB.close();
                    
                    new GetBooksAsyncTask().execute(); 
                }
                break;              
         
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    


}