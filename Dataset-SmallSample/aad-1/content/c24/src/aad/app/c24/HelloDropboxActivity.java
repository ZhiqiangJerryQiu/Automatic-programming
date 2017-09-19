package aad.app.c24;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import aad.app.c24.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

public class HelloDropboxActivity extends Activity {

    private static final String TAG = HelloDropboxActivity.class.getSimpleName();

    private String mAppKey = Constants.APP_KEY;
    private String mAppSecret = Constants.APP_SECRET;

    private Button mAuthorizeButton;
    private Button mContentButton;

    DropboxAPI<AndroidAuthSession> mApi;
    final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

    /**
     * Build the Dropbox AndroidAuthSession.
     * @return
     */
    private AndroidAuthSession buildSession() {

        AppKeyPair appKeyPair = new AppKeyPair(mAppKey, mAppSecret);
        AndroidAuthSession session;

        SharedPreferences sp = this.getSharedPreferences(Constants.PREF_FILE_DROPBOX, MODE_PRIVATE);
        mAppKey = sp.getString(Constants.KEY_ACCESS_KEY, null);
        mAppSecret = sp.getString(Constants.KEY_ACCESS_SECRET, null);

        if (mAppKey != null && mAppSecret != null) {
            AccessTokenPair accessToken = new AccessTokenPair(mAppKey, mAppSecret);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        }
        else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        return session;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // This is a Dropbox check to see if our manifest is correctly registered for the intent
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + mAppKey;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));        
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            Toast.makeText(this, "URL scheme in your app's manifest is not set up correctly. You should have a com.dropbox.client2.android.AuthActivity with the scheme: " + scheme, Toast.LENGTH_LONG);
            finish();
        }
        
        // Check to see if we are already authenticated
        SharedPreferences sp = this.getSharedPreferences(Constants.PREF_FILE_DROPBOX, MODE_PRIVATE);
        boolean isAuthenticated = sp.getBoolean("authenticated", false);
        
        mContentButton = (Button) findViewById(R.id.contentButton);
        mContentButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                writeContent();
            }
        });
        
        mAuthorizeButton = (Button) findViewById(R.id.buttonAuthorize);
        mAuthorizeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mApi.getSession().startAuthentication(HelloDropboxActivity.this);
            }
        });
        
        
        if (isAuthenticated) {
        
            Log.d(TAG, "onCreate() Already Authenticated");
            
            findViewById(R.id.buttonAuthorize).setVisibility(View.GONE);
            findViewById(R.id.contentEditText).setVisibility(View.VISIBLE);            
            mContentButton.setVisibility(View.VISIBLE);

        } 
        else {
            
            Log.d(TAG, "onCreate() Not Authenticated");
                        
            findViewById(R.id.contentEditText).setVisibility(View.GONE);
            findViewById(R.id.contentButton).setVisibility(View.GONE);
            mAuthorizeButton.setVisibility(View.VISIBLE);
            
            AndroidAuthSession session = buildSession();
            mApi = new DropboxAPI<AndroidAuthSession>(session);
    
        }
    }

    @Override
    protected void onResume() {
        
        Log.d(TAG, "onResume()");
        
        super.onResume();
        
        AndroidAuthSession session = mApi.getSession();

        // Complete the Dropbox authentication
        if (session.authenticationSuccessful()) {
            try {
                
                Log.d(TAG, "onResume() Authentication Successful");
                
                findViewById(R.id.buttonAuthorize).setVisibility(View.GONE);
                
                // Mandatory call to complete the auth
                session.finishAuthentication();
                
                // Write the access tokens to shared preferences
                SharedPreferences sp = this.getSharedPreferences(Constants.PREF_FILE_DROPBOX, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.KEY_ACCESS_KEY, session.getAccessTokenPair().key);
                editor.putString(Constants.KEY_ACCESS_SECRET, session.getAccessTokenPair().secret);
                editor.putBoolean("authenticated", true);
                editor.commit();
				
                findViewById(R.id.buttonAuthorize).setVisibility(View.GONE);
                findViewById(R.id.contentEditText).setVisibility(View.VISIBLE);            
                mContentButton.setVisibility(View.VISIBLE);
                        
            }
            catch (IllegalStateException e) {
                Toast.makeText(this, "Couldn't authenticate with Dropbox:" + e.getLocalizedMessage(), Toast.LENGTH_LONG);
                Log.e(TAG, "onResume() Error authenticating", e);
            }
        }
        else {
            findViewById(R.id.buttonAuthorize).setVisibility(View.VISIBLE);
        }
    }
    
    // Actually write the content out to Dropbox
    private void writeContent() {
        
        try {
            
            String fileContents = ((EditText) findViewById(R.id.contentEditText)).getText().toString();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContents.getBytes());

            String fileName = UUID.randomUUID().toString().substring(0, 6);
            
            Entry newEntry = mApi.putFile("/access/" + fileName + ".txt", inputStream, fileContents.length(), null, null);
            Log.i(TAG, "Uploading content as " + fileName);

            List<DropboxAPI.Entry> files = mApi.search("/Apps/Access Test 1/access/", ".txt", 50, false);
            Log.i(TAG, "onResume() files: " + files.size());
            for (DropboxAPI.Entry f : files) {
                Log.i(TAG, "onResume() Found file: " + f.fileName());
            }
        } catch (DropboxException e) {
            e.printStackTrace();
        }        
        
    }

}