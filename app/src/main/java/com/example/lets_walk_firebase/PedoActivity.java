package com.example.lets_walk_firebase;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedoActivity extends Activity implements SensorEventListener {

    public static List<String> friends = new ArrayList<String>();



    private DrawerLayout drawerLayout;
    private View drawerView;

    private DatabaseReference databaseReference;


    public static int cnt = 0;
    public static int height;
    public static int kcal = cnt / 30;
    public static double dis = cnt / 1.5;
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
        setContentView(R.layout.activity_pedometer);

        friends.add(my_id);

        databaseReference = FirebaseDatabase.getInstance().getReference("CALORIE").child("0");
        /*databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(my_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                goal_step = map.get("goal_step");
                goal = Integer.parseInt(goal_step);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


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

        //String goal_step = null;
        Intent i2 = getIntent();
        i2.getStringExtra("goal_step");
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {
            goal_step = bundle1.getString("goal_step");
            Log.d("goal_step", goal_step);

        }


        Intent i3 = getIntent();
        i3.getStringExtra("name");
        Bundle bundle22 = getIntent().getExtras();
        if (bundle22 != null) {
            my_name = bundle22.getString("name");
            user_name = my_name;
        }

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
        String km = String.format("%.1f", (cnt / 1.5));
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

        if (accelerormeterSensor != null) {
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.example.lets_walk_firebase");
            registerReceiver(broadcastReceiver, intentFilter);
            Log.e("페도의 ", "onstart입니다");

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
            String km = String.format("%.1f", (cnt / 1.5));
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
            String km = String.format("%.1f", (cnt / 1.5));
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

           /* String id_value2 = null;
            Intent i4 = getIntent();
            i4.getStringExtra("id");
            Bundle bundle2 = getIntent().getExtras();
            if (bundle2 != null) {
                id_value2 = bundle2.getString("id");
                Log.d("id", id_value2);
            }
            String step_value = null;
            Intent i = getIntent();
            i.getStringExtra("step");
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                step_value = bundle.getString("step");
                Log.d("step_value", step_value);
            }*/
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
                    String km = String.format("%.1f", (cnt / 1.5));
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