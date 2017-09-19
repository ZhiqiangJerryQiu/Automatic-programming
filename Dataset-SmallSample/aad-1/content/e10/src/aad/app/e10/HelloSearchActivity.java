
package aad.app.e10;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HelloSearchActivity extends Activity {

    private static final String TAG = HelloSearchActivity.class.getSimpleName();
        
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }
    
    
}
