package com.example.lets_walk_firebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;

public class MyInfo extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private EditText edit_goal;
    private EditText edit_height;
    Button btn_changed;
    Button btn_changed_height;
    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex = new ArrayList<String>();

    static ArrayList<String> arrayData = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
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
                Intent intent = new Intent(MyInfo.this, GPSActivity.class);
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

        BarChart chart = findViewById(R.id.barchart);

        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(700f, 0));
        NoOfEmp.add(new BarEntry(10400f, 1));
        NoOfEmp.add(new BarEntry(6532f, 2));
        NoOfEmp.add(new BarEntry(7893f, 3));
        NoOfEmp.add(new BarEntry(1239f, 4));
        NoOfEmp.add(new BarEntry(6789f, 5));
        NoOfEmp.add(new BarEntry(10500f, 6));

        ArrayList year = new ArrayList();

        year.add("5/08");
        year.add("5/09");
        year.add("5/10");
        year.add("5/11");
        year.add("5/12");
        year.add("5/13");
        year.add("5/14");
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