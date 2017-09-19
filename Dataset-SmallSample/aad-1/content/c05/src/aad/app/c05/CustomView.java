package aad.app.c05;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;


public class CustomView extends View {
    
    public CustomView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public CustomView(Context context, AttributeSet attrs) {

        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomView(Context context) {

        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        
        canvas.drawColor(Color.BLUE);
    }

    
}
