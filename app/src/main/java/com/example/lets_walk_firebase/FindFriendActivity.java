package com.example.lets_walk_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

//전체 : 친구 아이디를 검색하고 친구 찾기 누름 없는 아이디면 아이디를 찾을 수 없다고 뜸 있는 아이디면 다음 액티비티로 넘어감
//1. 내가 입력한 아이디랑 디비의 아이디랑 일치하는게 있는지 확인
//2. 일치 안하면 없는 아이디라는 토스트 메세지, 일치하면 그 아이디의 이름, 나이, 성별 받아와서 setText해줌
//3. 친구 추가 버튼 누르면 현재 사용자의 친구 리스트에 그 친구들이 들어감

public class FindFriendActivity extends AppCompatActivity {
    EditText Friend_ID;
    String dt_id;
    Button find;
    private DatabaseReference databaseReference;
    String name;
    String age;
    String gender;
    String my_id;
//    String my_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findfriend);
        find = (Button)findViewById(R.id.find_friend2);
        Friend_ID = (EditText) findViewById(R.id.friend_id);
        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        my_id=null;
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
//                            my_id= dt_id;
                            Map<String, String> map = (Map) dt.getValue();

                            while (dt.getKey().equals(Friend_ID.getText().toString())) {
                                name = map.get("name");
                                age = String.valueOf(map.get("age"));
                                gender = String.valueOf(map.get("gender"));
                                Log.d("id", dt_id);
                                Log.d("name", name);
                                Log.d("age", age);
                                Log.d("gender", gender);
                                //Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), FoundFriendActivity.class);
                                intent.putExtra("id", dt_id);
//                                intent.putExtra("my_id", my_id);
                                intent.putExtra("name", name);
                                intent.putExtra("age", age);
                                intent.putExtra("gender", gender);
                                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
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


}
