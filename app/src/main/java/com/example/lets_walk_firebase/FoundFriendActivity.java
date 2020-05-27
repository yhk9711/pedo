package com.example.lets_walk_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoundFriendActivity extends AppCompatActivity {
    //    String dt_id = null;
    /*String name = null;
    String age = null;
    String gender = null;
    String dt_id = null;*/
    private TextView nameView;
    private TextView ageView;
    private TextView genderView;

    List<String> afriend=new ArrayList<String>();

    String id_value;

    private DatabaseReference databaseReference;
//    List<String> friends = new ArrayList<String>();
    //String my_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundfriend);

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
        Log.d("my_id",PedoActivity.my_id);
//        String my_id=null;
//        Intent i4 = getIntent();
//        i4.getStringExtra("my_id");
//        Bundle bundle4 = getIntent().getExtras();
//        if (bundle4 != null) {
//            my_id = bundle4.getString("my_id");
//            Log.d("my_id",my_id);
//        }


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
                        Intent intent = new Intent(FoundFriendActivity.this, FindFriendActivity.class);
                        startActivity(intent);
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


                    Intent intent = new Intent(FoundFriendActivity.this, FindFriendActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private void fillTextView(int id, String text) {
        TextView View = (TextView) findViewById(id);
        View.setText(text);

    }
    private void dtid(String id){
        //FirebasePost.friends.add(id);
        //Log.d("id",id);
        FirebasePost p = new FirebasePost();

//        p.WriteStep(PedoActivity.my_id, 34);
        Log.d("fire.fr", String.valueOf(FirebasePost.friends));
        p.WriteFriends(PedoActivity.my_id, PedoActivity.friends);
        Intent intent = new Intent(getApplicationContext(), com.example.lets_walk_firebase.FindFriendActivity.class);
        startActivity(intent);



    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.add_friend:
//                Toast.makeText(getApplicationContext(), "친구 추가가 완료 되었습니다.", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(FoundFriendActivity.this, FindFriendActivity.class);
//                startActivity(intent);
//                FirebasePost.friends.add(dt_id);
//                break;
//        }
//
//    }
}
