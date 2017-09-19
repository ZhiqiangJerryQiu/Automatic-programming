package aad.app.c20;

import aad.app.l01.Fail;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class HelloBugsActivity extends Activity implements OnClickListener {

    private static final String TAG = HelloBugsActivity.class.getSimpleName();

    private SharedPreferences mPreferences;
    private boolean mShutdownWasClean;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.findViewById(R.id.emailButton).setOnClickListener(this);

        Fail fail = new Fail();
        fail.exampleAccessNull2();
    }

    @Override
    protected void onDestroy() {

        Log.d(TAG, "onDestroy()");

        // Mark that we shutdown successfully
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean("shutdown_was_clean", true);
        editor.commit();

        super.onDestroy();
    }

    @Override
    protected void onStart() {

        // Check to see if we started successfully
        mPreferences = this.getPreferences(MODE_PRIVATE);
        mShutdownWasClean = mPreferences.getBoolean("shutdown_was_clean", false);
        Log.d(TAG, "onStart() shutdown_was_clean: " + mShutdownWasClean);

        super.onStart();
    }

    @Override
    public void onClick(View v) {

        // Send an Email
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] { "xone@uw.edu" });
        i.putExtra(Intent.EXTRA_SUBJECT, "Application Report");
        i.putExtra(Intent.EXTRA_TEXT, "Important Information about your situation...");
        startActivity(Intent.createChooser(i, "Send Report"));

    }

}