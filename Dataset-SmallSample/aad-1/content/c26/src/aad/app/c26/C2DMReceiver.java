
package aad.app.c26;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class C2DMReceiver extends BroadcastReceiver {

    public static final String TAG = C2DMReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive() Action: " + intent.getAction());

        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            handleRegistration(context, intent);
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            handleMessage(context, intent);
        }
    }

    private void handleMessage(Context context, Intent intent) {
        Log.d(TAG, "handleMessage() Action: " + intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String message = extras.getString("message");
            if (message != null && message.length() > 0) {
                
                Intent showIntent = new Intent(context, ShowMessage.class);
                showIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                showIntent.putExtra("message", message);
                context.startActivity(showIntent);                
            }
        }

    }

    private void handleRegistration(Context context, Intent intent) {
        Log.d(TAG, "handleRegistration() Action: " + intent.getAction());
        String registrationID = intent.getStringExtra("registration_id");

        if (intent.getStringExtra("error") != null) {
            Log.e(TAG, "handleRegistration() Error");
        } else if (intent.getStringExtra("unregistered") != null) {
            Log.w(TAG, "handleRegistration() Unregistered");
        } else if (registrationID != null) {
            Log.i(TAG, "handleRegistration() registrationID: " + registrationID);

        }
    }

}
