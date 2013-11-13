package com.example.acelerometro;

/**
 * Created by Desarrollo on 05/11/13.
 */

        import android.app.Activity;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.hardware.Camera;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.media.AudioRecord;
        import android.media.MediaPlayer;
        import android.media.MediaRecorder;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.v7.app.ActionBarActivity;
        import android.util.Log;
        import android.view.Menu;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.util.Date;

/**
 * Created by Desarrollo on 04/11/13.
 */
public class prueba extends ActionBarActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private int action=0;
    boolean status=false;

    private Movement m= new Movement();

    public Camera cam;
    public android.hardware.Camera.Parameters p;



    private MediaPlayer mPlayer = null;
    private static String mFileName = null;
    private static final String LOG_TAG = "AudioRecordTest";
    MediaRecorder recorder = new MediaRecorder();


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

            //((TextView) findViewById(R.id.texto_prueba2)).setText(Boolean.toString(status));

            getInicio(event);

            if(status){

                if(!(limitarEje(event) && limitarTiempo(event.timestamp, m.getCurrentTime()))){
                    ((TextView) findViewById(R.id.texto_prueba)).setText("Acelerómetro X, Y , Z: " + event.values[0] +" , "+ event.values[1] +" , "+ event.values[2]);
                    return;
                }

                action=m.isMovement(event.values[0], event.values[1], event.values[2], event.timestamp);
                if(action>=0){
                    if(action==10){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivity(intent);
                        status=false;
                    }

                    if(action==12){
                        //context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                        if(cam==null){
                            cam = Camera.open();
                            p = cam.getParameters();
                            p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                            cam.setParameters(p);
                            cam.startPreview();
                        }
                        else{
                            p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
                            cam.setParameters(p);
                            cam.release();
                            cam = null;

                        }
                    }
                    if(action==13){
                      /* if(mPlayer==null){

                           recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                           recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                           recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                           recorder.setOutputFile(path);
                           recorder.prepare();
                           recorder.start();
                       }
                        else{
                           recorder.stop();
                           recorder.reset();
                           recorder.release();

                           recorder = null;
                       }
*/
                    }
                }

                ((TextView) findViewById(R.id.texto_prueba2)).setText(Integer.toString(action));

                ((TextView) findViewById(R.id.texto_prueba1)).setText(Long.toString((event.timestamp-m.getCurrentTime())/500000000));
            }
            //((TextView) findViewById(R.id.texto_prueba)).setText("Acelerómetro X, Y , Z: " + curX +" , "+ curY +" , "+ curZ);
        }
    }

    public void getInicio(SensorEvent event){
        if(!status){
            //Inicio en X positivo
            if(event.values[0]>8 && event.values[0]<10.8){
                m.setMovement(event.values[0], event.values[1], event.values[2], event.timestamp, 0, 2);
                long current_time = event.timestamp;
                status=true;
                //mov++;
            }

            //Inicio en Y positivo
            if(event.values[1]>8 && event.values[1]<10.8){
                m.setMovement(event.values[0],event.values[1],event.values[2], event.timestamp, 1, 2);
                long current_time = event.timestamp;
                status=true;
                //mov++;
            }

            //Inicio en Z positivo
            if(event.values[2]>8 && event.values[2]<10.8){
                m.setMovement(event.values[0],event.values[1],event.values[2], event.timestamp, 2, 2);
                long current_time = event.timestamp;
                status=true;
                //mov++;
            }

            //Inicio en X negativo
            if(event.values[0]<-8 && event.values[0]>-10.8){
                m.setMovement(event.values[0],event.values[1],event.values[2], event.timestamp, 3, 2);
                long current_time = event.timestamp;
                status=true;
                //mov++;
            }

           //Inicio en Y negativo
            if(event.values[1]<-8 && event.values[1]>-10.8){
                m.setMovement(event.values[0],event.values[1],event.values[2], event.timestamp, 4, 2);
                long current_time = event.timestamp;
                status=true;
                //mov++;
            }

            //Inicio en Z negativo
            if(event.values[2]<-8 && event.values[2]>-10.8){
                m.setMovement(event.values[0],event.values[1],event.values[2], event.timestamp, 5, 2);
                long current_time = event.timestamp;
                status=true;
                //mov++;
            }
        }

    }

    public boolean limitarEje(SensorEvent event){

        if(Math.abs(event.values[0])>5 && Math.abs(event.values[1])>5 && Math.abs(event.values[2])>4)
            return status=false;
        if(Math.abs(event.values[0])>5 && Math.abs(event.values[1])>4 && Math.abs(event.values[2])>5)
            return status=false;
        if(Math.abs(event.values[0])>4 && Math.abs(event.values[1])>5 && Math.abs(event.values[2])>5)
            return status=false;
        return true;
        //return (Math.abs(event.values[0])>4 && Math.abs(event.values[1])>4 && Math.abs(event.values[1])>4) ? status=false : true;

    }

    public boolean limitarTiempo(long actual, long ultimo){

        return (((actual-ultimo)/1000000000)>=3) ? status=false : true;

    }


}
