package com.example.HelloSensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Vibrator;

public class Compass extends AppCompatActivity implements SensorEventListener {

    private ImageView compassImage;
    private TextView heading;
    private TextView directionTitle;
    private EditText input;

    private float currentDegree = 0f;

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compassImage = (ImageView)findViewById(R.id.compassImage);
        heading = (TextView)findViewById(R.id.heading);
        input = (EditText)findViewById(R.id.input);
        directionTitle = (TextView)findViewById(R.id.directionTitle);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);
        heading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        compassImage.startAnimation(ra);
        currentDegree = -degree;

        try {
            int chosenHeading = Integer.parseInt(input.getText().toString());
            directionTitle.setText("Your direction: " + chosenHeading + "°");

            if(chosenHeading >= 0 && chosenHeading <= 360) {
                if(degree == chosenHeading) {
                    mediaPlayer.start();
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        mediaPlayer.start();
                        //deprecated in API 26
                        vibrator.vibrate(500);
                    }
                }
            } else {
                heading.setText("You can only choose 0-365°");
            }

        } catch (NumberFormatException e) {
            directionTitle.setText("Your direction: 360°");

            if(degree > 345 && degree < 15){
                getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            }

            if(degree == 360) {
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mediaPlayer.start();
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    mediaPlayer.start();
                    //deprecated in API 26
                    vibrator.vibrate(500);
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used
    }
}