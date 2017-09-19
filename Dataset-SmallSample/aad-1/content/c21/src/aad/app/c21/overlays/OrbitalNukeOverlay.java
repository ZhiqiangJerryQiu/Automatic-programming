
package aad.app.c21.overlays;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

public class OrbitalNukeOverlay extends ItemizedOverlay<OverlayItem> {

    private static final String TAG = OrbitalNukeOverlay.class.getSimpleName();

    private ArrayList<NukeOverlayItem> mItemList = new ArrayList<NukeOverlayItem>();

    private Drawable mNukeMarker;
    private Drawable mNukeExplosionMarker;
    
    class NukeOverlayItem extends OverlayItem {

        Drawable mNuke = null;
        Drawable mNukeExplosion = null;

        boolean isExploded = false;

        NukeOverlayItem(GeoPoint gp, String name, String snippet, Drawable nuke, Drawable nukeExplosion) {
            super(gp, name, snippet);

            this.mNuke = nuke;
            this.mNukeExplosion = nukeExplosion;
        }

        @Override
        public Drawable getMarker(int stateBitset) {
            Drawable result = (isExploded ? mNukeExplosion : mNuke);
            setState(result, stateBitset);
            return (result);
        }

        void explode() {
            Log.d(TAG, "NukeOverlayItem:explode()");
            isExploded = true;
        }
    }

    public OrbitalNukeOverlay(Drawable nukeMarker, Drawable nukeExplosionMarker) {
        super(boundCenterBottom(nukeMarker));
        
        mNukeMarker = nukeMarker;
        mNukeExplosionMarker = nukeExplosionMarker;
        
        populate();
    }

    public void addItem(GeoPoint p, String title, String snippet, Drawable nuke, Drawable nukeExplosion) {
        NukeOverlayItem newItem = new NukeOverlayItem(p, title, snippet, nuke, nukeExplosion);
        mItemList.add(newItem);
        populate();
    }

    public void nuke() {
        
        for (NukeOverlayItem item : mItemList) {
            item.explode();
        }
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
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, false);
    }

    @Override
    public boolean onTap(final GeoPoint p, final MapView mapView) {

        Log.d(TAG, "onTap() Nuke");

        addItem(p, "Nuke", "", mNukeMarker, mNukeExplosionMarker);

        // We handle the tap here
        return true;
    }

    @Override
    protected boolean onTap(int index) {
        return true;
    }
}
