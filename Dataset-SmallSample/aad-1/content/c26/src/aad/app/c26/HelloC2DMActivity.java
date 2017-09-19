package aad.app.c26;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class HelloC2DMActivity extends Activity implements OnClickListener {

    private static final String TAG = HelloC2DMActivity.class.getSimpleName();
        
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.registerButton)
            registerC2DM();
        else if (view.getId() == R.id.unregisterButton)
            unregisterC2DM();
        else
            sendMessage();
    }
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.findViewById(R.id.registerButton).setOnClickListener(this);
        this.findViewById(R.id.unregisterButton).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }
    
    private void registerC2DM() { 
        Log.d(TAG, "registerC2DM()");
        Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
        //registrationIntent.putExtra("sender", "xone@washington.edu");
        registrationIntent.putExtra("sender", "uwaad.test@gmail.com");
        startService(registrationIntent);        
    }

    private void sendMessage() {
        Log.d(TAG, "sendMessage()");
        Intent messageIntent = new Intent("aad.app.c26.SelectAccountActivity"); 
        startActivity(messageIntent);
    }

    private void unregisterC2DM() {
        Log.d(TAG, "unregisterC2DM()");
        Intent unregeistrationIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
        unregeistrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
        startService(unregeistrationIntent);
    }
    
    
    

}