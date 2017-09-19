
package aad.app.e02;

import aad.app.e02.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class HelloBluetoothActivity extends Activity {

    private static final String TAG = HelloBluetoothActivity.class.getSimpleName();
    
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private TextView mMainTextView;
    private ListView mMainListView;

    private ArrayAdapter<BluetoothDevice> mDiscoveredDevices;
    
    private UUID mUUID = UUID.randomUUID();

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        
        public void onReceive(Context context, Intent intent) {
            
            String action = intent.getAction();
            
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDiscoveredDevices.add(device);
                mMainTextView.append("Discovered Device: " + device.getName() + " (" + device.getAddress() + ")\n");
            }
            
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mMainTextView.append("Finished Discovery.\n");
                
                if (!mDiscoveredDevices.isEmpty())
                    showDiscoveredDevices();
            }
            
        }

    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mMainTextView = (TextView) this.findViewById(R.id.mainTextView);

        // Get the default BT Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        mDiscoveredDevices = new ArrayAdapter<BluetoothDevice>(this, android.R.layout.simple_list_item_1);
    }

    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if (item.getItemId() == R.id.menu_item_start_discovery) {
            
            // Start discovery of BT devices
            if (mBluetoothAdapter.startDiscovery())
                mMainTextView.append("\nStarting Discovery...\n");                		
            else
                mMainTextView.append("\nError Starting Discovery!\n");
        }
        
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPause() {
        this.unregisterReceiver(mReceiver);
        super.onPause();      
    }
    
    
    @Override
    public void onResume() {
        super.onResume();

        // Turn on BT
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("Name: " + mBluetoothAdapter.getName() + "\n");
            sb.append("Address: " + mBluetoothAdapter.getAddress() + "\n");
            sb.append("State: " + printState(mBluetoothAdapter.getState()) + "\n");

            // Check to see if there already bonded or paired devices
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    sb.append("PairedDevice: " + device.getName() + "\n  " + device.getAddress() + "\n");
                }
            }

            mMainTextView.setText(sb.toString());
        }

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);        
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        
        registerReceiver(mReceiver, filter); 

    } 

    private String printState(int state) {

        String stateString = "";
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                stateString = "State OFF";
                break;

            case BluetoothAdapter.STATE_ON:
                stateString = "State OFF";
                break;

            case BluetoothAdapter.STATE_TURNING_OFF:
                stateString = "State Turning OFF";
                break;

            case BluetoothAdapter.STATE_TURNING_ON:
                stateString = "State Turning ON";
                break;
        }

        return stateString;
    }
    
    private void showDiscoveredDevices() {
        
        mMainTextView.setVisibility(View.GONE);
        
        mMainListView = (ListView) this.findViewById(R.id.mainListView);
        mMainListView.setAdapter(mDiscoveredDevices);
        mMainListView.setVisibility(View.VISIBLE);
        mMainListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String address = tv.getText().toString();
                
                if (!address.isEmpty())
                    connectDevice(address);
                
                mMainTextView.setVisibility(View.VISIBLE);
                mMainListView.setVisibility(View.GONE);
                
            }
            
        });
        
    }
    
    private void connectDevice(String address) {
        Log.d(TAG, "connectDevice address: " + address);
        
        // TODO Really do something useful on Connect
        
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            device.createRfcommSocketToServiceRecord(mUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }
    

}
