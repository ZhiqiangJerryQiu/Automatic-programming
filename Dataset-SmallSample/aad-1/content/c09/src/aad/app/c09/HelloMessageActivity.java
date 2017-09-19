
package aad.app.c09;

import aad.app.c09.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class HelloMessageActivity extends Activity {

    public static final String TAG = HelloMessageActivity.class.getSimpleName();

    private TextView mTextView;

    private StringBuilder mStringBuilder;

    // Main handler for this thread
    private Handler mMainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            addText((String) msg.obj);

            super.handleMessage(msg);
        }
    };

    // Custom Thread class
    private class CustomThread extends Thread {

        private Handler mThreadHandler = new Handler();

        private boolean mIsRunning = true;

        public void CustomThread() {
            this.setName("CustomThread");
        }

        public void setIsRunning(boolean value) {
            mIsRunning = value;
        }

        @Override
        public void run() {
            while (mIsRunning) {
                try {
                    Thread.sleep(3000);

                    String output = "Called from "
                            + Thread.currentThread().getName()
                            + " thread, mTheadHandler hash: " + mThreadHandler.hashCode()
                            + " mMainHandler hash: " + mMainHandler.hashCode();

                    // !!!
                    addText(output);
                    // !!!

                    // Get a message from the global pool
                    Message msg = Message.obtain();
                    msg.obj = output;
                    mMainHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
            }
            // super.run();
        }

    };

    // Custom AsyncTask class
    private class CustomTask extends AsyncTask<Void, Void, Void> {

        private Handler mAsyncHandler = new Handler();

        @Override
        protected Void doInBackground(Void... params) {

            try {
                while (true) {
                    Thread.sleep(4000);
                    String output = "Called from "
                            + Thread.currentThread().getName()
                            + " thread, mTheadHandler hash: " + mAsyncHandler.hashCode()
                            + " mMainHandler hash: " + mMainHandler.hashCode();

                    // !!!
                    addText(output);
                    // !!!

                    // Get a message from the global pool
                    Message msg = Message.obtain();
                    msg.obj = output;
                    mMainHandler.sendMessage(msg);
                }

            } catch (InterruptedException e) {
                // e.printStackTrace();
            }

            return null;
        }

    };

    private CustomThread mCustomThread;
    private CustomTask mCustomTask;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mStringBuilder = new StringBuilder();
        mTextView = (TextView) this.findViewById(R.id.mainTextView);

        addText("Called from " + Thread.currentThread().getName() + " thread, mMainHandler hash: "
                + mMainHandler.hashCode());

        // Start a new CustomThread
        mCustomThread = new CustomThread();
        mCustomThread.start();

        // Start a new CustomTask
        mCustomTask = new CustomTask();
        mCustomTask.execute((Void[]) null);
    }

    private void addText(String text) {

        mStringBuilder.append(text);
        mStringBuilder.append("\n");

        Log.d(TAG, "addText(): " + text);

        mTextView.setText(mStringBuilder.toString());
    }

    @Override
    protected void onStop() {

        // !!! Stop our threads
        mCustomThread.setIsRunning(false);
        mCustomTask.cancel(true);
        // !!!

        super.onStop();
    }

}
