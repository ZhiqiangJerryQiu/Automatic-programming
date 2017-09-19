package aad.app.c08;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListActivity extends ListActivity implements OnItemClickListener {

    private String[] mProjection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER };

    private class ContactAdapter extends SimpleCursorAdapter implements OnClickListener, OnItemClickListener {

        private Context mContext;
        private Cursor mCursor;
        
        public ContactAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {

            super(context, layout, c, from, to);
            this.mCursor = c;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.contact_list_view_item, null);
            }
            
            parent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            
            mCursor.moveToPosition(position);
            String displayName = mCursor.getString(1); // Get the Display Name
            if (displayName != null) {

                TextView displayNameTextView = (TextView) v.findViewById(R.id.contactDisplayName);
                if (displayNameTextView != null)
                    displayNameTextView.setText(displayName);

            }

            String hasPhone = mCursor.getString(2); // Get the Display Name
            if (hasPhone != null) {

                ImageButton phoneImageButton = (ImageButton) v.findViewById(R.id.phoneImageButton);
                if (hasPhone.equalsIgnoreCase("1")) {
                    phoneImageButton.setEnabled(true);
                    phoneImageButton.setOnClickListener(this);
                }
                else
                    phoneImageButton.setEnabled(false);

            }
            return v;
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(mContext, "Phone Button Clicked", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            String location = "position: " + position + "id: " + id;
            Toast.makeText(mContext, location, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Need android.permission.READ_CONTACTS

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED DESC";
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, sortOrder);

        startManagingCursor(cursor);      

        // Columns from the data to bind
        String[] from = new String[] { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER };

        // The views to which the data will be bound
        int[] to = new int[] { R.id.contactDisplayName };

        ContactAdapter contactAdapter = new ContactAdapter(this, R.layout.contact_list_view_item, cursor, from, to);
        this.setListAdapter(contactAdapter);
        
        this.getListView().setOnItemClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        String location = "position: " + position + " id: " + id;
        Toast.makeText(this, location, Toast.LENGTH_LONG).show();
    }


}
