
package aad.app.c26;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class SendMessageActivity extends Activity implements OnClickListener {

    public static final String TAG = SendMessageActivity.class.getSimpleName();

    private Account mAccount;
    
//    private void authenticate() {
//        
//        Account account = new Account(name, "com.google");
//        AccountManagerFuture<Bundle> accountFuture = AccountManager.get(this).getAuthToken(account, "ah", null, activity, null, null);
//        Bundle authTokenBundle = accountFuture.getResult();
//        String authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
//    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);
        
        Bundle b = this.getIntent().getExtras();
        mAccount = (Account) b.get("account");
        
        if (mAccount == null) {
            Log.e(TAG, "onCreate() Account was NULL");
            this.finish();
        }

        this.findViewById(R.id.messageButton).setOnClickListener(this);
    }

    private static class CustomizedHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    private void sendMessage(String message) {

        //AccountManager.get(this).getAuthToken(mAccount, SCOPE, null, this, cb, null);
//
//        private final static String AUTH = "authentication";
//        private static final String UPDATE_CLIENT_AUTH = "Update-Client-Auth";
//        public static final String PARAM_REGISTRATION_ID = "registration_id";
//        public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";
//        public static final String PARAM_COLLAPSE_KEY = "collapse_key";
//        private static final String UTF8 = "UTF-8";
//
//        // public static int sendMessage(String auth_token, String
//        // registrationId, String message) throws IOException {
//
//        StringBuilder postDataBuilder = new StringBuilder();
//        postDataBuilder.append(PARAM_REGISTRATION_ID).append("=").append(registrationId);
//        postDataBuilder.append("&").append(PARAM_COLLAPSE_KEY).append("=").append("0");
//        postDataBuilder.append("&").append("data.payload").append("=").append(URLEncoder.encode(message, UTF8));
//
//        byte[] postData = postDataBuilder.toString().getBytes(UTF8);
//
//        URL url = new URL("https://android.clients.google.com/c2dm/send");
//        HttpsURLConnection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
//        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//        conn.setDoOutput(true);
//        conn.setUseCaches(false);
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
//        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + auth_token);
//
//        OutputStream out = conn.getOutputStream();
//        out.write(postData);
//        out.close();
//
//        int responseCode = conn.getResponseCode();
//        return responseCode;

    }

    @Override
    public void onClick(View v) {
        
        EditText messageEditText = (EditText) this.findViewById(R.id.messageEditText);
        String message = messageEditText.getText().toString();
        
        sendMessage(message);        
    }

}
