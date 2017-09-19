
package aad.app.c23;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HelloFacebookService extends WallpaperService {

    public static final String TAG = HelloFacebookService.class.getSimpleName();
    
    private SharedPreferences mSharedPreferences;
    private FriendsSQLiteOpenHelper mFriendsDBHelper;
    
    @Override
    public Engine onCreateEngine() {
        return new FacebookWallpaperEngine();
    }

    private class FacebookWallpaperEngine extends Engine {
        
        private final Handler handler = new Handler();
        
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                updateList();
                draw();
            }

        };

        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private boolean touchEnabled;
        
        private List<Bitmap> images;

        public FacebookWallpaperEngine() {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HelloFacebookService.this);
            
            touchEnabled = prefs.getBoolean("touch", false);
                        
            
            images = new ArrayList<Bitmap>();
            
            
            Thread t = new Thread() {

                @Override
                public void run() {
                    
                    // ReLoad the friends from the db
                    mFriendsDBHelper = new FriendsSQLiteOpenHelper(HelloFacebookService.this);
                    SQLiteDatabase rdb = mFriendsDBHelper.getReadableDatabase();
                    
                    String[] columns = { FriendsSQLiteOpenHelper.TABLE_FB_ID, FriendsSQLiteOpenHelper.TABLE_FB_NAME };
                    
                    // Randomly pull a limited number
                    Cursor c = rdb.query(FriendsSQLiteOpenHelper.TABLE_NAME, columns, null, null, null, null, "random()");
                                            
                    for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext()) {

                        String name = "";
                        try {
                            
                            name = c.getString(c.getColumnIndex(FriendsSQLiteOpenHelper.TABLE_FB_NAME));
                            //Log.d(TAG, "onVisibilityChanged() Retrieveing image for " + name);    
                            
                            int id = c.getInt(c.getColumnIndex(FriendsSQLiteOpenHelper.TABLE_FB_ID));  
                            
                            Bitmap friendBitmap = null;
                            
                            // Check to see if we have a cached version of the Bitmap image
                            String friendFileName = getExternalCacheDir().getAbsolutePath() + "/" + id + ".jpg";
                            File cachedFriendImage = new File(friendFileName);
                            if (cachedFriendImage.exists()) {
                                friendBitmap = BitmapFactory.decodeFile(cachedFriendImage.getAbsolutePath());
                            }
                            else {
                                // Go get the image
                                String friendImageURI = "http://graph.facebook.com/" + id + "/picture?type=large";                   
                                friendBitmap = BitmapFactory.decodeStream((InputStream) new URL(friendImageURI).getContent());
                                Log.d(TAG, "onVisibilityChanged() Caching image for " + name + " as " + friendFileName); 
                                
                                FileOutputStream outFile = new FileOutputStream(friendFileName);
                                friendBitmap.compress(Bitmap.CompressFormat.PNG, 80, outFile);                                    
                            }                                                              

                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            Log.w(TAG, "onVisibilityChanged() Could not find image for " + name);
                            //e.printStackTrace();
                        }
                    }
                                            
                    rdb.close();
                }
                
            };
            t.setName("FriendBitmapThread");
            t.start();
            
            
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);

        }

        
        private void updateList() {
            
            // ReLoad the friends from the db
            mFriendsDBHelper = new FriendsSQLiteOpenHelper(HelloFacebookService.this);
            SQLiteDatabase rdb = mFriendsDBHelper.getReadableDatabase();
            
            String[] columns = { FriendsSQLiteOpenHelper.TABLE_FB_ID, FriendsSQLiteOpenHelper.TABLE_FB_NAME };
            
            Cursor c = rdb.query(FriendsSQLiteOpenHelper.TABLE_NAME, columns, null, null, null, null, null);
            
            images.clear();
            
            int count = 0;
            for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext()) {
            
             // We only want to display 20 pictures at a time
                count++;
                if(count > 24)
                    break;
                
                int id = c.getInt(c.getColumnIndex(FriendsSQLiteOpenHelper.TABLE_FB_ID));  
                
                Bitmap friendBitmap = null;
                
                // Check to see if we have a cached version of the Bitmap image
                String friendFileName = getExternalCacheDir().getAbsolutePath() + "/" + id + ".jpg";
                File cachedFriendImage = new File(friendFileName);
                if (cachedFriendImage.exists()) {
                    friendBitmap = BitmapFactory.decodeFile(cachedFriendImage.getAbsolutePath());
                }
                else {
                    // Don't add anything
                    continue;
                }
                
                synchronized(images) {
                    images.add(friendBitmap);
                }
            
               
            }
            
            rdb.close();
            
        }
        
        @Override
        public void onVisibilityChanged(boolean visible) {
            
            Log.d(TAG, "onVisibilityChanged()");            
            
            this.visible = visible;
            
            if (visible) {
                handler.post(drawRunner);
            } 
            else 
            {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (touchEnabled) {

                // Handle touch there
                super.onTouchEvent(event);
            }
        }

        private void draw() {
            
            SurfaceHolder holder = getSurfaceHolder();
            
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                
                if (canvas != null) {
                                                                                
                    canvas.drawColor(Color.WHITE);
                    int x = 0;
                    int y = 0;
                    synchronized(images) {
                        for (Bitmap b : images) {
                            x = (int) (width * Math.random()) - 40;
                            y = (int) (height * Math.random()) - 20;
                            canvas.drawBitmap(b, x, y, paint);
                        }
                    }                    
                }
                
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            
            handler.removeCallbacks(drawRunner);
            
            // Recycle the drawing every 6 seconds
            if (visible) {
                handler.postDelayed(drawRunner, 6000);
            }
        }

    }

}
