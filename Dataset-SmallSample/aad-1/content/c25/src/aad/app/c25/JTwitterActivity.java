package aad.app.c25;

import java.util.List;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;

import aad.app.c25.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class JTwitterActivity extends Activity {

    private static final String TAG = "JTwitterActivity";

    private static final String OAUTH_CALLBACK_SCHEME = "oauth-hello-jtwitter";
    private static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://callback";

    private Button mAuthorizeButton;
    private Button mComposeButton;
    private ListView mFeedLayout;

    private static String mAccessKey = null;
    private static String mAccessSecret = null;

    private static String mConsumerKey = "7Wstd5pszqa0FnkToFoI3g";
    private static String mConsumerSecret = "HX5iOJBv8P0PTNJqTMsN2Xo0A0Acghq7aJ56yh9M0";
    private static CommonsHttpOAuthConsumer mConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mConsumerSecret);
    private static CommonsHttpOAuthProvider mProvider = new CommonsHttpOAuthProvider("https://api.twitter.com/oauth/request_token", "https://api.twitter.com/oauth/access_token", "https://api.twitter.com/oauth/authorize");

    private ArrayAdapter<String> mTweets;

    /** Read tweets */
    private void readTweets() {

        Log.d("HelloTwitter", "JTwitterActivity:readTweets()");
        
        OAuthSignpostClient client = new OAuthSignpostClient(mConsumerKey, mConsumerSecret, mAccessKey, mAccessSecret);
        Twitter twitter = new Twitter("C14-JTwitter", client);

        List<Status> statuses;

        statuses = twitter.getUserTimeline();
        mTweets.clear();
        for (Status s : statuses) {
            mTweets.add(s.getText());
        }
        
        mTweets.notifyDataSetChanged();
    }

    /** Send a tweet */
    private void sendTweet(String tweet) {

        Log.d("HelloTwitter", "JTwitterActivity:sendTweet()");
        
        try {
            OAuthSignpostClient client = new OAuthSignpostClient(mConsumerKey, mConsumerSecret, mAccessKey, mAccessSecret);
            Twitter twitter = new Twitter("C14-JTwitter", client);

            twitter.setStatus(tweet);
        }
        catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);

            e.printStackTrace();
        }

    }

    // Check to see if we have stored authorization credentials
    private boolean isAuthorized(String prefsFile) {

        Log.d(TAG, "JTwitterActivity:isAuthorized()");

        SharedPreferences sp = this.getSharedPreferences(prefsFile, MODE_PRIVATE);
        mAccessKey = sp.getString(Constants.KEY_ACCESS_KEY, null);
        mAccessSecret = sp.getString(Constants.KEY_ACCESS_SECRET, null);

        if (mAccessKey != null && mAccessKey != null) {

            Log.d(TAG, "JTwitterActivity:isAuthorized() Existing ACCESS_KEY: " + mAccessKey);
            Log.d(TAG, "JTwitterActivity:isAuthorized() Existing ACCESS_SECRET: " + mAccessSecret);

            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "JTwitterActivity:onCreate()");
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.feed_layout);

        mTweets = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mFeedLayout = (ListView) this.findViewById(R.id.feedLayout);
        mFeedLayout.setAdapter(mTweets);
        mAuthorizeButton = (Button) this.findViewById(R.id.authorizeButton);
        mComposeButton = (Button) this.findViewById(R.id.tweetButton);
        mComposeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText tweetEditText = (EditText) findViewById(R.id.tweetEditText);
                String tweetString = tweetEditText.getText().toString();

                sendTweet(tweetString);
                updateUI(); // This is being called too soon and not picking up the change... need to thread out the update
            }
        });

        updateUI();
    }

    @Override
    public void onResume() {

        super.onResume();

        Log.d(TAG, "JTwitterActivity:onResume()");

        Uri uri = this.getIntent().getData();

        if (uri != null && uri.toString().startsWith(OAUTH_CALLBACK_URL)) {
            Log.d(TAG, "JTwitterActivity:onResume() uri: " + uri.toString());

            // 12-05 16:48:11.610: D/HelloTwitter(12269):
            // oauth-hello-jtwitter://callback?denied=FWSAs7HbJSz2bFSWE8C9deTTN1DqI0EaGVj1R5TMM4

            String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
            if (verifier == null) {
                Log.e(TAG, "JTwitterActivity:onResume() Verifier is null - There was a problem");
                return;
            }

            try {

                mProvider.retrieveAccessToken(mConsumer, verifier);
                mAccessKey = mConsumer.getToken();
                mAccessSecret = mConsumer.getTokenSecret();

                // We have the keys we need - store them locally

                SharedPreferences sp = this.getSharedPreferences(Constants.PREF_FILE_JTWITTER, MODE_PRIVATE);
                Editor editor = sp.edit();
                editor.putString(Constants.KEY_ACCESS_KEY, mAccessKey);
                editor.putString(Constants.KEY_ACCESS_SECRET, mAccessSecret);
                editor.commit();

                Log.d(TAG, "JTwitterActivity:onResume() New ACCESS_KEY: " + mAccessKey);
                Log.d(TAG, "JTwitterActivity:onResume() New ACCESS_SECRET: " + mAccessSecret);

            }
            catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            }
            catch (OAuthNotAuthorizedException e) {
                e.printStackTrace();
            }
            catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            }
            catch (OAuthCommunicationException e) {
                e.printStackTrace();
            }

            updateUI();
        }
        else {
            Log.d(TAG, "JTwitterActivity:onResume() Not a OAUTH Callback");
        }
    }

    class OAuthAuthorizeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            Log.d(TAG, "OAuthAuthorizeTask:doInBackground()");

            String authUrl;
            String message = null;
            try {
                authUrl = mProvider.retrieveRequestToken(mConsumer, OAUTH_CALLBACK_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
                startActivity(intent);
            }
            catch (OAuthMessageSignerException e) {
                message = "OAuthMessageSignerException";
                e.printStackTrace();
            }
            catch (OAuthNotAuthorizedException e) {
                message = "OAuthNotAuthorizedException";
                e.printStackTrace();
            }
            catch (OAuthExpectationFailedException e) {
                message = "OAuthExpectationFailedException";
                e.printStackTrace();
            }
            catch (OAuthCommunicationException e) {
                message = "OAuthCommunicationException";
                e.printStackTrace();
            }
            return message;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            Log.d(TAG, "OAuthAuthorizeTask:onPostExecute()");

            if (result != null) {
                Toast.makeText(JTwitterActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateUI() {

        if (isAuthorized(Constants.PREF_FILE_JTWITTER)) {

            // Hide the authorization button
            mAuthorizeButton.setVisibility(View.GONE);

            readTweets();
            
            // Show the content layout
            this.findViewById(R.id.contentLinearLayout).setVisibility(View.VISIBLE);
        }
        else {

            mAuthorizeButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Log.d(TAG, "JTwitterActivity:mAuthorizeButton:onClick()");

                    try {
                        String authURL = mProvider.retrieveRequestToken(mConsumer, OAUTH_CALLBACK_URL);

                        Log.d(TAG, "Authorization authURL: " + authURL);
                        Intent authActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(authURL));
                        authActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // We don't want to navigate through the
                                                                                // activity
                        startActivity(authActivity);
                    }
                    catch (OAuthMessageSignerException e) {
                        e.printStackTrace();
                    }
                    catch (OAuthNotAuthorizedException e) {
                        e.printStackTrace();
                    }
                    catch (OAuthExpectationFailedException e) {
                        e.printStackTrace();
                    }
                    catch (OAuthCommunicationException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

}
