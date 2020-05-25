package com.example.lets_walk_firebase;


import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

//import android.support.v4.app.NotificationCompat;

public class RealService extends Service implements SensorEventListener {
    public static Intent serviceIntent = null;
    public static int intentNum = 0;
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;
    SensorManager sensorManager;
    Sensor accelerormeterSensor;

    NotificationManager Notifi_M;
    Notification Notifi;
    ServiceThread thread;

    private DatabaseReference databaseReference;


    // 서비스를 생성할 때 호출
    public void onCreate() {
        super.onCreate();

        /*String step_value = serviceIntent.getStringExtra("step");
        PedoActivity.cnt = Integer.parseInt(step_value);*/

        Log.d("여기는", "서비스의 onCreate");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;


        /*String step_value = serviceIntent.getStringExtra("step");
        PedoActivity.cnt = Integer.parseInt(step_value);*/

        String id_value = serviceIntent.getStringExtra("id");

        Log.d("서비스 start의 id_val", id_value);

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(id_value);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                String value = String.valueOf(map.get("step"));
                PedoActivity.cnt = Integer.parseInt(value);
                //PedoActivity.kcal = PedoActivity.cnt / 30;
                Map<String, List<String>> map2 = (Map) dataSnapshot.getValue();
                List<String> friend = map2.get("friends");
                PedoActivity.friends = friend;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Log.e("MyService", "Service startId = " + startId);
        super.onStart(intent, startId);
        Log.e("감지", "서비스의 onstart입니다");

        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);

        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        Log.d("여기는", "서비스의 onStartCommand");
        return START_STICKY;
    }
*/

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.d("여기는", "서비스의 sensor_changed");


        String id_value = serviceIntent.getStringExtra("id");


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            if (gabOfTime > 95) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime
                        * 10000;

                if (speed > SHAKE_THRESHOLD) {


                    PedoActivity.cnt++;
                    PedoActivity.kcal = PedoActivity.cnt/30;
                    FirebasePost user = new FirebasePost();

                    user.WriteStep(id_value, PedoActivity.cnt);
                    Intent intent1 = new Intent();
                    intent1.setAction("com.example.lets_walk_firebase");

                    String pass = Integer.toString(PedoActivity.cnt);
                    intent1.putExtra("DATAPASSED", pass);
                    sendBroadcast(intent1);
                    //Log.e("감지", "이벤트 발생");
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
        // }
    }

    class myServiceHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            if (serviceIntent != null) {
                Intent intent = new Intent(RealService.this, PedoActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(RealService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notifi = new Notification.Builder(getApplicationContext()).setContentTitle("렛츠워크").setContentText("실행되고 있어요!").setSmallIcon(R.drawable.logo).setTicker("알림!!!").setContentIntent(pendingIntent).build();
                Notifi.defaults = Notification.DEFAULT_SOUND;
                Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                Notifi_M.notify(777, Notifi);
                //Toast.makeText(RealService.this, "", Toast.LENGTH_LONG).show();

            }
        }
    };


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

        //serviceIntent = null;

        thread.stopForever();
        thread = null;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            Log.e("감지_destroy", "ondestroy");
        }
    }

    protected void onHandleIntent(Intent intent) {
        Intent intent1 = new Intent();
        intent1.setAction("com.example.lets_walk_firebase");
        intent1.putExtra("DATAPASSED", PedoActivity.cnt);
        sendBroadcast(intent1);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}