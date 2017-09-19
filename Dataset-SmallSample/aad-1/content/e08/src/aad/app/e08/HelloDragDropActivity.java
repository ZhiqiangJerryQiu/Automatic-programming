package aad.app.e08;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class HelloDragDropActivity extends Activity implements OnDragListener, OnLongClickListener {

    private static final String TAG = HelloDragDropActivity.class.getSimpleName();

    private static class CustomDragShadowBuilder extends View.DragShadowBuilder {

        private BitmapDrawable mBitmapDrawable;
        private static Drawable shadow;

        public CustomDragShadowBuilder(View v) {

            super(v);
            
            v.setDrawingCacheEnabled(true);
            mBitmapDrawable = new BitmapDrawable(v.getResources(), v.getDrawingCache());
        }

        @Override
        public void onDrawShadow(Canvas canvas) {

            mBitmapDrawable.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {

            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);

            // Double the height and width
            int height = getView().getHeight() * 2;
            int width = getView().getWidth() * 2;            
            mBitmapDrawable.setBounds(0, 0, width, height);
            shadowSize.set(width, height);
            
            // Set the touch point to the middle
            shadowTouchPoint.set(width / 2, height / 2);
        }

    }
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Setup the basket
        ImageView basketImageView = (ImageView) this.findViewById(R.id.basketImageView);
        basketImageView.setOnDragListener(this);
        
        
        // Add an onClickListener to all the children
        LinearLayout itemsLinearLayout = (LinearLayout) this.findViewById(R.id.itemsLinearLayout);
        int childCount = itemsLinearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {

            itemsLinearLayout.getChildAt(i).setOnLongClickListener(this);
            itemsLinearLayout.getChildAt(i).setOnDragListener(this);            
        }
        
    }
    
    @Override
    public boolean onDrag(View v, DragEvent event) {

        // Defines a variable to store the action type for the incoming event
        final int action = event.getAction();
        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                //Log.d(TAG, "onDrag() ACTION_DRAG_STARTED");
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "onDrag() ACTION_DRAG_ENTERED");
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                //Log.d(TAG, "onDrag() ACTION_DRAG_LOCATION");
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "onDrag() ACTION_DRAG_EXITED");
                return true;

            case DragEvent.ACTION_DROP:
                Log.d(TAG, "onDrag() ACTION_DROP");
                
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);
                
                String sku = item.getText().toString();
                
                if (v.getId() == R.id.basketImageView) {
                    Toast.makeText(this, "Dropped " + sku, Toast.LENGTH_LONG).show();    
                }
                

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                //Log.d(TAG, "onDrag() ACTION_DRAG_ENDED");
                return true;

                // An unknown action type was received.
            default:
                Log.e(TAG, "onDrag() Unknown DragEvent");

                break;
        }
        
        Log.d(TAG, "onDrag() Exit with false");
        return false;
    }
    
    @Override
    public boolean onLongClick(View v) {
     
        // Get the item SKU
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

        String[] mimeType = { "text/plain" };
        ClipData clipData = new ClipData((CharSequence) v.getTag(), mimeType, item);

        // Create a custom image representation for when dragging
        DragShadowBuilder imageShadow = new CustomDragShadowBuilder(v);
        
        v.startDrag(clipData, imageShadow, null, 0);

        return true;
    }


}
