package aad.app.c18;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class ViewTag extends Activity implements OnClickListener {

    private static final String TAG = ViewTag.class.getSimpleName();
    
    Button mWriteFrownyTagButton;
    Button mWriteSmileyTagButton;
    
    ImageView mImageView;
    
    Tag mTag;
    String mInfo;
    
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) 
                mImageView.setImageResource(R.drawable.smiley);           
            else if (msg.what == 2) 
                mImageView.setImageResource(R.drawable.frowny);
            else 
                mImageView.setImageResource(R.drawable.nothing);   
                        
            super.handleMessage(msg);
        }        
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.tag_layout);               
        
        Intent i = this.getIntent();

        mImageView = (ImageView) this.findViewById(R.id.mainImageView);
        
        mWriteFrownyTagButton = (Button) this.findViewById(R.id.writeFrownyTagButton);
        mWriteFrownyTagButton.setOnClickListener(this);
        
        mWriteSmileyTagButton = (Button) this.findViewById(R.id.writeSmileyTagButton);
        mWriteSmileyTagButton.setOnClickListener(this);
        
        mTag = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.i(TAG, "Found " + mTag.getId());
        
        Thread t = new Thread() {

            @Override
            public void run() {

                TagTool tt = new TagTool();        
                mInfo = tt.readTag(mTag);
                Log.i(TAG, "mInfo: " + mInfo);
                
                int what = 0;
                
                if (mInfo.substring(0, 4).equalsIgnoreCase("SMYL"))
                    what = 1;
                
                if (mInfo.substring(0, 4).equalsIgnoreCase("FRWN"))
                    what = 2;
                
                mHandler.sendEmptyMessage(what);
            }
        
        };
        
        t.start();
    }

    /**
     * Handle the click from both buttons
     */
    @Override
    public void onClick(View v) {

        Log.d(TAG, "onClick()");
        
        final boolean isSmiling = (v == this.mWriteSmileyTagButton);     

        Thread t = new Thread() {

            @Override
            public void run() {

                TagTool tt = new TagTool();        
                tt.writeTag(mTag, isSmiling);
                super.run();
            }
        
        };
        
        t.start();
    }
    
    

}
