package aad.app.c22;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HelloYouTubeActivity extends ListActivity {

    public static final String TAG = HelloYouTubeActivity.class.getSimpleName();
    
    private SharedPreferences mSharedPreferences;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);

        Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
        this.setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, accounts));
       
        mSharedPreferences = this.getSharedPreferences(Constants.PREF_NAME, 0);
        Editor e = mSharedPreferences.edit();
        e.clear();
        e.commit();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Account account = (Account) getListView().getItemAtPosition(position);
        Intent intent = new Intent(this, OAuth2Activity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }

}