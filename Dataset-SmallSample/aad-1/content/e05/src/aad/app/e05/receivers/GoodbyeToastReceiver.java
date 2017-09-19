package aad.app.e05.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GoodbyeToastReceiver extends BroadcastReceiver {
    
    private static final String TAG = GoodbyeToastReceiver.class.getSimpleName();    

    @Override
    public void onReceive(Context context, Intent intent) {
        
        Log.d(TAG, "onReceive() intent: " + intent);
        // If we receive anything it is the GOODBYE action, so just alert the UI
        
        Toast.makeText(context, "GoodbyeReceiver says Goodbye!!!", Toast.LENGTH_SHORT).show();
        
    }

}
