package com.example.lets_walk_firebase;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyInfo extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private EditText edit_goal;
    private EditText edit_height;
    Button btn_changed;
    Button btn_changed_height;
    Button cal;
    private TextView average_step;
    ArrayAdapter<String> arrayAdapter;
    public static int syear = 0;
    public static int smonth = 0;
    public static int sday = 0;

    static ArrayList<String> arrayIndex = new ArrayList<String>();

    static ArrayList<String> arrayData = new ArrayList<String>();

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            Intent intent = new Intent(MyInfo.this, StatisticsActivity.class);
            startActivity(intent);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        average_step = (TextView) findViewById(R.id.averagestep3);
        cal = (Button) findViewById(R.id.cal);
        btn_changed = (Button) findViewById(R.id.btn_changed);
        edit_goal = (EditText) findViewById(R.id.edit_goal);
        btn_changed.setEnabled(true);
        btn_changed_height = (Button) findViewById(R.id.btn_changed_height);
        edit_height = (EditText) findViewById(R.id.edit_height);
        btn_changed_height.setEnabled(true);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        TextView name = (TextView) findViewById(R.id.nameofuser);
        name.setText("" + PedoActivity.my_name + " 님");
        //drawerLayout.openDrawer(drawerView);
        Button logout = (Button) findViewById(R.id.button);
        Button friendlist = (Button) findViewById(R.id.friendlist);
        Button notice = (Button) findViewById(R.id.notice);
        Button hometraining = (Button) findViewById(R.id.hometraining);
        Button maps = (Button) findViewById(R.id.maps);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해 SharedPreferences를 불러옴
                Intent intent = new Intent(MyInfo.this, MainActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //auto에 들어있는 모든 정보를 기기에서 지움
                editor.clear();
                editor.commit();
                Toast.makeText(MyInfo.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfo.this, FriendListActivity.class);
                startActivity(intent);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfo.this, NoticeActivity.class);
                startActivity(intent);
            }
        });
        hometraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfo.this, HomeTrainActivity.class);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfo.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btn_changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.btn_changed:

                        PedoActivity.goal = Integer.parseInt(edit_goal.getText().toString());
                        setInsertMode();
                        Toast.makeText(getApplicationContext(), "목표 걸음 수가 변경되었습니다.", Toast.LENGTH_LONG).show();

                        String id_value = null;
                        Intent i = getIntent();
                        i.getStringExtra("id");
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            id_value = bundle.getString("id");
                        }

                        FirebasePost user = new FirebasePost();
                        user.WriteGoal(id_value, PedoActivity.goal);

                        break;


                }
            }
        });

        btn_changed_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.btn_changed_height:

                        PedoActivity.height = Integer.parseInt(edit_height.getText().toString());
                        setInsertMode();
                        Toast.makeText(getApplicationContext(), "키가 변경되었습니다.", Toast.LENGTH_LONG).show();

                        String id_value = null;
                        Intent i = getIntent();
                        i.getStringExtra("id");
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            id_value = bundle.getString("id");
                        }

                        FirebasePost user = new FirebasePost();
                        user.WriteHeight(id_value, PedoActivity.height);
                        PedoActivity.sheight=Integer.toString(PedoActivity.height);

                        break;


                }
            }
        });



        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.example.lets_walk_firebase.PickerActivity pd = new com.example.lets_walk_firebase.PickerActivity();
                pd.setListener(d);
                pd.show(getSupportFragmentManager(), "PickerActivity");
            }
                /*Intent intent = new Intent(MyInfo.this, com.example.lets_walk_firebase.PickerActivity.class);
                startActivity(intent);*/

        });

        BarChart chart = findViewById(R.id.barchart);

        ArrayList NoOfEmp = new ArrayList();

        //Log.e("int 값 프린트 ", String.valueOf(PedoActivity.cntlist.get(0)));
        //Log.e("long 값 프린트 ", String.valueOf(Long.valueOf(PedoActivity.cntlist.get(0))));


        ArrayList year = new ArrayList();

        Date currentTime = Calendar.getInstance().getTime();

        /*Date dDate = new Date();
        dDate = new Date(dDate.getTime()+(1000*60*60*24*-1));
        SimpleDateFormat dSdf = new SimpleDateFormat("yyyy/MM/dd EE", Locale.KOREA);
        String yesterday = dSdf.format(dDate);*/

        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
