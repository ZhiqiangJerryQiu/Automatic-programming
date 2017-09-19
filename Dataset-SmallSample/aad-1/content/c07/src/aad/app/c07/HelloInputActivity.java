package aad.app.c07;

import aad.app.c07.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class HelloInputActivity extends Activity implements OnClickListener {

    public static final String TAG = HelloInputActivity.class.getSimpleName();

    private ToggleButton mToggleButton;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if (msg.arg1 == R.id.radioToast) {
                setToast((String) msg.obj);
            }

            if (msg.arg1 == R.id.radioClipboard) {
                setClipboard((String) msg.obj);
            }

            if (msg.arg1 == R.id.radioDialog) {
                setDialog((String) msg.obj);
            }
        }    

    };   
        
    private SeekBar mSeekBar;
    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            setAwesomeLevel(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            // Not used
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            // Not used
        }

    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.findViewById(R.id.formButton).setOnClickListener(this);
        this.findViewById(R.id.sendButton).setOnClickListener(this);

        mSeekBar = (SeekBar) this.findViewById(R.id.awesomeSeekBar);
        mToggleButton = (ToggleButton) this.findViewById(R.id.awesomeToggleButton);

        mSeekBar.setOnSeekBarChangeListener(mSeekListener);

        // Initialize the level
        setAwesomeLevel(0);

        mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    setAwesomeLevel(mSeekBar.getMax());
                else
                    mSeekBar.setProgress(0);

            }
        });

    }

    private void setAwesomeLevel(int progress) {

        Log.d(TAG, "setAwesomeLevel() progress: " + progress);

        if (progress < 5) {
            mToggleButton.setTextOff("Boring " + progress);
            mToggleButton.setChecked(false);
            return;
        }

        if (progress < 11) {
            mToggleButton.setTextOff("Rocking " + progress);
            mToggleButton.setChecked(false);
            return;
        }

        mSeekBar.setProgress(mSeekBar.getMax());
        mToggleButton.setTextOn("Off the cliff " + progress + "!");
        mToggleButton.setChecked(true);

    }

    private void setToast(String text) {

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /**
     * Set the contents of the clipboard to the passed text.
     * 
     * @param text
     */
    private void setClipboard(String text) {

        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.setText(text); // This is the simplest of convenience calls, look to ClipData for storing more complex data
                          // in the clipboard
    }

    /**
     * Create a custom dialog from a layout to display our string.
     * 
     * @param text
     */
    private void setDialog(String text) {

        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle(this.getResources().getString(R.string.app_name));

        TextView tv = (TextView) dialog.findViewById(R.id.messageTextView);
        tv.setText(text);

        dialog.show();
    }

    /**
     * Click handler for the buttons on the Activity.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.formButton) {
            this.startActivity(new Intent(this, InputTypeActivity.class));
        }

        if (v.getId() == R.id.sendButton) {
            
            // Get whether this is going to be delayed
            CheckBox cb = (CheckBox) this.findViewById(R.id.futureCheckBox);
            boolean inTheFuture = cb.isChecked();                            
            
            // Get the content
            EditText et = (EditText) this.findViewById(R.id.messageEditText);
            String content = et.getText().toString();

            // Get the target
            RadioGroup rg = (RadioGroup) this.findViewById(R.id.radioGroup);
            
            // Package up the relevant information into a message
            Message m = new Message();
            m.obj = content;
            m.arg1 = rg.getCheckedRadioButtonId();
            
            if (inTheFuture) {
                mHandler.sendMessageDelayed(m, 5000);
            }
            else { // Send immediately
                mHandler.sendMessage(m);
            }
            

        }

    }

}