package aad.app.c16;

import android.graphics.Canvas;

/** The public interface for our GameObject
 * 
 * @author Mike
 *
 */
public interface GameObject {
        
        public GameObjectType getType();
        public AnimatedSprite getSprite();
        public void draw(Canvas canvas);
        public void update(long gameTime);
        public int getX();
        public int getY();
        public void setX(int x);
        public void setY(int y);
        
    }
