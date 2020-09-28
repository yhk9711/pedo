package com.example.lets_walk_firebase;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class FoundFriendActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private View drawerView;

    EditText Friend_ID;
    String dt_id;
    Button find;
    private DatabaseReference databaseReference;
    String name;
    String age;
    String gender;
    String my_id;

    List<String> afriend=new ArrayList<String>();

    String id_value;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundfriend);
        find = (Button)findViewById(R.id.find_friend);
        Friend_ID = (EditText) findViewById(R.id.edit_id);
        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        my_id=null;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        TextView names = (TextView) findViewById(R.id.nameofuser);
        names.setText("" + PedoActivity.my_name + " 님");
        Button logout = (Button) findViewById(R.id.button);
        Button friendlist = (Button) findViewById(R.id.friendlist);
        Button notice = (Button) findViewById(R.id.notice);
        Button hometraining = (Button) findViewById(R.id.hometraining);
        Button maps = (Button) findViewById(R.id.maps);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해 SharedPreferences를 불러옴
                Intent intent = new Intent(FoundFriendActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //auto에 들어있는 모든 정보를 기기에서 지움
                editor.clear();
                editor.commit();
                Toast.makeText(FoundFriendActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoundFriendActivity.this, FriendListActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoundFriendActivity.this, NoticeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        hometraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoundFriendActivity.this, HomeTrainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoundFriendActivity.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        String name_value = null;
        Intent i = getIntent();
        i.getStringExtra("name");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name_value = bundle.getString("name");
            Log.d("Fname", name_value);
            fillTextView(R.id.friendname, name_value);
        }
        String age_value = null;
        Intent i2 = getIntent();
        i2.getStringExtra("age");
        Bundle bundle2 = getIntent().getExtras();
        if (bundle2 != null) {
            age_value = bundle2.getString("age");
            Log.d("Fage", age_value);
            fillTextView(R.id.friendage, age_value);
        }
        String gender_value = null;
        Intent i3 = getIntent();
        i3.getStringExtra("gender");
        Bundle bundle3 = getIntent().getExtras();
        if (bundle3 != null) {
            gender_value = bundle3.getString("gender");
            Log.d("Fgender", gender_value);
            fillTextView(R.id.friendgender, gender_value);
        }
        Log.d("my_id", PedoActivity.my_id);


        final Button add_friend = (Button) findViewById(R.id.add_friend);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_value = null; //친구 아이디
                Intent i5 = getIntent();
                i5.getStringExtra("id");
                Bundle bundle5 = getIntent().getExtras();
                if (bundle5 != null) {
                    id_value = bundle5.getString("id");
                    Log.d("Fid", id_value);
                }

                Log.d("dt_id",id_value);
                afriend = PedoActivity.friends;
                int a = 0;

                for(String s : afriend){
                    Log.d("s", s);
                    Log.d("friends", String.valueOf(PedoActivity.friends));

                    if (s.equals(id_value)) {
                        Log.d("s", s);
                        Log.d("id_value",id_value);
                        Toast.makeText(getApplicationContext(), "이미 친구 관계입니다", Toast.LENGTH_SHORT).show();

                        a = 1;
                        break;
                    } else;
                }

                if(a == 0) {

                    PedoActivity.friends.add(id_value);
                    Toast.makeText(getApplicationContext(), "친구 추가가 완료 되었습니다.", Toast.LENGTH_SHORT).show();

                    dtid(id_value);

                    databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(id_value);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String, List<String>> map3 = (Map) dataSnapshot.getValue();
                            Log.d("id_Value", id_value);

                            List<String> friend = map3.get("friends");
                            Log.d("friend", String.valueOf(friend));

                            friend.add(PedoActivity.my_id);
                            FirebasePost pp = new FirebasePost();
                            pp.WriteFriends(id_value, friend);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                        while(child.hasNext()) {
                            DataSnapshot dt = child.next();
                            dt_id = dt.getKey();

                            Map<String, String> map = (Map) dt.getValue();

                            while (dt.getKey().equals(Friend_ID.getText().toString())) {
                                name = map.get("name");
                                age = String.valueOf(map.get("age"));
                                gender = String.valueOf(map.get("gender"));
                                Log.d("id", dt_id);
                                Log.d("name", name);
                                Log.d("age", age);
                                Log.d("gender", gender);

                                Intent intent = new Intent(getApplicationContext(), FoundFriendActivity.class);
                                intent.putExtra("id", dt_id);
                                intent.putExtra("name", name);
                                intent.putExtra("age", age);
                                intent.putExtra("gender", gender);
                                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return;
                            }
                        }
                        Log.d("error","존재하지 않는다고!!! =");
                        Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다.",Toast.LENGTH_SHORT).show();


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void fillTextView(int id, String text) {
        TextView View = (TextView) findViewById(id);
        View.setText(text);

    }
    private void dtid(String id){

        FirebasePost p = new FirebasePost();

        Log.d("fire.fr", String.valueOf(FirebasePost.friends));
        p.WriteFriends(PedoActivity.my_id, PedoActivity.friends);
        Intent intent = new Intent(getApplicationContext(), FindFriendActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
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
        Intent intent = new Intent(getApplicationContext(), FindFriendActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

}