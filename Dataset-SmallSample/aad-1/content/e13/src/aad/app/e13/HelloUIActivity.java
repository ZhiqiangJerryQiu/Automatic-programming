
package aad.app.e13;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;


public class HelloUIActivity extends AccountAuthenticatorActivity {

    private static final String TAG = HelloUIActivity.class.getSimpleName();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
                
    }
    
    
}
