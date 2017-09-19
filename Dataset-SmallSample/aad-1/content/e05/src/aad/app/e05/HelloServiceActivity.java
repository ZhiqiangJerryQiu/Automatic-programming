
package aad.app.e05;

import aad.app.e05.services.UpdateService;
import aad.app.e05.services.UpdateService.UpdateBinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.TextView;

public class HelloServiceActivity extends Activity implements OnClickListener {

    private static final String TAG = HelloServiceActivity.class.getSimpleName();

    private UpdateService mService;
    private boolean mServiceIsBound;
    
    private Handler mGoodbyeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            TextView tv = (TextView) findViewById(R.id.serviceTextView);
            tv.setText(tv.getText().toString() + " Goodbye " );
            super.handleMessage(msg);
        }
    };
    
    private GoodbyeTextReceiver mGoodbyeTextReceiver =  new GoodbyeTextReceiver();
    private class GoodbyeTextReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive() intent: " + intent);
            mGoodbyeHandler.sendEmptyMessage(1);            
        }
    }
    
    /** 
     * Service Binding callbacks
     **/
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected()");
            UpdateBinder binder = (UpdateBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "onServiceDisconnected()");
            
            mServiceIsBound = false;
            findViewById(R.id.getGoodbyeButton).setEnabled(mServiceIsBound);
            findViewById(R.id.getHelloButton).setEnabled(mServiceIsBound);
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.startButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);
        findViewById(R.id.getHelloButton).setOnClickListener(this);
        findViewById(R.id.getGoodbyeButton).setOnClickListener(this);
        
        this.registerReceiver(mGoodbyeTextReceiver, new IntentFilter("aad.app.e05.GOODBYE"));       

    }

    @Override
    public void onClick(View v) {
        
        switch(v.getId()) {
            
            case R.id.startButton:
                Log.d(TAG, "startService()");
                
                // This call to startService isn't really necessary as BIND_AUTO_CREATE does just that
                // !!! But it does help fix the START_STICKY option
                startService(new Intent(this, UpdateService.class));
                
                // Bind to the service
                bindService(new Intent(this, UpdateService.class), mConnection, Context.BIND_AUTO_CREATE);
                
                mServiceIsBound = true;
                findViewById(R.id.getGoodbyeButton).setEnabled(mServiceIsBound);
                findViewById(R.id.getHelloButton).setEnabled(mServiceIsBound);
                
                break;
                
            case R.id.stopButton:
                Log.d(TAG, "stopService()");
                
                mServiceIsBound = false;
                findViewById(R.id.getGoodbyeButton).setEnabled(mServiceIsBound);
                findViewById(R.id.getHelloButton).setEnabled(mServiceIsBound);
                
                if (mServiceIsBound) 
                    unbindService(mConnection);
                
                stopService(new Intent(this, UpdateService.class));
                break;
                
            case R.id.getHelloButton:
                if (mServiceIsBound) {
                    TextView tv = (TextView) this.findViewById(R.id.serviceTextView);
                    tv.setText(tv.getText().toString() + " " + mService.getHello());
                }
                break;
                
            case R.id.getGoodbyeButton:
                if (mServiceIsBound) {
                    mService.getGoodbye();
                }
                break;
        }
        
    }

    @Override
    protected void onStop() {
        
        // Unbind the service
        if (mServiceIsBound) {
            unbindService(mConnection);
            mServiceIsBound = false;
        }
        
        // Cleanup our receiver
        this.unregisterReceiver(mGoodbyeTextReceiver);
        
        super.onStop();
    }   
    

}
