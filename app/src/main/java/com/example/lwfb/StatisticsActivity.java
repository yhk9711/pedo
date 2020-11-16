package com.example.lwfb;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    public ArrayList<String> past_date = new ArrayList();
    public ArrayList<Integer> past_cnt = new ArrayList();

    private DrawerLayout drawerLayout;
    private View drawerView;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        /*drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        TextView name = (TextView) findViewById(R.id.nameofuser);
        name.setText("" + PedoActivity.my_name + " 님");
        Button logout = (Button) findViewById(R.id.button);
        Button myInfo = (Button) findViewById(R.id.myinfo);
        Button friendlist = (Button) findViewById(R.id.friendlist);
        Button notice = (Button) findViewById(R.id.notice);
        Button hometraining = (Button) findViewById(R.id.hometraining);
        Button maps = (Button) findViewById(R.id.maps);*/


        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(StatisticsActivity.this, com.example.lwfb.MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(StatisticsActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, com.example.lwfb.MyInfo.class);
                *//*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*//*
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, NoticeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, com.example.lwfb.FriendListActivity.class);
                *//*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*//*
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        hometraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, com.example.lwfb.HomeTrainActivity.class);
                *//*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*//*
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, com.example.lwfb.MapsActivity.class);
                *//*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*//*
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
*/

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(PedoActivity.my_id).child("paststeps");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map)dataSnapshot.getValue();
                //past_date = map.keySet();*/
                Iterator<String> iterator = map.keySet().iterator();
                while(iterator.hasNext()){

                    String date = iterator.next();

                    past_date.add(date);
                    past_cnt.add(Integer.parseInt(String.valueOf(map.get(date))));

                    Log.e("past_steps의 날짜 가져오기", String.valueOf(past_date));
                    Log.e("past_steps의 걸음 수 가져오기", String.valueOf(past_cnt));

                }

                int year = MonthActivity.syear;
                int month = MonthActivity.smonth;
                int day = MonthActivity.sday;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Calendar cal = Calendar.getInstance();

                cal.set(year, month, day); //월은 -1해줘야 해당월로 인식
                BarChart chart = findViewById(R.id.barchart);


                ArrayList NoOfEmp = new ArrayList();


                for(int j = 0; j < past_cnt.size(); j++){

                    Log.e("past_cnt-size", String.valueOf(past_cnt.size()));
                    String date_value = String.valueOf(past_date.get(j));
                    Log.e("date_value", String.valueOf(past_date.get(j)));
                    String substring = date_value.substring(6);
                    Log.e("subSTring", substring);
                    NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(past_cnt.get(j))), Integer.parseInt(substring) - 1));
                    Log.e("past_cnt_value", String.valueOf(past_cnt.get(j)));
                    Log.e("jjj", String.valueOf(j));
                    Log.e("past_cnt보기", String.valueOf(past_cnt));
                    if(j == past_cnt.size()){
                        break;
                    }
                }


                ArrayList cday = new ArrayList();

                for(int i=1 ; i<=cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
                    cday.add(String.valueOf(i));
                }

                BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
                BarData data = new BarData(cday, bardataset);      // MPAndroidChart v3.X 오류 발생
                final int[] MY_COLORS = {Color.rgb(255,255,255), Color.rgb(207,227,255),
                        Color.rgb(150,194,255), Color.rgb(97,157,242), Color.rgb(40,99,176), Color.rgb(14,73,156),Color.rgb(0,40,97)};
                //bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for(int c: MY_COLORS) colors.add(c);
                chart.setDescription(null);
                chart.getLegend().setEnabled(false);
                chart.setNoDataText("차트를 보려면 클릭하세요");
                bardataset.setColors(colors);
                chart.setData(data);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Log.e("past_cnt-size22", String.valueOf(past_cnt.size()));



        /*int year = MonthActivity.syear;
        int month = MonthActivity.smonth;
        int day = MonthActivity.sday;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        cal.set(year, month, day); //월은 -1해줘야 해당월로 인식
        BarChart chart = findViewById(R.id.barchart);


        ArrayList NoOfEmp = new ArrayList();*/


        /*for(int i = 0; i < past_cnt.size(); i++){
            Log.e("past_cnt-size", String.valueOf(past_cnt.size()));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(past_cnt.get(i))), i));
            Log.e("past_cnt_value", String.valueOf(past_cnt.get(i)));
        }

        Log.e("왜 안 나와222", "!!!!!!!!!!!!");

        ArrayList cday = new ArrayList();

        for(int i=1 ; i<=cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            cday.add(String.valueOf(i));
        }

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
        BarData data = new BarData(cday, bardataset);      // MPAndroidChart v3.X 오류 발생
        final int[] MY_COLORS = {Color.rgb(255,255,255), Color.rgb(207,227,255),
                Color.rgb(150,194,255), Color.rgb(97,157,242), Color.rgb(40,99,176), Color.rgb(14,73,156),Color.rgb(0,40,97)};
        //bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        bardataset.setColors(colors);
        chart.setData(data);*/
    }

   /* DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {

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
    };*/

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), PedoActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
    }
}