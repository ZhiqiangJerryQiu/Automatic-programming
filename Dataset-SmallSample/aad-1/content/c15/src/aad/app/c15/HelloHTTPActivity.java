package aad.app.c15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import aad.app.c15.R;
import android.app.Activity;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HelloHTTPActivity extends Activity implements OnClickListener {

    private static final String TAG = HelloHTTPActivity.class.getSimpleName();
    
    private static String USER_AGENT = "HelloHTTP/1.0";

    private AndroidHttpClient mClient;
    private Context mContext;
    private Button mButtonGet;
    private TextView mHeaderTextView;
    private TextView mContentTextView;
    private String mURLString;
        
    // Handler for updating the UI
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            String header = msg.getData().getString("header");
            if (header != null)
                mHeaderTextView.setText(header);
            
            String text = msg.getData().getString("body");
            if (text != null)
                mContentTextView.setText(text);
        }                
    };
    
    
    // Some example class definitions for deserializing the JSON
    class Book {
        String title;
        String isbn10;
    }
    
    class Record {
        Book book;
    }
    
    private StringBuilder mXMLBuilder;
    
    // DefaultHandler implementation
    public class SAXHandler extends DefaultHandler {

        public void startDocument() throws SAXException {
            mXMLBuilder.append("startDocument()\n");
        }

        public void endDocument() throws SAXException {
            mXMLBuilder.append("endDocument()\n");
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            mXMLBuilder.append("startElement() " + qName + "\n");
            
            for (int i = 0; i < attributes.getLength(); i++) {
                mXMLBuilder.append(attributes.getQName(i) + " : " + attributes.getValue(i) + "\n");
            }            
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            mXMLBuilder.append("endElement() " + qName + "\n");
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            mXMLBuilder.append("characters() " + new String(ch, start, length) + "\n");
        }
    }
    
    /** An AsycTask used to update the retrieved HTTP header and content displays */
    private class GetAsyncTask extends AsyncTask<Void, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(Void... params) {
            // Single call to get the response
            return getResponse();
        }

        /**
         * Handles the HttpResponse after returned. 
         * */
        @Override
        protected void onPostExecute(HttpResponse response) {

            if (response == null) {
                Log.e(TAG, "Error accessing: " + mURLString);
                Toast.makeText(mContext, "Error accessing: " + mURLString, Toast.LENGTH_LONG).show();
                return;
            }
            
            // Get the content
            BufferedReader bf;
            StringBuilder sb = new StringBuilder();
            try {
                bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 8192);
                sb.setLength(0); // Reuse the StringBuilder
                String line;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                bf.close();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
                        
            CheckBox jsonCheckBox = (CheckBox) findViewById(R.id.jsonCheckBox);
            CheckBox xmlCheckBox = (CheckBox) findViewById(R.id.xmlCheckBox);
            
            if (jsonCheckBox.isChecked()) {                
                Log.d(TAG, "onPostExecute() Parsing JSON");
                
                // Deserialize the JSON content
                Gson gson = new Gson();
                try {
                    String jsonString = gson.toJson(new Record());
                    Record r = gson.fromJson(sb.toString(), Record.class);
                    String text = "The book is titled " + r.book.title + " and its isbn10 is " + r.book.isbn10;
                    setText(null, text);
                }
                catch (JsonSyntaxException e) {
                    Log.e(TAG, e.getMessage());
                }                

            } 
            else if (xmlCheckBox.isChecked()) {                
                Log.d(TAG, "onPostExecute() Parsing XML");
                
                mXMLBuilder = new StringBuilder();
                
                // Deserialize the XML content
                SAXHandler handler = new SAXHandler();
                try {
                    Xml.parse(sb.toString(), handler);
                }
                catch (SAXException e) {
                    e.printStackTrace();
                }  
                
                setText(null, mXMLBuilder.toString());
            }               
            else {

                // Set the text of the Content
                String body = sb.toString();                                
                
                Header[] headers = response.getAllHeaders();           
                for (Header h : headers) {
                    sb.append(h.getName() + ":" + h.getValue() + "\n");
                }
    
                // Set the text of the Header
                mHeaderTextView.setText(sb.toString());
                
                String header = sb.toString();
                setText(header, body);
            }
            
            mClient.close();
            super.onPostExecute(response);
        }
        
        // Call back to the UI thread to update the textual content
        private void setText(String header, String body) {
            
            Bundle data = new Bundle();
            data.putString("header", header);
            data.putString("body", body);
            Message msg = new Message();
            msg.setData(data);
            mHandler.sendMessage(msg);
        }
    }
    
    /** Get the response */
    private HttpResponse getResponse() {

        mURLString = ((EditText) this.findViewById(R.id.urlEditText)).getText().toString();

        mClient = AndroidHttpClient.newInstance(USER_AGENT);

        // Make a GET request and execute it to return the response 
        HttpGet request = new HttpGet(mURLString);
        try {
            return mClient.execute(request);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mContext = this;
        
        mButtonGet = (Button) this.findViewById(R.id.buttonGet);
        mButtonGet.setOnClickListener(this);

        mHeaderTextView = (TextView) this.findViewById(R.id.headerTextView);
        mContentTextView = (TextView) this.findViewById(R.id.contentTextView);
    }

    /** Handle the click from the Get button */
    @Override
    public void onClick(View v) {

        // Anonymous GetAsyncTask
        new GetAsyncTask().execute();
    }

}