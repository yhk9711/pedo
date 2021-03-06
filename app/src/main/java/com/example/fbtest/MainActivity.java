package com.example.fbtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class MainActivity extends AppCompatActivity {



    private DatabaseReference databaseReference;
    EditText checkId;
    EditText checkpw;
    Button login;
    Button register;
    String pass;
    String loginId, loginPwd;
    String dt_id;
    String step_num;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        checkId = (EditText)findViewById(R.id.checkId);
        checkpw = (EditText)findViewById(R.id.checkpw);
        login =(Button)findViewById(R.id.login);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd",null);

        if(loginId !=null && loginPwd != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    while(child.hasNext())
                    {
                        DataSnapshot dt = child.next();
                        dt_id = dt.getKey();
                        Map<String, String> map = (Map)dt.getValue();

                        if(dt.getKey().equals(loginId))
                        {
                            pass = map.get("pw");
                            step_num = String.valueOf(map.get("step"));

                            Log.d("password", pass);
                            Log.d("step", step_num);
                        }
                        if(loginId.equals(dt_id) && loginPwd.equals(pass)) {
                            Toast.makeText(MainActivity.this, loginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                            Log.d("자동로그인 성공함",pass);
                            Intent intent = new Intent(MainActivity.this, com.example.lets_walk_firebase.PedoActivity.class);
                            intent.putExtra("id", dt_id);
                            intent.putExtra("step", step_num);
                            startActivity(intent);
                            finish();
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"존재하지 않는 아이디이거나 비밀번호를 잘 못 입력하셨습니다.",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                        while(child.hasNext())
                        {
                            DataSnapshot dt = child.next();
                            dt_id = dt.getKey();
                            Map<String, String> map = (Map)dt.getValue();

                            if(dt.getKey().equals(checkId.getText().toString()))
                            {
                                pass = map.get("pw");
                                step_num = String.valueOf(map.get("step"));

                                Log.d("password", pass);
                                if(pass.equals(checkpw.getText().toString()))
                                {
                                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                    //아이디가 '부르곰'이고 비밀번호가 '네이버'일 경우 SharedPreferences.Editor를 통해
                                    //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("inputId", checkId.getText().toString());
                                    autoLogin.putString("inputPwd", checkpw.getText().toString());

                                    //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                                    autoLogin.commit();
                                    Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), com.example.lets_walk_firebase.PedoActivity.class);
                                    intent.putExtra("id", dt_id);
                                    intent.putExtra("step", step_num);
                                    startActivity(intent);
                                    //Intent theIntent = new Intent(this, PedoActivity.class);

                                    //startActivity(theIntent);
                                    //Map<String, Object> taskmap = new HashMap<String, Object>;
                                    //taskmap.put("")
                                    //finish();
                                    return;
                                }

                            }
                        }
                        Toast.makeText(getApplicationContext(),"존재하지 않는 아이디이거나 비밀번호를 잘 못 입력하셨습니다.",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}