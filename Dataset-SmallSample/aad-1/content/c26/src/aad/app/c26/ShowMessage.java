package aad.app.c26;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowMessage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.show_message);
        
        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra("message");        
            TextView tv = (TextView) this.findViewById(R.id.messageTextView);
            tv.setText(message);
        }
    }

    
}
