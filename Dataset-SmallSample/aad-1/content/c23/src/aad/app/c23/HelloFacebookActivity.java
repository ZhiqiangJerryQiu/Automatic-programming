
package aad.app.c23;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class HelloFacebookActivity extends Activity implements OnClickListener, DialogListener {

    public static final String TAG = HelloFacebookActivity.class.getSimpleName();
    
    private static final int POST_REQUEST = 1;
    private static final int TV_REQUEST = 2;
    private Facebook mFacebook;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.control);
        
        this.findViewById(R.id.postButton).setOnClickListener(this);
        this.findViewById(R.id.readButton).setOnClickListener(this); 
        
        // Check to see if we have an access token
        mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
    }   
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        
        mSharedPreferences = getSharedPreferences("FacebookLoginActivity", MODE_PRIVATE);
        String access_token = mSharedPreferences.getString("access_token", null);
        Log.d(TAG, "onActivityResult() access_token: " + access_token);
        long expires = mSharedPreferences.getLong("access_expires", 0);
        
        if(access_token != null) {
            mFacebook.setAccessToken(access_token);
        }
        
        if(expires != 0) {
            mFacebook.setAccessExpires(expires);
        }
        
        if (requestCode == POST_REQUEST) {            
            showPost();
        }

        if (requestCode == TV_REQUEST) {            
            showTV();
        }
        
        super.onActivityResult(requestCode, resultCode, data);        
    }

    @Override
    public void onClick(View view) {
       
        String accessToken = mFacebook.getAccessToken();
        Log.i(TAG, "onClick() accessToken: " + accessToken);
        
        if (view.getId() == R.id.postButton) {              
            if (accessToken != null)
                showPost();
            else {
                Intent intent = new Intent(this, FacebookLoginActivity.class);
                this.startActivityForResult(intent, POST_REQUEST);
            }
        } else {
            if (accessToken != null)
                showTV();
            else {
                Intent intent = new Intent(this, FacebookLoginActivity.class);
                this.startActivityForResult(intent, TV_REQUEST);
            }
        }
        
    }

    @Override
    public void onComplete(Bundle values) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void onFacebookError(FacebookError e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void onError(DialogError e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void onCancel() {
        // TODO Auto-generated method stub
        
    }
    
    private void showPost() {
        Log.d(TAG, "showPost()");
        try {
            Bundle parameters = new Bundle();
            //parameters.putString("message", "This is a test message");
            mFacebook.dialog(this, "stream.publish", parameters, this);           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Handler mResponseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TextView tv = (TextView) findViewById(R.id.readTextView);
            tv.setText((String) msg.obj);
            super.handleMessage(msg);
        }         
    };
    
    private void showTV() {
        Log.d(TAG, "showTV()");
        
        AsyncFacebookRunner facebookRunner = new AsyncFacebookRunner(mFacebook);
        facebookRunner.request("me/television", new RequestListener(){

            @Override
            public void onComplete(String response, Object state) {
                // TODO Auto-generated method stub
                Message message = Message.obtain();
                message.obj = response;
                mResponseHandler.sendMessage(message);
            }

            @Override
            public void onIOException(IOException e, Object state) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e, Object state) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onMalformedURLException(MalformedURLException e, Object state) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
                // TODO Auto-generated method stub
                
            }});
    }

}
