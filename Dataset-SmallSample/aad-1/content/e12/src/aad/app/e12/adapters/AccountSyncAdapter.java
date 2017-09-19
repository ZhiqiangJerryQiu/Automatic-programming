package aad.app.e12.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;


public class AccountSyncAdapter extends AbstractThreadedSyncAdapter {
    
    private static final String TAG = AccountSyncAdapter.class.getSimpleName();

    public AccountSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync()");
        
        // This is where you would take your data and store it locally or merge it with existing elements
        
        // Use SyncResult to track your status
        syncResult.stats.numEntries++;
    }
}
