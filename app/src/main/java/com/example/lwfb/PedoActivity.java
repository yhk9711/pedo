package com.example.lwfb;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;


public class PedoActivity extends Activity implements SensorEventListener {

    private DrawerLayout drawerLayout;
    private View drawerView;


    public static int cnt = 0;
    public static int kcal = cnt / 30;
    public static double dis = cnt / 1.5;
    public static int goal = 10000;

    private TextView fView;
    private TextView tView;
    private TextView kView;
    private TextView dView;
    private TextView gView;

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;

    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    private Intent serviceIntent;

    //private DatabaseReference mDatabase;


    //final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference ref = database.getReference("MEMBER");

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedo);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        fView = (TextView) findViewById(R.id.goalstepnum);
        tView = (TextView) findViewById(R.id.cntView);
        kView = (TextView) findViewById(R.id.kcalnum);
        dView = (TextView) findViewById(R.id.distancenum);
        gView = (TextView) findViewById(R.id.numtogoal2);

        String step_value = null;
        Intent i = getIntent();
        i.getStringExtra("step");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            step_value = bundle.getString("step");
            Log.d("step_value", step_value);
        }

        String goal_step = null;
        Intent i2 = getIntent();
        i2.getStringExtra("goal_step");
        Bundle bundle1 = getIntent().getExtras();
        if(bundle1 != null){
            goal_step = bundle1.getString("goal_step");
//            Log.d("goal_step", goal_step);
        }


        cnt = Integer.parseInt(step_value);
        goal = Integer.parseInt(goal_step);


        fView.setText("" + goal);
        tView.setText("" + (cnt));
        kView.setText("" + cnt / 30);
        String km = String.format("%.1f", (cnt / 1.5));
        dView.setText("" + km);
        gView.setText("" + (goal - cnt));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        //drawerLayout.openDrawer(drawerView);
        Button logout = (Button) findViewById(R.id.button);
        Button myInfo = (Button) findViewById(R.id.myinfo);



        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(PedoActivity.this, MainActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(PedoActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        myInfo.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PedoActivity.this, MyInfo.class);
                String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                startActivity(intent);
            }
        });




        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);

        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }


        if (RealService.serviceIntent == null) {
            String id_value2 = null;
            Intent i3 = getIntent();
            i3.getStringExtra("id");
            Bundle bundle2 = getIntent().getExtras();
            if (bundle2 != null) {
                id_value2 = bundle2.getString("id");
                Log.d("id", id_value2);
            }
            serviceIntent = new Intent(this, RealService.class);
            serviceIntent.putExtra("id", id_value2);
            startService(serviceIntent);
        } else {
            serviceIntent = RealService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }


    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
        //drawerLayout.openDrawer(drawerView);
    };


    @Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null) {
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.example.lwfb");
            registerReceiver(broadcastReceiver, intentFilter);
            Log.e("감지", "onstart");
            fView.setText("" + goal);
            tView.setText("" + (cnt));
            kView.setText("" + cnt / 30);
            String km = String.format("%.1f", (cnt / 1.5));
            dView.setText("" + km);
            gView.setText("" + (goal - cnt));
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("감지", "받아짐");
            String pass = intent.getStringExtra("DATAPASSED");
            cnt = Integer.parseInt(pass);
            //String s1 = intent.getStringExtra("DATAPASSED");
            fView.setText("" + goal);
            tView.setText("" + (cnt));
            kView.setText("" + cnt / 30);
            String km = String.format("%.1f", (cnt / 1.5));
            dView.setText("" + km);
            gView.setText("" + (goal - cnt));
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            unregisterReceiver(broadcastReceiver);
            Log.e("감지", "onstop");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 95) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    fView.setText("" + goal);
                    tView.setText("" + (++cnt));
                    kView.setText("" + cnt / 30);
                    String km = String.format("%.1f", (cnt / 1.5));
                    dView.setText("" + km);
                    gView.setText("" + (goal - cnt));
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];

                //DatabaseReference usersRef = ref.child("")

                //mDatabase = FirebaseDatabase.getInstance().getReference();
                //Map<String, Object> ChildUpdates = new HashMap<>();
                // ChildUpdates.put("/");


                String id_value = null;
                Intent i = getIntent();
                i.getStringExtra("id");
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    id_value = bundle.getString("id");
                    //Log.d("id", id_value);
                }
                FirebasePost user = new FirebasePost();
                user.WriteStep(id_value, cnt);

                Intent intent2 = new Intent(getApplicationContext(), com.example.lwfb.RealService.class);
                intent2.putExtra("id", id_value);

                //Intent intent = new Intent(PedoActivity.this, com.example.lets_walk_firebase.RealService.class);
                //intent.putExtra("cnt", String.valueOf(cnt));
                //startService(intent);


                //Intent intent = new Intent(getApplicationContext(), MyService.class);
                //startService(intent);
                //HashMap<String, Object> childUpdates = null;
                //childUpdates = new HashMap<>();
                //childUpdates.put("/MEMBER/" + id_value, );
                //childUpdates.put("step", cnt);
                //childUpdates.put("/MEMBER/" + id_value, )

                //Map<String, Object> userUpdates = new HashMap<>();
                //userUpdates.put("id_value/step", cnt);


            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}