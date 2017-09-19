
package aad.app.c23;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;

public class FacebookLoginActivity extends Activity {

    public static final String TAG = FacebookLoginActivity.class.getSimpleName();
    
    private Facebook mFacebook;
    private FriendsSQLiteOpenHelper mFriendsDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
        
        if(!mFacebook.isSessionValid())
            mFacebook.authorize(this, new DialogListener() {
            
                @Override
                public void onComplete(Bundle values) {
                    Log.d(TAG, "onComplete()");
                    
                    // Get the User's friends list from https://graph.facebook.com/me/friends
                    try {
                        
                        String result = mFacebook.request("me");
                        JSONObject me = new JSONObject(result);
                        
                        String name = me.getString("name");
                        
                        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("facebook_authenticated", true);
                        editor.putString("facebook_account", name);      
                        String accessToken = mFacebook.getAccessToken();
                        Log.i(TAG, "onComplete() accessToken: " + accessToken);
                        editor.putString("access_token", accessToken);
                        editor.putLong("access_expires", mFacebook.getAccessExpires());
                        editor.commit();
                        
                        Log.i(TAG, "onComplete() me name: " + name);
                        
                        result = mFacebook.request("me/friends");
                        JSONObject jObject = new JSONObject(result); 
                        JSONArray data = jObject.getJSONArray("data");
                        
                        mFriendsDBHelper = new FriendsSQLiteOpenHelper(FacebookLoginActivity.this);
                        SQLiteDatabase wdb = mFriendsDBHelper.getWritableDatabase();
                        
                        // Clean out all existing friends
                        wdb.delete(FriendsSQLiteOpenHelper.TABLE_NAME, null, null);
                        
                        ContentValues cv = new ContentValues();
                        // Add all the found friends
                        for(int i = 0 ; i < data.length(); i++) {
                            
                            JSONObject jName = data.getJSONObject(i);
                            cv.put("fb_id", jName.getInt("id"));
                            cv.put("fb_name", jName.getString("name"));
                            
                            wdb.insert(FriendsSQLiteOpenHelper.TABLE_NAME, null, cv);
                            //Log.i(TAG, "onComplete() " + jName + " " + jID);
                        }
                        
                        wdb.close();
                        
                        Log.i(TAG, "onComplete() result complete");
                        finish();
                        
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                
                }
    
                @Override
                public void onFacebookError(FacebookError error) {
                }
    
                @Override
                public void onError(DialogError e) {
                }
    
                @Override
                public void onCancel() {
                    Log.d(TAG, "onCancel()");
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult()");
        
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

}
