package aad.app.c17;

import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class SimulatedSensorActivity extends Activity implements SensorEventListener {

    private static final String TAG = SimulatedSensorActivity.class.getSimpleName();
    
    private SensorManagerSimulator mSensorManager;
    private Sensor mOrientationSensor;
    
    private String mSensorString = "";
    
    private TextView mOrientationTextView;

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mOrientationTextView = (TextView) this.findViewById(R.id.orientationTextView);
        
        mSensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
                
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }
    
    @Override
    protected void onPause() {

        mSensorManager.disconnectSimulator();
        mSensorManager.unregisterListener(this);
        super.onPause();
    }
    
    @Override
    protected void onResume() {

        mSensorManager.connectSimulator();
        
        mSensorManager.registerListener(this, mOrientationSensor, SensorManagerSimulator.SENSOR_DELAY_NORMAL);                
                
        super.onResume();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

            mSensorString = String.valueOf(event.values[0]);
            mSensorString += " : ";
            mSensorString += String.valueOf(event.values[1]);
            mSensorString += " : ";
            mSensorString += String.valueOf(event.values[2]);
            mOrientationTextView.setText("Orientation: " + mSensorString);
            return;
    }
}