package com.example.lets_walk_firebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeTrainActivity extends AppCompatActivity {
    Button high;
    Button medi;
    Button low;
    private DrawerLayout drawerLayout;
    private View drawerView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hometrain);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        TextView name = (TextView) findViewById(R.id.nameofuser);
        name.setText("" + PedoActivity.my_name + " 님");
        //drawerLayout.openDrawer(drawerView);
        Button logout = (Button) findViewById(R.id.button);
        Button friendlist = (Button) findViewById(R.id.friendlist);
        Button notice = (Button) findViewById(R.id.notice);
        Button myInfo = (Button) findViewById(R.id.myinfo);
        Button maps = (Button) findViewById(R.id.maps);

        high = (Button)findViewById(R.id.high);
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.lets_walk_firebase.HighActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        medi = (Button)findViewById(R.id.medi);
        medi.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.lets_walk_firebase.MediActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        low = (Button)findViewById(R.id.low);
        low.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.lets_walk_firebase.LowActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해 SharedPreferences를 불러옴
                Intent intent = new Intent(HomeTrainActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //auto에 들어있는 모든 정보를 기기에서 지움
                editor.clear();
                editor.commit();
                Toast.makeText(HomeTrainActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeTrainActivity.this, MyInfo.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeTrainActivity.this, com.example.lets_walk_firebase.FriendListActivity.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeTrainActivity.this, NoticeActivity.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeTrainActivity.this, MapsActivity.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
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

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), PedoActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

}