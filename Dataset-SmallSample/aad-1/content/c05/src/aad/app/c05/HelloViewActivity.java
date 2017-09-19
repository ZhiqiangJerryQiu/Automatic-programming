package aad.app.c05;

import aad.app.c05.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelloViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Display the DPI
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        
        ((TextView) this.findViewById(R.id.dpiTextView)).setText("Density: " + outMetrics.densityDpi + " Scaled density: " + outMetrics.scaledDensity);
                
        
        ((CustomButton) this.findViewById(R.id.customButton1)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // An Empty Click Handler to register for the playSoundEffect override
                
            }});
    
        ((Button) this.findViewById(R.id.animatedButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(HelloViewActivity.this, R.anim.rotate);
                v.setAnimation(animation);
                
            }
            
        });
        
        ((Button) this.findViewById(R.id.transitionButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(HelloViewActivity.this, SecondActivity.class));
                HelloViewActivity.this.overridePendingTransition(R.anim.in, R.anim.out);
                
            }
            
        });

        
        CustomView v = new CustomView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 30);
        v.setLayoutParams(params);
        ((LinearLayout) this.findViewById(R.id.mainLinearLayout)).addView(v);
        
    }
}