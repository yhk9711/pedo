package com.example.lets_walk_firebase;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

//import android.support.v4.app.NotificationCompat;

public class RealService extends Service implements SensorEventListener {
    public static Intent serviceIntent = null;
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    int SHAKE_THRESHOLD = 1000;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;
    SensorManager sensorManager;
    Sensor accelerormeterSensor;


    // 서비스를 생성할 때 호출
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }
    @Override
    public void onStart(Intent intent, int startId) {
        serviceIntent = intent;
        // Log.e("MyService", "Service startId = " + startId);
        super.onStart(intent, startId);

        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        String id_value = serviceIntent.getStringExtra("id");

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime
                        * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    //Log.e("감지", "흔듬감지됨");
                    //showToast(getApplication(), "흔들림감지");
                    PedoActivity.cnt++;

                    FirebasePost user = new FirebasePost();
                    user.WriteStep(id_value, PedoActivity.cnt);




                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
        // }
    }
    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        serviceIntent = null;
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}