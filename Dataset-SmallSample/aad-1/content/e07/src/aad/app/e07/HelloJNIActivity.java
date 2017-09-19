
package aad.app.e07;

import aad.app.e07.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.EditText;
import android.widget.TextView;

public class HelloJNIActivity extends Activity implements OnClickListener {

    private static final String TAG = HelloJNIActivity.class.getSimpleName();
    
    // Load the native library
    static {
        System.loadLibrary("hello-jni");
    }
    public native String getHelloWorldJNI();
    public native int incrementJNI(int value);
        
    private TextView mOutputTextView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        this.findViewById(R.id.incrementButton).setOnClickListener(this);
        
        mOutputTextView = (TextView) this.findViewById(R.id.outputTextView);
        mOutputTextView.setText(getHelloWorldJNI());
    }

    @Override
    public void onClick(View v) {
        
        switch(v.getId()) {
            
            case R.id.incrementButton:
                EditText et = (EditText) this.findViewById(R.id.incrementEditText);
                int value = Integer.parseInt(et.getText().toString());
                int returnValue = incrementJNI(value);
                mOutputTextView.setText(String.valueOf(returnValue));
                break;
        }
        
    }

}
