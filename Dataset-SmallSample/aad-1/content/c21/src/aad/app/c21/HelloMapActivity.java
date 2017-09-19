
package aad.app.c21;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;

import aad.app.c21.overlays.*;

public class HelloMapActivity extends MapActivity implements OnClickListener {

    private static final String TAG = HelloMapActivity.class.getSimpleName();

    private LocationManager mLocationManager;
    private MapView mMapView;
    private OrbitalNukeOverlay mOrbitalNukeOverlay;
    private LocationOverlay mLocationOverlay;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mMapView = (MapView) this.findViewById(R.id.mapView);
        mMapView.setBuiltInZoomControls(true);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getAllProviders();

        // List out our available providers
        for (String provider : providers) {
            Log.i(TAG, "onCreate() provider: " + provider);
        }        
        
        this.findViewById(R.id.findMeButton).setOnClickListener(this);
        this.findViewById(R.id.nukeButton).setOnClickListener(this);
               
        Drawable nukeMarker = getResources().getDrawable(R.drawable.nuke);
        nukeMarker.setBounds(0, 0, nukeMarker.getIntrinsicWidth(), nukeMarker.getIntrinsicHeight());
        Drawable nukeExplosionMarker = getResources().getDrawable(R.drawable.nuke_explosion);
        // Center via bounds
        nukeExplosionMarker.setBounds(-nukeExplosionMarker.getIntrinsicWidth(), -nukeExplosionMarker.getIntrinsicHeight(), nukeExplosionMarker.getIntrinsicWidth(), nukeExplosionMarker.getIntrinsicHeight());
                
        Drawable findMeMarker = getResources().getDrawable(R.drawable.down_arrow);
        findMeMarker.setBounds(0, 0, findMeMarker.getIntrinsicWidth(), findMeMarker.getIntrinsicHeight());
          
        mOrbitalNukeOverlay = new OrbitalNukeOverlay(nukeMarker, nukeExplosionMarker);
        mMapView.getOverlays().add(mOrbitalNukeOverlay);
        
        mLocationOverlay = new LocationOverlay(findMeMarker);
        mMapView.getOverlays().add(mLocationOverlay);
        
    }
    
    protected void findMe() {
        
        // Get GPS provider to get information about it
        LocationProvider lp = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);

        // Get the location of the device
        Location l = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        // Need to convert to microdegrees for the GeoPoint
        GeoPoint gp = new GeoPoint((int)(l.getLatitude() * 1e6), (int)(l.getLongitude() * 1e6));
        moveToGeoPoint(gp);
        
        mLocationOverlay.addItem(gp, "This is where you are", "");
    }
    
    protected void nuke() {

        Log.d(TAG, "nuke()");
        
        mOrbitalNukeOverlay.nuke();
        mMapView.invalidate();
        
        MediaPlayer mp = MediaPlayer.create(this, R.raw.nuke);
        mp.start();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void moveToGeoPoint(GeoPoint centerGeoPoint)
    {
        mMapView.getController().animateTo(centerGeoPoint);
        mMapView.getController().setZoom(16);        
    }

    @Override
    public void onClick(View view) {
       
        switch (view.getId()) {
        
            case R.id.findMeButton:
                findMe();
                break;

            case R.id.nukeButton:
                nuke();
                break;
        }
    };

}
