
package aad.app.e05.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {

    public static final String TAG = UpdateService.class.getSimpleName();
    
    // Return this instance of the service so clients can call public methods
    public class UpdateBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
    
    // Binder given to clients
    private final IBinder mBinder = new UpdateBinder();
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return mBinder;
    }

    /**
     * Public method to expose functionality.
     * @return
     */
    public void getGoodbye() {
        Log.d(TAG, "getGoodbye()");
        
        Intent goodbyeIntent = new Intent();
        goodbyeIntent.setAction("aad.app.e05.GOODBYE");
        this.sendBroadcast(goodbyeIntent);  
    }    
    
    /**
     * Public method to expose functionality.
     * @return
     */
    public String getHello() {
        Log.d(TAG, "getHello()");
        
        return "Hello";
    }    
    
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart() intent: " + intent);
        super.onStart(intent, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() intent: " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() intent: " + intent);

        // START_NOT_STICKY - Icky We want to live on, live free!
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
    }


}