//        String year = yearFormat.format(currentTime);
//        String month = monthFormat.format(currentTime);
//        String day = dayFormat.format(currentTime);

        //Log.d("webnautes", year + "년 " + month + "월 " + day + "일 " + weekDay + "요일");


        if (PedoActivity.index == 0){
            year.add("화");
            year.add("수");
            year.add("목");
            year.add("금");
            year.add("토");
            year.add("일");
            year.add("월");
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(1))), 0));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(2))), 1));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(3))), 2));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(4))), 3));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(5))), 4));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(6))), 5));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(0))), 6));
        }
        if (PedoActivity.index == 1){
            year.add("수");
            year.add("목");
            year.add("금");
            year.add("토");
            year.add("일");
            year.add("월");
            year.add("화");
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(2))), 0));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(3))), 1));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(4))), 2));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(5))), 3));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(6))), 4));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(0))), 5));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(1))), 6));
        }
        if (PedoActivity.index == 2){
            year.add("목");
            year.add("금");
            year.add("토");
            year.add("일");
            year.add("월");
            year.add("화");
            year.add("수");
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(3))), 0));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(4))), 1));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(5))), 2));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(6))), 3));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(0))), 4));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(1))), 5));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(2))), 6));
        }
        if (PedoActivity.index == 3){
            year.add("금");
            year.add("토");
            year.add("일");
            year.add("월");
            year.add("화");
            year.add("수");
            year.add("목");
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(4))), 0));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(5))), 1));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(6))), 2));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(0))), 3));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(1))), 4));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(2))), 5));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(3))), 6));
        }
        if (PedoActivity.index == 4){
            year.add("토");
            year.add("일");
            year.add("월");
            year.add("화");
            year.add("수");
            year.add("목");
            year.add("금");
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(5))), 0));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(6))), 1));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(0))), 2));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(1))), 3));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(2))), 4));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(3))), 5));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(4))), 6));
        }
        if (PedoActivity.index == 5){
            year.add("일");
            year.add("월");
            year.add("화");
            year.add("수");
            year.add("목");
            year.add("금");
            year.add("토");
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(6))), 0));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(0))), 1));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(1))), 2));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(2))), 3));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(3))), 4));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(4))), 5));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(5))), 6));
        }
        if (PedoActivity.index == 6){
            year.add("월");
            year.add("화");
            year.add("수");
            year.add("목");
            year.add("금");
            year.add("토");
            year.add("일");
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(0))), 0));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(1))), 1));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(2))), 2));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(3))), 3));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(4))), 4));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(5))), 5));
            NoOfEmp.add(new BarEntry(Long.parseLong(String.valueOf(PedoActivity.cntlist.get(6))), 6));
        }

        //일주일 평균 걸음 수 계산하여 보여주기
        int sum = 0;
        for(int i = 0; i < PedoActivity.cntlist.size(); i++){
            sum += Integer.parseInt(String.valueOf(PedoActivity.cntlist.get(i)));
            //Log.e("cntlist", String.valueOf(sum));
        }
        int avg = sum/7;
        average_step.setText("" + avg);




        BarDataSet bardataset = new BarDataSet(NoOfEmp, "");
        //chart.animateY(5000);
        BarData data = new BarData(year, bardataset);      // MPAndroidChart v3.X 오류 발생
        final int[] MY_COLORS = {Color.rgb(255,255,255), Color.rgb(207,227,255),
                Color.rgb(150,194,255), Color.rgb(97,157,242), Color.rgb(40,99,176), Color.rgb(14,73,156),Color.rgb(0,40,97)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        bardataset.setColors(colors);
        //dataSet.setColors(colors);
        chart.setData(data);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);


    }

    public void setInsertMode() {
        edit_goal.setText("");
        edit_height.setText("");

    }


    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {

        @Override

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String[] tempData = arrayData.get(position).split("\\s+");

            edit_goal.setText(tempData[0].trim());

            btn_changed.setEnabled(true);

            edit_height.setText(tempData[1].trim());
            btn_changed_height.setEnabled(true);

        }

    };


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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), PedoActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}