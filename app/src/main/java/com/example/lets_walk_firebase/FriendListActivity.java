package com.example.lets_walk_firebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener{
    String name = "박연휘";
    Integer step=100000;
    String my_id;
    private DrawerLayout drawerLayout;
    private View drawerView;
    //    Button find_friend = (Button) findViewById(R.id.find_friend);
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        TextView name = (TextView) findViewById(R.id.nameofuser);

        Log.d("이름", PedoActivity.my_name);

        name.setText("" + PedoActivity.my_name + " 님");

        //drawerLayout.openDrawer(drawerView);
        Button logout = (Button) findViewById(R.id.button);
        //Button friendlist = (Button) findViewById(R.id.friendlist);
        Button notice = (Button) findViewById(R.id.notice);
        Button myInfo = (Button) findViewById(R.id.myinfo);
        Button hometraining = (Button) findViewById(R.id.hometraining);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(FriendListActivity.this, MainActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(FriendListActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendListActivity.this, MyInfo.class);
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
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendListActivity.this, NoticeActivity.class);
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
            }
        });
        hometraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendListActivity.this, HomeTrainActivity.class);
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
            }
        });


        Button button1 = (Button)findViewById(R.id.find_friend);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), FindFriendActivity.class);
                startActivity(intent);
            }
        });
        ListView listview ;
        ListViewBtnAdapter adapter;
        ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>() ;

        // items 로드.
        loadItemsFromDB(items) ;

        // Adapter 생성
        adapter = new ListViewBtnAdapter(this, R.layout.listview, items, this) ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            }
        }) ;
        my_id=null;
//        Intent i4 = getIntent();
//        i4.getStringExtra("my_id");
//        Bundle bundle4 = getIntent().getExtras();
//        if (bundle4 != null) {
//            my_id = bundle4.getString("my_id");
//            Log.d("my_id",my_id);
//        }
        Intent intent = new Intent(getApplicationContext(), FindFriendActivity.class);
        intent.putExtra("my_id", my_id);
    }
    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {
        ListViewBtnItem item ;
        int i ;

        if (list == null) {
            list = new ArrayList<ListViewBtnItem>() ;
        }

        // 순서를 위한 i 값을 1로 초기화.
        i = 1 ;

        // 아이템 생성.
        item = new ListViewBtnItem() ;
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.firstimg)) ;
        item.setText(Integer.toString(i)+"등" + "   "+ name+ "   "+ Integer.toString(step) +"걸음") ;
        list.add(item) ;
        i++ ;

        item = new ListViewBtnItem() ;
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.secondimg)) ;
        item.setText(Integer.toString(i)+"등" + "   "+name+ "   "+ Integer.toString(step) +"걸음") ;
        list.add(item) ;
        i++ ;

        item = new ListViewBtnItem() ;
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.thirdimg)) ;
        item.setText(Integer.toString(i)+"등"+ "   " +name+ "   "+ Integer.toString(step) +"걸음") ;
        list.add(item) ;
        i++ ;

        item = new ListViewBtnItem() ;
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.norank)) ;
        item.setText(Integer.toString(i)+"등"+ "   " +name+ "   "+ Integer.toString(step) +"걸음") ;
        list.add(item) ;
        i++ ;

        item = new ListViewBtnItem() ;
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.norank)) ;
        item.setText(Integer.toString(i)+"등"+ "   " +name+ "   "+ Integer.toString(step) +"걸음") ;
        list.add(item) ;
        i++ ;

        item = new ListViewBtnItem() ;
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.norank)) ;
        item.setText(Integer.toString(i)+"등"+ "   " +name+ "   "+ Integer.toString(step) +"걸음") ;
        list.add(item) ;


        return true ;
    }


    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position+1) + " Item is selected..", Toast.LENGTH_SHORT).show() ;
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

}