package com.example.lwfb;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PedoActivity extends Activity implements SensorEventListener {

    public static List<String> friends = new ArrayList<String>();
    //public static List<Integer> cntlist= new ArrayList<>();
    public static List<Integer> cntlist= new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0));

    private static int ONE_MINUTE = 5626;
    public static int index=0;



    private DrawerLayout drawerLayout;
    private View drawerView;

    private DatabaseReference databaseReference;

    public static String sheight ="123";
    public static int height;
    public static int cnt = 0;
    public static int kcal = cnt / 30;
    public static int goal = 10000;
    public static String my_id;
    public static String my_name;


    private TextView fView;
    private TextView kView;
    private TextView dView;
    private TextView foodView;

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

    public static Intent serviceIntent;

    String user_name = null;
    public static String step_value;
    public static String goal_step;
    public static String main_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedo);

        friends.add(my_id);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch(day){
            case 1:
                index = 6; //일요일
                break;
            case 2:
                index = 0; //월요일
                break;
            case 3:
                index = 1; //화요일
                break;
            case 4:
                index = 2; //수요일
                break;
            case 5:
                index = 3; //목요일
                break;
            case 6:
                index = 4; //금요일
                break;
            case 7:
                index = 5; //토요일
                break;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("CALORIE").child("0");


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        fView = (TextView) findViewById(R.id.goalstepnum);
        kView = (TextView) findViewById(R.id.kcalnum);
        dView = (TextView) findViewById(R.id.distancenum);
        foodView = (TextView) findViewById(R.id.kcalfoodname);


        //String step_value = null;
        Intent i = getIntent();
        i.getStringExtra("step");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            step_value = bundle.getString("step");
            Log.d("step_value", step_value);

        }
        Intent i4 = getIntent();
        i4.getStringExtra("height");
        Bundle bundle4 = getIntent().getExtras();
        if (bundle != null) {
            sheight = bundle.getString("height");
        }
        Log.d("frontsheight", String.valueOf(sheight));
        Log.d("frontheight", String.valueOf(height));
        height = Integer.parseInt(sheight);
        Log.d("sheight", String.valueOf(sheight));
        Log.d("height", String.valueOf(height));

        //String goal_step = null;
        Intent i2 = getIntent();
        i2.getStringExtra("goal_step");
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {
            goal_step = bundle1.getString("goal_step");
            Log.d("goal_step", goal_step);
            goal = Integer.parseInt(goal_step);
        }


        Intent i3 = getIntent();
        i3.getStringExtra("name");
        Bundle bundle22 = getIntent().getExtras();
        if (bundle22 != null) {
            my_name = bundle22.getString("name");
            user_name = my_name;
        }
        /*Intent i45 = getIntent();
        i45.getStringExtra("index");
        Bundle bundle45 = getIntent().getExtras();
        if (bundle45 != null) {
            index = Integer.valueOf(bundle.getString("index"));
            Log.d("index값 페도에서 가져옴", String.valueOf(index));

        }*/

        //String main_id = null;
        Intent i44 = getIntent();
        i44.getStringExtra("id");
        Bundle bundle33 = getIntent().getExtras();
        if (bundle33 != null) {
            main_id = bundle33.getString("id");
            Log.d("id", main_id);
        }
        my_id = main_id;

        cnt = Integer.parseInt(step_value);
        Log.e("Pedo의 onCreate에서 step", step_value);
        Log.e("Pedo의 onCreate에서 cnt", String.valueOf(cnt));
        //goal = Integer.parseInt(goal_step);
        kcal = cnt / 30;

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(PedoActivity.my_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                Map<String, List<Integer>> map3 = (Map) dataSnapshot.getValue();

                List<Integer> stplist = map3.get("steps");

                Log.e("stplist", String.valueOf(stplist));
                cntlist=stplist;
                Log.e("cntlistreg", String.valueOf(RegisterActivity.cntlistreg));
                Log.e("cntlist", String.valueOf(cntlist));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("CALORIE");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (kcal < 30) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("0");
                    Log.d("get_value", value);
                    foodView.setText("" + value);

                } else if (kcal >= 30 && kcal < 50) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("30");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 50 && kcal < 100) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("50");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 100 && kcal < 150) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("100");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 150 && kcal < 200) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("150");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 200 && kcal < 250) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("200");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 250 && kcal < 300) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("250");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 300 && kcal < 350) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("300");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 350 && kcal < 400) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("350");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 400 && kcal < 450) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("400");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 450 && kcal < 500) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("450");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 500 && kcal < 550) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("500");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 550 && kcal < 600) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("550");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 600 && kcal < 650) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("600");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 650 && kcal < 700) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("650");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 700 && kcal < 750) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("700");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 750 && kcal < 800) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("750");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 800 && kcal < 850) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("800");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 850 && kcal < 900) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("850");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 900 && kcal < 950) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("900");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 950 && kcal < 1000) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("950");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                } else if (kcal >= 1000) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String value = map.get("1000");
                    Log.d("get_value", value);
                    foodView.setText("" + value);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fView.setText("" + goal);
        kView.setText("" + cnt / 30);
        String km = String.format("%.1f", (height *0.37 *0.01 *cnt));
        dView.setText("" + km);

        drawerLayout = (DrawerLayout)

                findViewById(R.id.drawer_layout);

        drawerView = (View)

                findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        Button logout = (Button) findViewById(R.id.button);
        Button myInfo = (Button) findViewById(R.id.myinfo);
        Button friendlist = (Button) findViewById(R.id.friendlist);
        Button notice = (Button) findViewById(R.id.notice);
        Button hometraining = (Button) findViewById(R.id.hometraining);
        Button maps = (Button) findViewById(R.id.maps);
        TextView name = (TextView) findViewById(R.id.nameofuser);
        name.setText("" + my_name + " 님");


        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해 SharedPreferences를 불러옴
                Intent intent = new Intent(PedoActivity.this, MainActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //auto에 들어있는 모든 정보를 기기에서 지움
                editor.clear();
                editor.commit();
                Toast.makeText(PedoActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        myInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedoActivity.this, MyInfo.class);
                /*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                }*/
                intent.putExtra("id", my_id);
                intent.putExtra("name", user_name);
                startActivity(intent);
            }
        });
        friendlist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent123 = new Intent(PedoActivity.this, FriendListActivity.class);
                String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");

                }
                intent123.putExtra("id", id_value2);
                intent123.putExtra("name", user_name);
                startActivity(intent123);
            }
        });
        notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedoActivity.this, NoticeActivity.class);
                String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);
                startActivity(intent);
            }
        });
        hometraining.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedoActivity.this, HomeTrainActivity.class);
                String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedoActivity.this, MapsActivity.class);
                String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);
                startActivity(intent);
            }
        });

        resetAlarm(getApplicationContext());
        Log.e("pedo", "페도에서 알람서비스 호출_oncreate");
        //  new AlarmHATT(getApplicationContext()).Alarm();
        cntlist.set(index, cnt);

    }
    /*public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(PedoActivity.this, com.example.lwfb.AlarmReceiver.class);

            PendingIntent sender = PendingIntent.getBroadcast(PedoActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //   calendar.setTimeInMillis(System.currentTimeMillis());
            //   calendar.set(Calendar.HOUR_OF_DAY,18);
            //   calendar.set(Calendar.MINUTE,43);
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 19, 10, 0);

            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, sender);
            Log.d("페도!!!!!!!!!", "걸음수 바뀜ㅁㅁㅁㅁㅁ");
        }
    }
*/
    public static void resetAlarm(Context context){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, com.example.lwfb.AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,0);


        //자정 시간
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        long aTime = System.currentTimeMillis();
        long bTime = calendar.getTimeInMillis();

        long interval = 1000*60*60*24;

        while(aTime > bTime) {
            bTime += interval;
            Log.e("btime", "btime: " + bTime);
        }

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌
        am.setInexactRepeating(am.RTC_WAKEUP, bTime, am.INTERVAL_DAY, sender);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setRestTime = format.format(new Date(bTime));
        String setRestTime2 = format.format(new Date(aTime));

        Log.e("resetAlarm", "ResetHour: " + setRestTime);
        Log.e("resetAlarm2", "ResetHour: " + setRestTime2);

    }

    @Override
    protected void onDestroy() {
        step_value = String.valueOf(cnt);
        super.onDestroy();
        Log.d("여기는", "페도의 onDestroy");

        serviceIntent = new Intent(this, RealService.class);
        serviceIntent.putExtra("id", my_id);
        serviceIntent.putExtra("step", cnt);
        startService(serviceIntent);

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
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        step_value = String.valueOf(cnt);
        //resetAlarm(getApplicationContext());

        if (accelerormeterSensor != null) {
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.example.lwfb");
            registerReceiver(broadcastReceiver, intentFilter);
            Log.e("페도의 ", "onstart입니다");
            //new AlarmHATT(getApplicationContext()).Alarm();

            kcal = cnt / 30;
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (kcal < 30) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("0");
                        Log.d("get_value", value);
                        foodView.setText("" + value);

                    } else if (kcal >= 30 && kcal < 50) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("30");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 50 && kcal < 100) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("50");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 100 && kcal < 150) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("100");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 150 && kcal < 200) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("150");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 200 && kcal < 250) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("200");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 250 && kcal < 300) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("250");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 300 && kcal < 350) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("300");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 350 && kcal < 400) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("350");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 400 && kcal < 450) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("400");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 450 && kcal < 500) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("450");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 500 && kcal < 550) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("500");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 550 && kcal < 600) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("550");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 600 && kcal < 650) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("600");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 650 && kcal < 700) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("650");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 700 && kcal < 750) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("700");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 750 && kcal < 800) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("750");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 800 && kcal < 850) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("800");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 850 && kcal < 900) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("850");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 900 && kcal < 950) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("900");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 950 && kcal < 1000) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("950");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 1000) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("1000");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            fView.setText("" + goal);
            kView.setText("" + cnt / 30);
            String km = String.format("%.1f", (height *0.37*0.01 *cnt));
            dView.setText("" + km);
            PieChart pieChart = findViewById(R.id.piechart);

            ArrayList Step = new ArrayList();
            int numtogoal = PedoActivity.goal - PedoActivity.cnt;
            if (numtogoal < 0) {
                numtogoal = 0;
            }
            String nowstep = Integer.toString(PedoActivity.cnt);
            PieDataSet dataSet = new PieDataSet(Step, "");
            Step.add(new Entry(PedoActivity.cnt, 0));
            Step.add(new Entry(numtogoal, 1));

            ArrayList year = new ArrayList();

            year.add("현재 걸음수");
            year.add("남은 걸음수");

            pieChart.setCenterText(nowstep);
            pieChart.setCenterTextSize(40f);
            pieChart.setCenterTextColor(Color.BLACK);

            PieData data = new PieData(year, dataSet);          // MPAndroidChart v3.X 오류 발생
            pieChart.setData(data);
            data.setValueTextSize(14f);
            //data.setValueTextColor(Color.TRANSPARENT);
            final int[] MY_COLORS = {Color.rgb(191,212,252), Color.rgb(142,169,219),
                    Color.rgb(150,194,255), Color.rgb(97,157,242), Color.rgb(40,99,176), Color.rgb(14,73,156),Color.rgb(0,40,97)};
            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : MY_COLORS) colors.add(c);

            dataSet.setColors(colors);
            //dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
            pieChart.animateXY(0, 0);

            pieChart.setDrawHoleEnabled(true);
            //pieChart.setHoleColorTransparent(true);
            pieChart.setHoleColor(Color.TRANSPARENT);
            pieChart.setHoleRadius(80);
            pieChart.setTransparentCircleRadius(10);
            pieChart.setDescription(null);
            pieChart.getLegend().setEnabled(false);

        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("감지", "받아짐");
            String pass = intent.getStringExtra("DATAPASSED");
            cnt = Integer.parseInt(pass);
            step_value = String.valueOf(cnt);
            kcal = cnt / 30;
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (kcal < 30) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("0");
                        Log.d("get_value", value);
                        foodView.setText("" + value);

                    } else if (kcal >= 30 && kcal < 50) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("30");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 50 && kcal < 100) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("50");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 100 && kcal < 150) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("100");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 150 && kcal < 200) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("150");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 200 && kcal < 250) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("200");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 250 && kcal < 300) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("250");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 300 && kcal < 350) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("300");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 350 && kcal < 400) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("350");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 400 && kcal < 450) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("400");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 450 && kcal < 500) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("450");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 500 && kcal < 550) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("500");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 550 && kcal < 600) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("550");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 600 && kcal < 650) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("600");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 650 && kcal < 700) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("650");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 700 && kcal < 750) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("700");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 750 && kcal < 800) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("750");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 800 && kcal < 850) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("800");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 850 && kcal < 900) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("850");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 900 && kcal < 950) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("900");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 950 && kcal < 1000) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("950");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    } else if (kcal >= 1000) {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String value = map.get("1000");
                        Log.d("get_value", value);
                        foodView.setText("" + value);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            fView.setText("" + goal);
            kView.setText("" + cnt / 30);
            String km = String.format("%.1f", (height *0.37*0.01 *cnt));
            dView.setText("" + km);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            unregisterReceiver(broadcastReceiver);
            Log.e("감지", "페도의 onstop입니다");

            serviceIntent = new Intent(this, RealService.class);

            serviceIntent.putExtra("id", my_id);
            serviceIntent.putExtra("step", cnt);
            startService(serviceIntent);
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
                    kcal = cnt / 30;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (kcal < 30) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("0");
                                Log.d("get_value", value);
                                foodView.setText("" + value);

                            } else if (kcal >= 30 && kcal < 50) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("30");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 50 && kcal < 100) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("50");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 100 && kcal < 150) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("100");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 150 && kcal < 200) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("150");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 200 && kcal < 250) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("200");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 250 && kcal < 300) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("250");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 300 && kcal < 350) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("300");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 350 && kcal < 400) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("350");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 400 && kcal < 450) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("400");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 450 && kcal < 500) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("450");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 500 && kcal < 550) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("500");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 550 && kcal < 600) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("550");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 600 && kcal < 650) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("600");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 650 && kcal < 700) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("650");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 700 && kcal < 750) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("700");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 750 && kcal < 800) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("750");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 800 && kcal < 850) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("800");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 850 && kcal < 900) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("850");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 900 && kcal < 950) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("900");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 950 && kcal < 1000) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("950");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            } else if (kcal >= 1000) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                String value = map.get("1000");
                                Log.d("get_value", value);
                                foodView.setText("" + value);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    fView.setText("" + goal);
                    ++cnt;
                    step_value = String.valueOf(cnt);
                    kView.setText("" + cnt / 30);
                    String km = String.format("%.1f", (height *0.01*0.37 *cnt));
                    dView.setText("" + km);

                    PieChart pieChart = findViewById(R.id.piechart);

                    ArrayList Step = new ArrayList();
                    int numtogoal = PedoActivity.goal - PedoActivity.cnt;
                    if (numtogoal < 0) {
                        numtogoal = 0;
                    }
                    String nowstep = Integer.toString(PedoActivity.cnt);
                    PieDataSet dataSet = new PieDataSet(Step, "");
                    Step.add(new Entry(PedoActivity.cnt, 0));
                    Step.add(new Entry(numtogoal, 1));

                    ArrayList year = new ArrayList();

                    year.add("현재 걸음수");
                    year.add("남은 걸음수");

                    pieChart.setCenterText(nowstep);
                    pieChart.setCenterTextSize(40f);
                    pieChart.setCenterTextColor(Color.BLACK);

                    PieData data = new PieData(year, dataSet);          // MPAndroidChart v3.X 오류 발생
                    pieChart.setData(data);
                    data.setValueTextSize(14f);
                    //data.setValueTextColor(Color.TRANSPARENT);
                    final int[] MY_COLORS = {Color.rgb(102, 153, 204), Color.rgb(255, 255, 255), Color.rgb(255, 192, 0),
                            Color.rgb(127, 127, 127), Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189)};
                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : MY_COLORS) colors.add(c);

                    dataSet.setColors(colors);
                    //dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                    pieChart.animateXY(0, 0);

                    pieChart.setDrawHoleEnabled(true);
                    //pieChart.setHoleColorTransparent(true);
                    pieChart.setHoleColor(Color.TRANSPARENT);
                    pieChart.setHoleRadius(80);
                    pieChart.setTransparentCircleRadius(10);
                    pieChart.setDescription(null);
                    pieChart.getLegend().setEnabled(false);

                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];


                FirebasePost user = new FirebasePost();
                user.WriteStep(my_id, cnt);
                SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
                Date time = new Date();
                String time1 = format1.format(time);

                user.WritePastSteps(my_id,time1);
                cntlist.set(index, cnt);
//                user.WriteSteps(my_id, RegisterActivity.cntlistreg);
                user.WriteSteps(my_id);
                //Log.d("pedo에서writesteps",String.valueOf(cntlist));
                Intent intent2 = new Intent(getApplicationContext(), RealService.class);
                intent2.putExtra("id", my_id);

            }
        }
    }
    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}