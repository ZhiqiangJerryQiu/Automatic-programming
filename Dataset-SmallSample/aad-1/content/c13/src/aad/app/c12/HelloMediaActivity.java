package aad.app.c12;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.HashMap;

public class HelloMediaActivity extends Activity implements OnClickListener, OnLoadCompleteListener {
    
    private static final String TAG = HelloMediaActivity.class.getSimpleName();

    private SoundPool mSoundPool;
    
    private int mMixID;
    private int mAK47ID;
    
    private HashMap<Integer, SoundResource> mSoundResources = new HashMap<Integer, SoundResource>();
    
    
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mMIDIMediaPlayer;
    
    private class SoundResource {
        
        public SoundResource(int id, boolean loaded, float volume) {
            this.id = id;
            this.loaded = loaded;
            this.volume = volume;
        }
        
        public boolean loaded;
        public int id;
        public float volume;
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.buttonLinearLayout);
        int childCount = ll.getChildCount(); 
        for (int i = 0; i < childCount; i++) {
            ll.getChildAt(i).setOnClickListener(this);
        }
        
        mAudioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(this);

        AssetManager am = this.getAssets();
        AssetFileDescriptor afd;
        try {
            afd = am.openFd("street.wav");
            mMixID = mSoundPool.load(afd, 1);
            
            afd = am.openFd("ak47.mp3");
            mAK47ID = mSoundPool.load(afd, 1);
            //mAK47ID = mSoundPool.load(this, R.raw.ak47, 1);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        mMediaPlayer = new MediaPlayer();
        mMIDIMediaPlayer = new MediaPlayer();
        
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        
        mSoundResources.put(sampleId, new SoundResource(sampleId, true, 1f));
    }
    
    @Override
    public void onClick(View v) {
        
        switch (v.getId()) {
            
            // Pulled because of http://code.google.com/p/android/issues/detail?id=13453
            //case R.id.loopPlayMP3 :
                //playLoop();
                //break;
                
            case R.id.playAssetMID1 :
                playAssetMID(1);
                break;
                
            case R.id.playAssetMID2 :
                playAssetMID(2);
                break;                
                
            case R.id.playAssetOgg :
                playAssetOgg();
                break;
                
            case R.id.playMP3 :
                playMP3();
                break;
                
            case R.id.playHit :
                playHit();
                break;                
                
        }
        
    }
    
    private void playHit() {
                
        SoundResource sr = mSoundResources.get(mAK47ID);
        if (sr == null) {
            Log.e(TAG, "playHit() Cound not find SoundResource for ID: " + mAK47ID);
            return;
        }
        
        if (sr.loaded) {
            mSoundPool.play(sr.id, 0.5f, 0.5f, 1, 0, 1f);
        }
    }
    
//    private void playLoop() {
//        
//        Log.d(TAG, "playLoop() mixID: " + mMixID);
//                        
//        SoundResource sr = mSoundResources.get(mMixID);
//        if (sr.loaded) {
//            mSoundPool.play(sr.id, 0.5f, 0.5f, 1, 2, 1f);
//        }
//        
//    }
    
    private void playAssetMID(int which) {
        
        if (mMIDIMediaPlayer.isPlaying()) {
            Log.w(TAG, "playAssetMP3() MediaPlayer already playing");
            mMIDIMediaPlayer.stop();
            return;
        }

        mMIDIMediaPlayer.reset();
        
        AssetFileDescriptor afd;
               
        String fileString;
        if (which == 1) {
            fileString = "songs_United_States";
        }
        else {
            fileString = "title";
        }
        
        try {
            afd = getAssets().openFd(fileString + ".mid");
            mMIDIMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
            mMIDIMediaPlayer.prepare();
            mMIDIMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private void playAssetOgg() {
        
        if (mMediaPlayer.isPlaying()) {
            Log.w(TAG, "playAssetOgg() MediaPlayer already playing");
            mMediaPlayer.stop();            
            return;
        }
        
        mMediaPlayer.reset();
        
        AssetFileDescriptor afd;
        try {
            afd = getAssets().openFd("corp.ogg");
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private void playMP3() {
        
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.nuke);
        mediaPlayer.start();
    }
    
}