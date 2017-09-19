package aad.app.c25;

import aad.app.c25.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelloTwitterActivity extends Activity {

    private Button mButtonJTwitter;
    private Button mButtonTwitter4J;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mButtonJTwitter = (Button) this.findViewById(R.id.buttonJTwitter);
        mButtonJTwitter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startTwitterActivity(JTwitterActivity.class);
            }
        });

        mButtonTwitter4J = (Button) this.findViewById(R.id.buttonTwitter4J);
        mButtonTwitter4J.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startTwitterActivity(Twitter4JActivity.class);
            }
        });

        // $LEARN A different (anonymous) method of setting the OnClickListener
        ((Button) this.findViewById(R.id.buttonClearAuthorizations)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences(Constants.PREF_FILE_JTWITTER, MODE_PRIVATE);
                sp.edit().clear().commit();

                sp = getSharedPreferences(Constants.PREF_FILE_TWITTER4J, MODE_PRIVATE);
                sp.edit().clear().commit();
            }
        });
    }

    /** Activity start utility function */
    private void startTwitterActivity(Class<?> c) {

        Intent i = new Intent(this, c);
        this.startActivity(i);
    }

}