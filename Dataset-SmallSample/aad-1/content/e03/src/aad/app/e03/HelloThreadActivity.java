package aad.app.e03;

import aad.app.e03.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class HelloThreadActivity extends Activity {

    private static final String TAG = "HelloThreadActivity";

    public TextView mTextView;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            mTextView.setText("HelloThread!");
            super.handleMessage(msg);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextView = (TextView) this.findViewById(R.id.helloTextView);

        Thread t = new Thread() {

            @Override
            public void run() {

                // Don't do this
                // mTextView.setText("HelloThread!");

                // Do this
                mHandler.sendEmptyMessage(0);

                // Slow loop to keep the thread around to be viewed
                while (true) {
                    try {
                        Thread.sleep(10000);
                   }
                    catch (InterruptedException e) {
                        // Don't care if we are interrupted
                    }
                }

            }
        };

        t.setName("HelloThread Thread");
        t.start();
    }

}