
package aad.app.e12;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class AccountSignin extends AccountAuthenticatorActivity {

    private static final String TAG = AccountSignin.class.getSimpleName();
    
    public static final String ACCOUNT_TYPE = "aad.app.e12";
    
    private AccountManager mAccountManager;
    
    private String mUsername;
    private EditText mUsernameEditText;
    private String mPassword;
    private EditText mPasswordEditText;
    
    public class SigninTask extends AsyncTask<Void, Void, String> {

        
        
        @Override
        protected void onPreExecute() {
            mUsername = mUsernameEditText.getText().toString();
            mPassword = mPasswordEditText.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                
                Log.d(TAG, "SigninTask:doInBackground()");
                
                // Check to see if there is an account already
                mAccountManager = AccountManager.get(AccountSignin.this);
                Account accounts[] = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

                if (accounts.length == 0) {

                    // Create a new Account
                    Account account = new Account(mUsername, ACCOUNT_TYPE);
                    mAccountManager.addAccountExplicitly(account, mPassword, null);
                    
                } else {
                    // Do whatever needs to be done if an account already exists... like update a password
                }                
                
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // Stop any Progress indication
            AccountSignin.this.finish();
            super.onPostExecute(result);
        }
        
        

    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.signin);
                
        mUsernameEditText = (EditText) this.findViewById(R.id.usernameEditText);
        mPasswordEditText = (EditText) this.findViewById(R.id.passwordEditText);
        
        Button signinButton = (Button) this.findViewById(R.id.signinButton);
        signinButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create the Account
                // TODO Progress indication
                new SigninTask().execute();
                
            }});
        
    }
    
    
}
