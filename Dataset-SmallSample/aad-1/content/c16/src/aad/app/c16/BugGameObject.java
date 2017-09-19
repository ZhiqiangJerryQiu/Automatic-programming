package aad.app.c16;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/** Game object that represents a bug
 * 
 * @author Mike
 *
 */
public class BugGameObject implements GameObject {

    private boolean mIsAlive = true;
    private AnimatedSprite mSprite;
    private Paint textPaint;
    private int mX;
    private int mY;

    public int health = 99;
        
    public BugGameObject(Bitmap bitmap) {

        mSprite = new AnimatedSprite();
        mSprite.initialize(bitmap, 256, 256, 4, 2, true);
        
        // Setup the Paint for the health text
        textPaint = new Paint();
        textPaint.setARGB(255, 255, 0, 0);
        textPaint.setTextSize(24);
    }
    
    public boolean isAlive() {
        return mIsAlive;
    }
    
    // TODO Extract sprite size
    public boolean isHit(int x, int y) {

        // Need to offset because of our sprite draw...
        // We are also tightening the hit box by 32 pixels all around
        if (x < mX - 96 || x > mX + 96 || y < mY - 96 || y > mY + 96) {
            return false;
        }
        
        return true;
    }
    
    public void move() {
        if (mIsAlive) {
            mY = mY + 2;
        }
    }
    
    // Kill the bug
    public void splat(Bitmap splatBitmap) {      
        mIsAlive = false;
        
        
        //mSprite = new AnimatedSprite();
        mSprite.initialize(splatBitmap, 256, 256, 1, 4, false);
    }

    @Override
    public GameObjectType getType() {

        return GameObjectType.Bug;
    }

    @Override
    public AnimatedSprite getSprite() {

        return mSprite;
    }

    @Override
    public void draw(Canvas canvas) {
        
        mSprite.draw(canvas, mX, mY);
        
        // Draw the health text
        if (mIsAlive)
            canvas.drawText(String.valueOf(health), mX - 12, mY, textPaint);
        
    }

    @Override
    public void update(long gameTime) {

        mSprite.update(gameTime);
    }

    @Override
    public int getX() {

        return mX;
    }

    @Override
    public int getY() {

        return mY;
    }

    @Override
    public void setX(int x) {

        mX = x;

    }

    @Override
    public void setY(int y) {

        mY = y;

    }

}
