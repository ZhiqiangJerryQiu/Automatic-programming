package aad.app.c17;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class RealSensorActivity extends Activity implements SensorEventListener {

    private static final String TAG = RealSensorActivity.class.getSimpleName();
    
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mLightSensor;
    private Sensor mOrientationSensor;
    
    private String mSensorString = "";
    
    private TextView mAccelerometerTextView;
    private TextView mLightTextView;
    private TextView mOrientationTextView;
    private TextView mMainTextView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mAccelerometerTextView = (TextView) this.findViewById(R.id.accelerometerTextView);
        mLightTextView = (TextView) this.findViewById(R.id.lightTextView);
        mOrientationTextView = (TextView) this.findViewById(R.id.orientationTextView);
        mMainTextView = (TextView) this.findViewById(R.id.mainTextView);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);        
        
        List<Sensor> mSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sb = new StringBuilder();
        sb.append("Available Sensors:\n");
        sb.append("\n");
        for (Sensor s : mSensors) {
            sb.append(s.getName() + "\n");
        }

        mMainTextView.setText(sb);

        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }
    
    @Override
    protected void onPause() {

        mSensorManager.unregisterListener(this);
        super.onPause();
    }
    
    @Override
    protected void onResume() {

        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mOrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                
        super.onResume();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        // Not used
        
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mAccelerometerSensor) {
            mSensorString = "Force X: ";
            mSensorString += String.valueOf(event.values[0]);
            mSensorString += "\nForce Y: ";
            mSensorString += String.valueOf(event.values[1]);
            mSensorString += "\nForce Z: ";
            mSensorString += String.valueOf(event.values[2]);
            mAccelerometerTextView.setText("Accelerometer: \n" + mSensorString);
            return;
        }

        if (event.sensor == mLightSensor) {
            mSensorString = String.valueOf(event.values[0]);
            mLightTextView.setText("\nLight Sensor: " + mSensorString);
            return;
        }

        if (event.sensor == mOrientationSensor) {
            mSensorString = "Azimuth: ";
            mSensorString += String.valueOf(event.values[0]);
            mSensorString += "\nPitch: ";
            mSensorString += String.valueOf(event.values[1]);
            mSensorString += "\nRoll: ";
            mSensorString += String.valueOf(event.values[2]);
            mOrientationTextView.setText("\nOrientation: \n" + mSensorString);
            return;
        }
    }
}
