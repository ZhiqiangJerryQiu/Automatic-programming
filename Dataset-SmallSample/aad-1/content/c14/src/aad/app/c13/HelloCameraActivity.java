package aad.app.c13;

import java.util.UUID;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class HelloCameraActivity extends Activity implements OnClickListener {

    private static int IMAGE_CAPTURE_REQUEST = 1;
    private static String TAG = "HelloCameraActivity";

    private Uri mImageURI;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.findViewById(R.id.cameraAction).setOnClickListener(this);
        this.findViewById(R.id.cameraIntent).setOnClickListener(this);
        this.findViewById(R.id.recordAudioAction).setOnClickListener(this);
    }

    /** Get the result from the started Camera Activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_CAPTURE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult() Camera Returned OK image: " + mImageURI);
            }
            else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult() Camera Returned CANCELED");
            }
            else {
                Log.d(TAG, "onActivityResult() Camera Returned " + resultCode);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /** Handle the camera request button clicks */
    @Override
    public void onClick(View v) {

        Log.d(TAG, "onClick()");

        if (v.getId() == R.id.cameraAction) {
            startCameraAction();
        }
        else if (v.getId() == R.id.recordAudioAction) {
            startAudioAction();
        }
        else
        {
            startCameraIntent();
        }

    }

    /** Start the an internal audio intent */
    public void startAudioAction() {

        this.startActivity(new Intent(this, AudioActivity.class));
    }
    
    /** Start the an internal camera intent */
    public void startCameraAction() {

        this.startActivity(new Intent(this, CameraActivity.class));
    }

    /** Start the an external camera intent */
    public void startCameraIntent() {

        String imageFileName = UUID.randomUUID().toString() + ".jpg";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, imageFileName);

        mImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, mImageURI);

        try {
            startActivityForResult(i, IMAGE_CAPTURE_REQUEST);
        }
        catch (ActivityNotFoundException e) {
            Log.e(TAG, "Could not start a Camera Intent");
        }
    }

}