package aad.app.c05;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;


public class CustomButton extends Button {
    
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    public CustomButton(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public CustomButton(Context context) {

        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        
        Paint circlePaint = new Paint(); 
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(10, 10, 20, circlePaint);
        super.onDraw(canvas);       
        canvas.drawText("Custom", 10, 14, new Paint());          
    }

    @Override
    public void playSoundEffect(int soundConstant) {
        Log.i("CustomButton", "playSoundEffect()");
        MediaPlayer player = MediaPlayer.create(getContext(), Settings.System.DEFAULT_NOTIFICATION_URI);
        player.start();
    }
    
}
