package aad.app.e03;

import aad.app.e03.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;

public class HelloFilterActivity extends Activity {

    private static final String TAG = "HelloFilterActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ((Button) this.findViewById(R.id.testButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Try to start an intent with only ACTION_VIEW defined
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                startActivity(intent);

            }
        });

    }

}