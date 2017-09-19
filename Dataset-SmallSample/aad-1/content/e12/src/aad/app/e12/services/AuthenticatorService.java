package aad.app.e12.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {


    private Authenticator mAuthenticator;

    @Override
    public IBinder onBind(Intent intent) {

        return mAuthenticator.getIBinder();
    }
    
    @Override
    public void onCreate() {

        mAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {

    }

}
