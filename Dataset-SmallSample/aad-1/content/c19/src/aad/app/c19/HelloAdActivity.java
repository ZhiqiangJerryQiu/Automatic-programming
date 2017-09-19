package aad.app.c19;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import aad.app.c19.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class HelloAdActivity extends Activity implements OnClickListener {

    private static final String TAG = HelloAdActivity.class.getSimpleName();
    
    private static final String AD_UNIT_ID = "a14f320fc7ba5b4";
    
    private AdView mAdView;

    private void addAd() {
    
        // Create the adView
        mAdView = new AdView(this, AdSize.BANNER, AD_UNIT_ID);        

        mAdView.setAdListener(new AdListener() {
            
            @Override
            public void onDismissScreen(Ad arg0) {
                // TODO Auto-generated method stub                
            }

            @Override
            public void onFailedToReceiveAd(Ad ad, ErrorCode error) {

                // Hide the add if their is a problem
                mAdView.setVisibility(AdView.GONE);
            }

            @Override
            public void onLeaveApplication(Ad arg0) {
                // TODO Auto-generated method stub                
            }

            @Override
            public void onPresentScreen(Ad arg0) {
                // TODO Auto-generated method stub                
            }

            @Override
            public void onReceiveAd(Ad ad) {

                // Show the add if successful
                mAdView.setVisibility(AdView.VISIBLE);
            }
        });
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        layout.addView(mAdView);

        // Load an ad
        AdRequest ar = new AdRequest();        
        ar.setGender(AdRequest.Gender.FEMALE);
        //ar.addTestDevice("7FCEE51AE8D616077EA8B75268139FDC");
        mAdView.loadAd(ar);
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        addAd();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        
        mAdView.destroy();

    }

    @Override
    public void onClick(View v) {

    }

}