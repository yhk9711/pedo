package com.example.lets_walk_firebase;


import android.app.AlarmManager;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import java.text.SimpleDateFormat;

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
    com.example.lets_walk_firebase.ServiceThread thread;

    private DatabaseReference databaseReference;

    private static int ONE_MINUTE = 5626;


    // 서비스를 생성할 때 호출
    public void onCreate() {
        super.onCreate();
        PedoActivity.step_value =Integer.toString(PedoActivity.cnt);
        PedoActivity.goal_step = Integer.toString(PedoActivity.goal);
        PedoActivity.pedo_index = PedoActivity.index;

        // new AlarmHATT(getApplicationContext()).Alarm();
        resetAlarm(getApplicationContext());

        /*String step_value = serviceIntent.getStringExtra("step");
        PedoActivity.cnt = Integer.parseInt(step_value);*/

        Log.d("여기는", "서비스의 onCreate");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public static void resetAlarm(Context context){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, com.example.lets_walk_firebase.AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //자정 시간
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 33);
        calendar.set(Calendar.SECOND, 0);

        long aTime = System.currentTimeMillis();
        long bTime = calendar.getTimeInMillis();

        long interval = 1000*60*60*24;

        while(aTime > bTime){
            bTime += interval;
        }

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌
        am.setInexactRepeating(am.RTC_WAKEUP, bTime, am.INTERVAL_DAY, sender);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setRestTime = format.format(new Date(bTime));
        String setRestTime2 = format.format(new Date(aTime));

        Log.e("resetAlarm", "ResetHour: " + setRestTime);
        Log.e("resetAlarm2", "ResetHour: " + setRestTime2);

    }
    /* public class AlarmHATT {
         private Context context;
         public AlarmHATT(Context context) {
             this.context=context;
         }
         public void Alarm() {
             AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
             Intent intent = new Intent(RealService.this, AlarmReceiver.class);

             PendingIntent sender = PendingIntent.getBroadcast(RealService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

             Calendar calendar = Calendar.getInstance();
             //알람시간 calendar에 set해주기

             calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 18, 11, 0);
             Log.d("date", Calendar.YEAR + " " + Calendar.MONTH + " " + Calendar.DATE);

             //알람 예약
             am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), sender);
             *//*if(am != null && sender != null){
                am.cancel(sender);
            }*//*
        }
    }
*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PedoActivity.step_value =Integer.toString(PedoActivity.cnt);
        PedoActivity.goal_step = Integer.toString(PedoActivity.goal);
        PedoActivity.pedo_index = PedoActivity.index;
        serviceIntent = intent;

        resetAlarm(getApplicationContext());
        /*String step_value = serviceIntent.getStringExtra("step");
        PedoActivity.cnt = Integer.parseInt(step_value);*/

        String id_value = serviceIntent.getStringExtra("id");

//        Log.d("서비스 start의 id_val", id_value);

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(id_value);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                String value = String.valueOf(map.get("step"));
                int index = Integer.parseInt(map.get("index"));
                PedoActivity.cnt = Integer.parseInt(value);
                PedoActivity.index = index;
                //PedoActivity.kcal = PedoActivity.cnt / 30;
                Map<String, List<String>> map2 = (Map) dataSnapshot.getValue();
                List<String> friend = map2.get("friends");
                PedoActivity.friends = friend;
                PedoActivity.my_name = map.get("name");
                PedoActivity.step_value =Integer.toString(PedoActivity.cnt);
                PedoActivity.goal_step = Integer.toString(PedoActivity.goal);
                PedoActivity.pedo_index = PedoActivity.index;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart(intent, startId);
        Log.e("감지", "서비스의 onstart입니다");

        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);

        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new com.example.lets_walk_firebase.ServiceThread(handler);
        thread.start();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        String id_value = serviceIntent.getStringExtra("id");

        PedoActivity.step_value =Integer.toString(PedoActivity.cnt);
        PedoActivity.goal_step = Integer.toString(PedoActivity.goal);
        PedoActivity.pedo_index = PedoActivity.index;
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

                    PedoActivity.steps.set(PedoActivity.index, PedoActivity.cnt);

                    user.WriteStep(id_value, PedoActivity.cnt);
                    user.WriteSteps(id_value, PedoActivity.steps);
                    Intent intent1 = new Intent();
                    intent1.setAction("com.example.lets_walk_firebase");

                    String pass = Integer.toString(PedoActivity.cnt);
                    intent1.putExtra("DATAPASSED", pass);
                    sendBroadcast(intent1);
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }

    class myServiceHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            if (serviceIntent != null) {
                Intent intent = new Intent(RealService.this, PedoActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(RealService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notifi = new Notification.Builder(getApplicationContext()).setContentTitle("렛츠워크").setContentText(Integer.toString(PedoActivity.cnt)+"걸음").setSmallIcon(R.drawable.logo).setTicker("알림!!!").setContentIntent(pendingIntent).build();
                Notifi.defaults = Notification.DEFAULT_SOUND;
                Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                Notifi_M.notify(777, Notifi);


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

        PedoActivity.step_value =Integer.toString(PedoActivity.cnt);
        PedoActivity.goal_step = Integer.toString(PedoActivity.goal);
        PedoActivity.pedo_index = PedoActivity.index;
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

    /*public static void resetAlarm(Context context){
        AlarmManager resetAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, 0);

        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 21);
        resetCal.set(Calendar.MINUTE,50);
        resetCal.set(Calendar.SECOND, 0);

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis()
                +AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, resetSender);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY));

        Log.d("resetAlarm", "ResetHour : " + setResetTime);
    }*/


}