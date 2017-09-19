package aad.app.c27;

import aad.app.c27.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class HelloTestActivity extends Activity implements OnClickListener {

    private static final String TAG = HelloTestActivity.class.getSimpleName();
    
    private EditText mEditText;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mEditText = (EditText) this.findViewById(R.id.mainEditText);
        
        // Wire all the buttons
        TableLayout tl = (TableLayout) this.findViewById(R.id.mainTableLayout);
        setRowButtons(tl);

        startOperationBravoMikeTango();

    }

    // Some specialized recursion for setting the onClickListeners of the buttons
    private void setRowButtons(ViewGroup vg) {
        
        for (int i = 0; i < vg.getChildCount(); i++) {            
            View v = vg.getChildAt(i);
            if (v.getClass().equals(EditText.class)) {
                continue;
            }
            else if (v.getClass().equals(Button.class)) {                
                v.setOnClickListener(this);
            }
            else {
                setRowButtons((ViewGroup) v);
            }            
        }
    }
    
    
    @Override
    public void onClick(View v) {
        Button b = ((Button)v);
        String text = b.getText().toString();
        Log.i(TAG, "onClick() v:" + text);
        
        // Just append values
        mEditText.setText(mEditText.getText().append(text));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void startOperationBravoMikeTango() {
        
        Thread bitmapThread = new Thread() {

            @Override
            public void run() {
                
                while (true) {
                    
                    Bitmap bm = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_4444);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bm.recycle();
                }

            }          
            
        };
        bitmapThread.setName("BitmapThread");
        bitmapThread.start();
        
    }
    
  

}