package aad.app.c25;

import java.util.List;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import aad.app.c25.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Twitter4JActivity extends Activity {

    private static final String TAG = "Twitter4JActivity";

    private static final String OAUTH_CALLBACK_SCHEME = "oauth-hello-twitter4j";
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

        Log.d("HelloTwitter", "Twitter4JActivity:readTweets()");

        ConfigurationBuilder cb = new ConfigurationBuilder();
        try {
            cb.setDebugEnabled(true).setOAuthConsumerKey(mConsumerKey).setOAuthConsumerSecret(mConsumerSecret).setOAuthAccessToken(mAccessKey).setOAuthAccessTokenSecret(mAccessSecret);

            // Get our tweets
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            Paging paging = new Paging(1, 5);
            List<Status> statuses;

            statuses = twitter.getUserTimeline("uwaad_test", paging);
            mTweets.clear();
            for (Status s : statuses) {
                mTweets.add(s.getText());
            }

            mTweets.notifyDataSetChanged();
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    /** Send a tweet */
    private void sendTweet(String tweet) {

        Log.d("HelloTwitter", "Twitter4JActivity:sendTweet()");

        ConfigurationBuilder cb = new ConfigurationBuilder();
        try {
            cb.setDebugEnabled(true).setOAuthConsumerKey(mConsumerKey).setOAuthConsumerSecret(mConsumerSecret).setOAuthAccessToken(mAccessKey).setOAuthAccessTokenSecret(mAccessSecret);

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            twitter.updateStatus(tweet);
        }
        catch (TwitterException e) {

            e.printStackTrace();
        }
    }

    // Check to see if we have stored authorization credentials
    private boolean isAuthorized(String prefsFile) {

        Log.d(TAG, "Twitter4JActivity:isAuthorized()");

        SharedPreferences sp = this.getSharedPreferences(prefsFile, MODE_PRIVATE);
        mAccessKey = sp.getString(Constants.KEY_ACCESS_KEY, null);
        mAccessSecret = sp.getString(Constants.KEY_ACCESS_SECRET, null);

        if (mAccessKey != null && mAccessKey != null) {

            Log.d(TAG, "Twitter4JActivity:isAuthorized() Existing ACCESS_KEY: " + mAccessKey);
            Log.d(TAG, "Twitter4JActivity:isAuthorized() Existing ACCESS_SECRET: " + mAccessSecret);

            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("HelloTwitter", "Twitter4JActivity:onCreate()");
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

        Log.d(TAG, "Twitter4JActivity:onResume()");

        Uri uri = this.getIntent().getData();

        if (uri != null && uri.toString().startsWith(OAUTH_CALLBACK_URL)) {
            Log.d(TAG, "Twitter4JActivity:onResume() uri: " + uri.toString());

            String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
            if (verifier == null) {
                Log.e(TAG, "Twitter4JActivity:onResume() Verifier is null - There was a problem");
                return;
            }

            try {

                mProvider.retrieveAccessToken(mConsumer, verifier);
                mAccessKey = mConsumer.getToken();
                mAccessSecret = mConsumer.getTokenSecret();

                // We have the keys we need - store them locally

                SharedPreferences sp = this.getSharedPreferences(Constants.PREF_FILE_TWITTER4J, MODE_PRIVATE);
                Editor editor = sp.edit();
                editor.putString(Constants.KEY_ACCESS_KEY, mAccessKey);
                editor.putString(Constants.KEY_ACCESS_SECRET, mAccessSecret);
                editor.commit();

                Log.d(TAG, "Twitter4JActivity:onResume() New ACCESS_KEY: " + mAccessKey);
                Log.d(TAG, "Twitter4JActivity:onResume() New ACCESS_SECRET: " + mAccessSecret);

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

            // Update to show the compose option
            updateUI();
        }
        else {
            Log.d(TAG, "Twitter4JActivity:onResume() Not a OAUTH Callback");
        }
    }

    private void updateUI() {

        Log.d("HelloTwitter", "Twitter4JActivity:updateUI()");

        if (isAuthorized(Constants.PREF_FILE_TWITTER4J)) {

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

                    Log.d(TAG, "Twitter4JActivity:mAuthorizeButton:onClick()");

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
