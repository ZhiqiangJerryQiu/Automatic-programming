package aad.app.c06;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class ColorActivity extends Activity {

    public static final String TAG = "ColorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");
        
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.color_layout);

        // Turn off animations
        getWindow().setWindowAnimations(android.R.style.Animation);
        
        Bundle b = getIntent().getExtras();
        String colorString = b.getString("color");
        if (colorString != null)
            this.findViewById(R.id.color_layout).setBackgroundColor(Color.parseColor(colorString));

    }

}
