package aad.app.c16;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;


public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.game_view);
        
        // Need API Level 11 or higher
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    
    
}
