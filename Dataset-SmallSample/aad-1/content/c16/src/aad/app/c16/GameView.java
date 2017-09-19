package aad.app.c16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface.OutOfResourcesException;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = SurfaceView.class.getSimpleName();

    public static final int SOUND_EXPLOSION = 1;

    private AnimationThread mAnimationThread;
    private Random mBugRandom = new Random();
    private int mScore;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap;
    private volatile float mTouchedX;
    private volatile float mTouchedY;

    private volatile ArrayList<GameObject> mObjects = new ArrayList<GameObject>();
    private volatile ArrayList<GameObject> mDeadObjects = new ArrayList<GameObject>();

    private volatile Bitmap mBugBitmap;
    private volatile Bitmap mExplosionBitmap;
    private volatile Bitmap mSplatBitmap;
    
    private BitmapDrawable mFloorDrawable;

    private int mSurfaceHeight;
    private int mSurfaceWidth;
    
    private boolean mIsGameRunning = true;
    
        
    class AnimationThread extends Thread {

        private boolean mIsThreadRunning;
        private Paint textPaint;
        private long mTimeNow;
        private long mTimeLast;

        private int frameSamplesCollected = 0;
        private int frameSampleTime = 0;
        private int fps = 0;

        private SurfaceHolder mSurfaceHolder;        

        public AnimationThread(SurfaceHolder surfaceHolder) {

            mSurfaceHolder = surfaceHolder;

            // Set our pixel format?
            //mSurfaceHolder.setFormat(android.graphics.PixelFormat.RGBA_8888);
            
            textPaint = new Paint();
            textPaint.setARGB(255, 255, 255, 255);
            textPaint.setTextSize(32);
        }

        @Override
        public void run() {

            // Add the start bug
            addBug();

            while (mIsThreadRunning) {
                Canvas c = null;
                try {
                    // TODO I don't think we can pass a null here...
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        update();
                        draw(c);
                    }
                }
                finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        private void update() {

            mTimeNow = System.currentTimeMillis();

            if (mTimeLast > mTimeNow)
                return;

            if (mTimeLast != 0) {

                // Time difference between now and last time we were here
                int time = (int) (mTimeNow - mTimeLast);
                frameSampleTime += time;
                frameSamplesCollected++;

                // After 10 frames
                if (frameSamplesCollected == 10) {

                    // Update the fps variable
                    fps = (int) (10000 / frameSampleTime);

                    // Reset the sampletime + frames collected
                    frameSampleTime = 0;
                    frameSamplesCollected = 0;
                }
            }
            
            // Add a new bug - making things increasingly difficult
            if (mBugRandom.nextInt(1000) > (990 - (mScore)))
                addBug();
            

            synchronized (mObjects) {

                for (GameObject object : mObjects) {

                    // Update the object
                    object.update(mTimeNow);
                    
                    // Update the bugs march to destruction!
                    if (object.getType() == GameObjectType.Bug) {
                        
                        // Check to see if we have a winner (a bug that is)
                        if (object.getY() > mSurfaceHeight) {
                            endGame();
                        }
                        
                        // See if the bug is hit
                        boolean isHit = false;
                        for (GameObject explosion : mObjects) {
                            if (explosion.getType() == GameObjectType.Explosion) {
                                isHit = ((BugGameObject) object).isHit(explosion.getX(), explosion.getY());
                            }
                        }                     
                        
                        if (isHit)
                            ((BugGameObject) object).health--;
                        
                        if (((BugGameObject) object).health < 0) {
                            
                            // Only score if the bug is still alive
                            if (((BugGameObject) object).isAlive())
                            {
                                mScore++;
                                ((BugGameObject) object).splat(mSplatBitmap);
                            }
                            
                        }
                        
                        // This bug has thus far survived, move it...
                        ((BugGameObject) object).move();                    
                    }
                    
                    // Remove the dead objects                    
                    if (object.getSprite().dead)                        
                        mDeadObjects.add(object);
                    
                }

            }
            
            // Clear out all the dead sprites from the sprites collection
            synchronized (mObjects) {
                mObjects.removeAll(mDeadObjects);
                mDeadObjects.clear();
            }

            mTimeLast = mTimeNow;
        }

        private void draw(Canvas canvas) {

            // Draw the background color. Operations on the Canvas accumulate
            // so this is like clearing the screen. In a real game you can put in a background image of course
            canvas.drawColor(Color.GRAY);
            
           
            mFloorDrawable.draw(canvas);
            
            // canvas.drawPoint(mTouchedX, mTouchedY, pointPaint);
            synchronized (mObjects) {
                for (GameObject object : mObjects) {
                    // for (AnimatedSprite sprite : mSprites) {
                    object.draw(canvas);
                    // sprite.draw(canvas);
                }
            }

            if (mIsThreadRunning)
                canvas.drawText("Score: " + mScore + " Objects: " + mObjects.size() + " FPS: " + fps, 0, 40, textPaint);
            else
                canvas.drawText("Game Over - Score: " + mScore, 0, 40, textPaint);
            
            
            canvas.restore();
        }

        public void setRunning(boolean isRunning) {

            mIsThreadRunning = isRunning;
        }

    }

    private void initSounds() {

        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap<Integer, Integer>();

        // Load the sounds into the sound pool
        mSoundPoolMap.put(SOUND_EXPLOSION, mSoundPool.load(getContext(), R.raw.sound_explosion, 1));
    }

    public void playSound(int sound) {

        AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        mSoundPool.play(mSoundPoolMap.get(sound), volume, volume, 1, 0, 1f);
    }

    private void addBug() {

        BugGameObject bug = new BugGameObject(this.mBugBitmap);

        int startX = mBugRandom.nextInt(mSurfaceWidth - 128) + 64;
        
        bug.setX(startX);
        bug.setY(-128);

        synchronized (this) {
            mObjects.add(bug);
        }
    }
    
    private void endGame() {
        
        mIsGameRunning = false;
        mAnimationThread.setRunning(mIsGameRunning);
    }

    public GameView(Context context, AttributeSet attrs) {

        super(context, attrs);

        initSounds();
        
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mBugBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.sprite_cockroach));
        mExplosionBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.sprite_explosion));       
        mSplatBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.sprite_splat));
        
        mFloorDrawable = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.image_floor));
        
        mAnimationThread = new AnimationThread(holder);
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mTouchedX = event.getX();
        mTouchedY = event.getY();

        int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:

            if (mIsGameRunning)
            {
                ExplosionGameObject explosion = new ExplosionGameObject(mExplosionBitmap);
                explosion.setX((int) mTouchedX);
                explosion.setY((int) mTouchedY);
                
                synchronized (mObjects) {
                    mObjects.add(explosion);
                }
                playSound(SOUND_EXPLOSION);
            }
            break;

        }

        try {
            Thread.sleep(16);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Callback invoked when the surface dimensions change.
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // NOOP
    }

    /**
     * Callback invoked when the Surface has been created and is ready to be used.
     */
    public void surfaceCreated(SurfaceHolder holder) {

        
        mSurfaceHeight = holder.getSurfaceFrame().height();
        mSurfaceWidth = holder.getSurfaceFrame().width();
        
        holder.setFormat(PixelFormat.RGBA_8888);
        
        mFloorDrawable.setBounds(0, 0, holder.getSurfaceFrame().width(), mSurfaceHeight);
        mFloorDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        
        // The thread is actually started here, once the surface is created.
        mAnimationThread.setRunning(true);
        mAnimationThread.start();
    }

    /**
     * Callback invoked when the Surface has been destroyed. Don't use the surface again after this call.
     */
    public void surfaceDestroyed(SurfaceHolder holder) {

        // Cleanly shutdown the thread
        boolean retry = true;
        mAnimationThread.setRunning(false);
        while (retry) {
            try {
                mAnimationThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
