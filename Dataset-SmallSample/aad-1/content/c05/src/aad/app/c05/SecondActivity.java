package aad.app.c05;

import android.app.Activity;
import android.os.Bundle;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.second);
    }

    @Override
    public void finish() {
        super.finish();        
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    
    
}
