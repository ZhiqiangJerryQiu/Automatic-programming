package aad.app.c16;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/** Animated sprite that draws itself from a sprite sheet to the canvas
 * 
 * @author Mike
 *
 */
public class AnimatedSprite {

    public GameObjectType type = GameObjectType.Explosion;

    private Bitmap mBitmap;
    private Rect mSource;
    private int mNumFrames;
    private int mCurrentFrame = 0;
    private long mFrameTicker;

    private int mSpriteWidth;
    private int mSpriteHeight;

    private int mX;
    private int mY;

    private boolean mIsLooping = false;

    public boolean dead = false;

    private int mFPS;

    public AnimatedSprite() {

        mSource = new Rect(0, 0, 0, 0);
        mFrameTicker = 0;
        mCurrentFrame = 0;
        mX = 0;
        mY = 0;
        dead = false;
    }

    public void draw(Canvas canvas, int x, int y) {

        setX(x);
        setY(y);
        Rect dest = new Rect(mX, mY, mX + mSpriteWidth, mY + mSpriteHeight);
        canvas.drawBitmap(mBitmap, mSource, dest, null);;
    }

    public int getX() {

        return mX;
    }

    public int getY() {

        return mY;
    }

    public void initialize(Bitmap bitmap, int width, int height, int fps, int numFrames, boolean isLooping) {
        
        mBitmap = bitmap;
        mFPS = 1000 / fps;
        mNumFrames = numFrames;

        mSpriteHeight = height;
        mSpriteWidth = width;

        mSource.top = 0;
        mSource.bottom = mSpriteHeight;
        mSource.left = 0;
        mSource.right = mSpriteWidth;

        mIsLooping = isLooping;
    }

    public void setX(int value) {

        mX = value - (mSpriteWidth / 2);
    }

    public void setY(int value) {

        mY = value - (mSpriteHeight / 2);
    }

    public void update(long gameTime) {

        if (gameTime > mFrameTicker + mFPS) {
            
            mFrameTicker = gameTime;
            mCurrentFrame += 1;

            if (mCurrentFrame >= mNumFrames) {
                mCurrentFrame = 0;

                if (!mIsLooping)
                    dead = true;
            }

            mSource.top = mSource.top;
            mSource.bottom = mSpriteHeight;

            mSource.left = mCurrentFrame * mSpriteWidth;
            mSource.right = mSource.left + mSpriteWidth;

        }
    }

}