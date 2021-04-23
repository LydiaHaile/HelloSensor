package com.example.HelloSensor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView accelerometerX;
    private TextView accelerometerY;
    private TextView accelerometerZ;
    private TextView lutning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        accelerometerX = (TextView)findViewById(R.id.accelerometerX);
        accelerometerY = (TextView)findViewById(R.id.accelerometerY);
        accelerometerZ = (TextView)findViewById(R.id.accelerometerZ);
        lutning = (TextView)findViewById(R.id.lutning);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double X = Math.round(event.values[0]*100.0)/100.0;
        double Y = Math.round(event.values[1]*100.0)/100.0;
        double Z = Math.round(event.values[2]*100.0)/100.0;

        accelerometerX.setText("X: " + X);
        accelerometerY.setText("Y: " + Y);
        accelerometerZ.setText("Z: " + Z);

        if(event.values[0] > 1) {
            lutning.setText("LEFT = BLUE");
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
        } else if (event.values[0] < -1) {
            lutning.setText("RIGHT = YELLOW");
            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        } else {
            lutning.setText("NEUTRAL = GREEN");
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used
    }
}