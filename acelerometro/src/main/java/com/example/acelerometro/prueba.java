package com.example.acelerometro;

/**
 * Created by Desarrollo on 05/11/13.
 */

        import android.app.Activity;
        import android.content.Intent;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.v7.app.ActionBarActivity;
        import android.view.Menu;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.Date;

/**
 * Created by Desarrollo on 04/11/13.
 */
public class prueba extends ActionBarActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private int action=0;
    boolean status=false;

    private Movement m;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acelerometro);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //a.setText("asfdjksafnds");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        synchronized (this) {

            if(event.values[1]>7 && event.values[1]<9.8 && !status){
                m = new Movement(event.values[0],event.values[1],event.values[2], event.timestamp, 1, 2);

                long current_time = event.timestamp;
                status=true;
                //mov++;

            }

            if(status){
                action=m.isMovement(event.values[0], event.values[1], event.values[2], event.timestamp);

                if(action>=0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                    status=false;
                }
                ((TextView) findViewById(R.id.texto_prueba)).setText(Integer.toString(action));
                ((TextView) findViewById(R.id.texto_prueba1)).setText(Long.toString((event.timestamp-m.getCurrentTime())/500000000));
            }


           /* if(mov==3){
                status=false;
                mov=0;
                ((TextView) findViewById(R.id.texto_prueba)).setText("GRITA");

            }

            */

            //((TextView) findViewById(R.id.texto_prueba)).setText("Acelerómetro X, Y , Z: " + curX +" , "+ curY +" , "+ curZ);

        }


    }



}
