
package aad.app.c21.overlays;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class LocationOverlay extends ItemizedOverlay<OverlayItem> {
    
    private static final String TAG = LocationOverlay.class.getSimpleName();
    
    private ArrayList<OverlayItem> mItemList = new ArrayList<OverlayItem>();
    private int mTappedIndex = 0;
    
    public LocationOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
        populate();
    }

    public void addItem(GeoPoint p, String title, String snippet) {
        OverlayItem newItem = new OverlayItem(p, title, snippet);
        mItemList.add(newItem);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public int size() {
        return mItemList.size();
    }

    @Override
    public boolean onTap(final GeoPoint p, final MapView mapView) {
        boolean tapped = super.onTap(p, mapView);
        Log.d(TAG, "onTap() Location");
        if (tapped) {
            
            OverlayItem item = mItemList.get(mTappedIndex);
            Toast.makeText(mapView.getContext(), item.getTitle(), Toast.LENGTH_LONG).show();            
        }
        else {
            // It is important to return false when not handling a tap
            // - So that other overlays might be able to respond to the tap
            return false;
        }
        
        return true;
    }

    @Override
    protected boolean onTap(int index) {
        mTappedIndex = index;
        return true;
    }
}
