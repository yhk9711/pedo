package com.example.lets_walk_firebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private View drawerView;
    //    Button find_friend = (Button) findViewById(R.id.find_friend);
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        findViewById(R.id.notice1).setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        TextView name = (TextView) findViewById(R.id.nameofuser);
        name.setText("" + PedoActivity.my_name + " 님");
        //drawerLayout.openDrawer(drawerView);
        Button logout = (Button) findViewById(R.id.button);
        Button myInfo = (Button) findViewById(R.id.myinfo);
        Button friendlist = (Button) findViewById(R.id.friendlist);
        Button hometraining = (Button) findViewById(R.id.hometraining);
        Button maps = (Button) findViewById(R.id.maps);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(NoticeActivity.this, com.example.lets_walk_firebase.MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(NoticeActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, com.example.lets_walk_firebase.MyInfo.class);
                /*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*/
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, com.example.lets_walk_firebase.FriendListActivity.class);
                /*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*/
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        hometraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, com.example.lets_walk_firebase.HomeTrainActivity.class);
                /*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*/
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, com.example.lets_walk_firebase.MapsActivity.class);
                /*String id_value2 = null;
                Intent i2 = getIntent();
                i2.getStringExtra("id");
                Bundle bundle2 = getIntent().getExtras();
                if (bundle2 != null) {
                    id_value2 = bundle2.getString("id");
                    //Log.d("id", id_value2);
                }
                intent.putExtra("id", id_value2);
                intent.putExtra("name", user_name);*/
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notice1:
                new AlertDialog.Builder(this)
                        .setTitle("[필독] 5월 13일 앱 업데이트 사항 및 버그 수정")
                        .setMessage("1. 친구 검색 시 결과가 안나오는 오류가 수정되었습니다.\n 2. 홈트레이닝 영상이 5가지 추가되었습니다.\n 3. 걸음 수에 따른 음식 정보가 추가되었습니다 \n 항상 저희 앱을 사용해주시는 여러분께 감사드립니다 !  ")
                        .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                            }
                        })
                        .show(); // 팝업창 보여줌
                break;
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