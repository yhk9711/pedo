package com.example.lwfb;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FriendListActivity extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener {
   /* String name = "박연휘";
    Integer step=100000;
    String my_id;*/

    String fname;
    String fstep;
    private DatabaseReference databaseReference;
    public static List<String> friendlist = new ArrayList<String>();

    List<String> friendname = new ArrayList<String>();
    List<String> friendstep = new ArrayList<String>();

    private DrawerLayout drawerLayout;
    private View drawerView;

    String dt_id;
    String fid;
    ListViewBtnItem item;
    public static ArrayList<ListViewBtnItem> list2 = new ArrayList<ListViewBtnItem>();

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

                startActivity(intent);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendListActivity.this, NoticeActivity.class);

                startActivity(intent);
            }
        });
        hometraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendListActivity.this, HomeTrainActivity.class);

                startActivity(intent);
            }
        });


        Button button1 = (Button) findViewById(R.id.find_friend);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), FindFriendActivity.class);
                startActivity(intent);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(PedoActivity.my_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, List<String>> map3 = (Map) dataSnapshot.getValue();

                List<String> friend = map3.get("friends");
                Log.d("friend", String.valueOf(friend));
                friendlist = friend;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while (child.hasNext()) {
                    DataSnapshot dt = child.next();
                    dt_id = dt.getKey();
                    Map<String, String> map = (Map) dt.getValue();
                    Log.d("friendlist size", String.valueOf(friendlist.size()));
                    for (int j = 0; j < friendlist.size(); j++) {
                        Log.d("j", String.valueOf(j));
                        fid = friendlist.get(j);
                        Log.d("fid", fid);
                        Log.d("dt_id", dt_id);
                        if (dt.getKey().equals(fid)) { //DB의 id와 friendlist의 id가 같다면 해당 id에 대한 정보를 DB에서 가져옴
                            fname = map.get("name");
                            fstep = String.valueOf(map.get("step"));
                            friendname.add(fname);
                            friendstep.add(fstep);
//                            Log.d("fname", fname);
//                            Log.d("fstep", fstep);
                            Log.d("friendname", String.valueOf(friendname));
                            Log.d("friendstep", String.valueOf(friendstep));

                            //Log.d("list2", String.valueOf(list2));

                        }

                    }
                }
                if (0<friendname.size()){
                    item = new ListViewBtnItem();
                    item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.firstimg));
                    item.setText(1+ "등" + "   " + friendname.get(0) + "   " + friendstep.get(0) + "걸음");
                    Log.d("item", item.toString());
                    //Log.d("fk", String.valueOf(friendname));
                    list2.add(item);
                    Log.d("list2", list2.toString());
                }
                if (1<friendname.size()){
                    item = new ListViewBtnItem();
                    item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.secondimg));
                    item.setText(2+ "등" + "   " + friendname.get(1) + "   " + friendstep.get(1) + "걸음");
                    Log.d("item", item.toString());
                    //Log.d("fk", String.valueOf(friendname));
                    list2.add(item);
                    Log.d("list2", list2.toString());
                }

                if (2<friendname.size()){
                    item = new ListViewBtnItem();
                    item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thirdimg));
                    item.setText(3+ "등" + "   " + friendname.get(2) + "   " + friendstep.get(2) + "걸음");
                    Log.d("item", item.toString());
                    //Log.d("fk", String.valueOf(friendname));
                    list2.add(item);
                    Log.d("list2", list2.toString());

                }
                if(3<friendname.size() ) {
                    for (int k = 3; k < friendname.size(); k++) {
                        item = new ListViewBtnItem();
                        item.setText((k + 1) + "등" + "   " + friendname.get(k) + "   " + friendstep.get(k) + "걸음");
                        Log.d("item", item.toString());
                        //Log.d("fk", String.valueOf(friendname));
                        list2.add(item);
                        Log.d("list2", list2.toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ListView listview;
        ListViewBtnAdapter adapter;
        //ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>();

        //Log.d("items", String.valueOf(items));
        // items 로드.
        loadItemsFromDB(list2);


        // Adapter 생성
        adapter = new ListViewBtnAdapter(this, R.layout.listview, list2, this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            }
        });

       /* my_id = null;
//        Intent i4 = getIntent();
//        i4.getStringExtra("my_id");
//        Bundle bundle4 = getIntent().getExtras();
//        if (bundle4 != null) {
//            my_id = bundle4.getString("my_id");
//            Log.d("my_id",my_id);
//        }
        Intent intent = new Intent(getApplicationContext(), FindFriendActivity.class);
        intent.putExtra("my_id", my_id);*/
    }

    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {
        //ListViewBtnItem item;
        int i;
        if (list == null) {
            list = new ArrayList<ListViewBtnItem>();
        }




        /*readData(new FirebaseCallback() {

            @Override
            public void onCallback(List<ListViewBtnItem> list2, List<String> L1, List<String> L2) {

                Log.d("L1", L1.toString());
                Log.d("L2", String.valueOf(L2));
                //list2 = list;
                //list2 = list;

                item = new ListViewBtnItem();
                item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.firstimg));
                item.setText("1등" + "   " + L1.get(0) + "   " + L2.get(0) + "걸음");
                //Log.d("fk", String.valueOf(friendname));
                list2.add(item);
                Log.d("list", list2.toString());



            }
        });*/


        // 순서를 위한 i 값을 1로 초기화.
        i = 1;



        /*databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while (child.hasNext()) {
                    DataSnapshot dt = child.next();
                    dt_id = dt.getKey();
                    Map<String, String> map = (Map) dt.getValue();
                    Log.d("friendlist size", String.valueOf(friendlist.size()));
                    for (int j = 0; j < friendlist.size(); j++) {
                        Log.d("j", String.valueOf(j));
                        fid = friendlist.get(j);
                        Log.d("fid", fid);
                        Log.d("dt_id", dt_id);
                        if (dt.getKey().equals(fid)) {
                            fname = map.get("name");
                            fstep = String.valueOf(map.get("step"));
                            friendname.add(fname);
                            friendstep.add(fstep);
//                            Log.d("fname", fname);
//                            Log.d("fstep", fstep);
                            Log.d("friendname", String.valueOf(friendname));
                            Log.d("friendstep", String.valueOf(friendstep));

                            item = new ListViewBtnItem();
                            item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.firstimg));
                            item.setText("1등" + "   " + friendname.get(0) + "   " + friendstep.get(0) + "걸음");
                            Log.d("fk", String.valueOf(friendname));
                            list2.add(item);

                            Log.d("list2", String.valueOf(list2));

                        }

                    }

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        /*item = new ListViewBtnItem();
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.firstimg));
        item.setText(Integer.toString(i) + "등" + "   " + "   " +  "걸음");
        Log.d("fk", String.valueOf(friendname));
        list2.add(item);
        Log.d("list", String.valueOf(list));
        list = list2;*/
        Log.d("loadDB 안에 list", String.valueOf(list));


        return true;
    }

    /*private void readData(final FirebaseCallback firebaseCallback){
        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while (child.hasNext()) {
                    DataSnapshot dt = child.next();
                    dt_id = dt.getKey();
                    Map<String, String> map = (Map) dt.getValue();
                    Log.d("friendlist size", String.valueOf(friendlist.size()));
                    for (int j = 0; j < friendlist.size(); j++) {
                        Log.d("j", String.valueOf(j));
                        fid = friendlist.get(j);
                        Log.d("fid", fid);
                        Log.d("dt_id", dt_id);
                        if (dt.getKey().equals(fid)) { //DB의 id와 friendlist의 id가 같다면 해당 id에 대한 정보를 DB에서 가져옴
                            fname = map.get("name");
                            fstep = String.valueOf(map.get("step"));
                            friendname.add(fname);
                            friendstep.add(fstep);
//                            Log.d("fname", fname);
//                            Log.d("fstep", fstep);
                            Log.d("friendname", String.valueOf(friendname));
                            Log.d("friendstep", String.valueOf(friendstep));

                            //Log.d("list2", String.valueOf(list2));

                        }

                    }

                }

                firebaseCallback.onCallback(list2, friendname, friendstep);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private interface FirebaseCallback {
        void onCallback(List<ListViewBtnItem> list, List<String> L1, List<String> L2);

    }
*/

    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position + 1) + " Item is selected..", Toast.LENGTH_SHORT).show();
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