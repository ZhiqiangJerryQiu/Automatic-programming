
package aad.app.e12;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountDetails extends Activity {
    
    private AccountManager mAccountManager;
    private String username;
    private TextView usernameTextView;
    private String password;
    private TextView passwordTextView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.details);
        
        try {
                        
            // Check to see if there is an account already
            mAccountManager = AccountManager.get(AccountDetails.this);
            Account accounts[] = mAccountManager.getAccountsByType("aad.app.e12");

            if (accounts.length > 0) {

                username = accounts[0].name;
                password = mAccountManager.getPassword(accounts[0]);
                
                usernameTextView = (TextView) this.findViewById(R.id.usernameTextView);
                passwordTextView = (TextView) this.findViewById(R.id.passwordTextView);
                
                usernameTextView.setText(username);
                passwordTextView.setText(password);
                
            } else {
                // Not Account Found
            }                
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

}
