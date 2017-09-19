
package aad.app.c13;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class AudioActivity extends Activity implements OnClickListener {

    private static final String TAG = AudioActivity.class.getSimpleName();

    private Button mIntentButton;
    private Button mPlayButton;
    private Button mStartButton;
    private Button mStopButton;
    private MediaRecorder mMediaRecorder;
    private static String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.audio);

        mIntentButton = (Button) this.findViewById(R.id.recordIntent);
        mIntentButton.setOnClickListener(this);
        
        mPlayButton = (Button) this.findViewById(R.id.playAudioRecord);
        mPlayButton.setOnClickListener(this);

        mStartButton = (Button) this.findViewById(R.id.startAudioRecord);
        mStartButton.setOnClickListener(this);

        mStopButton = (Button) this.findViewById(R.id.stopAudioRecord);
        mStopButton.setOnClickListener(this);

        mMediaRecorder = new MediaRecorder();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            
            case R.id.recordIntent:
                Intent i = new Intent("android.provider.MediaStore.RECORD_SOUND");
                startActivityForResult(i, 0);
                break;

            case R.id.playAudioRecord:
                playRecording();
                break;

            case R.id.startAudioRecord:
                startRecording();
                break;

            case R.id.stopAudioRecord:
                stopRecording();
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (mMediaRecorder != null)
            mMediaRecorder.release();
    }

    private void playRecording() {

        File playFile = new File(mFileName);
        if (playFile.exists()) {

            try {
                MediaPlayer mp = new MediaPlayer();
                FileInputStream fis = new FileInputStream(playFile);
                mp.setDataSource(fis.getFD());
                mp.prepare();
                mp.start();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            Log.e(TAG, "playRecording() File does not exist: " + mFileName);
        }
    }

    private void startRecording() {

        mMediaRecorder.reset();

        String fileName = UUID.randomUUID().toString().substring(0, 6).concat(".3gp");
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(mFileName);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaRecorder.start();
    }

    private void stopRecording() {

        if (mMediaRecorder == null)
            return;

        mMediaRecorder.stop();
        mMediaRecorder = null;
    }
    
    

}
