
package aad.app.e12.services;

import aad.app.e12.adapters.AccountSyncAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountSyncService extends Service {

    private static final Object mSyncAdapterLock = new Object();
    private static AccountSyncAdapter mAccountSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (mSyncAdapterLock) {
            if (mAccountSyncAdapter == null) {
                mAccountSyncAdapter = new AccountSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAccountSyncAdapter.getSyncAdapterBinder();
    }
}
