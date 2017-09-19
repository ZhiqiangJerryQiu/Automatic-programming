
package aad.app.c13;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity {

    private static final String TAG = CameraActivity.class.getSimpleName();

    private CameraPreview mCameraPreview;
    private Button buttonClick;
    private Handler mHandler = new Handler();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        Log.d(TAG, "onCreate()");
        
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera);
               
        mCameraPreview = new CameraPreview(this);
        FrameLayout fl = (FrameLayout) findViewById(R.id.preview);
        fl.addView(mCameraPreview);
        fl.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mCameraPreview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        // We are just getting a single default camera
        mCameraPreview.camera = Camera.open();
    }

    /**
     * Release the resources when paused.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mCameraPreview.camera != null) {
            mCameraPreview.camera.release();
            mCameraPreview.camera = null;
        }
    }
    
    ShutterCallback shutterCallback = new ShutterCallback() {
        
        public void onShutter() {
            Log.d(TAG, "shutterCallback:onShutter()");
            
            // Play a sound
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    MediaPlayer mp = MediaPlayer.create(CameraActivity.this, R.raw.shutter);
                    mp.start();                    
                }
            };
            
            mHandler.postDelayed(r, 1000);
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "rawCallback:onPictureTaken()");
        }
    };

    // PictureCallback to handle saving the picture to storage
    PictureCallback jpegCallback = new PictureCallback() {
        
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "jpegCallback:onPictureTaken()");
            
            FileOutputStream fos = null;
            try {                
                String fileName = String.format(Environment.getExternalStorageDirectory().getAbsolutePath() + "/%d.jpg", System.currentTimeMillis());
                fos = new FileOutputStream(fileName);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            
        }
    };

}
