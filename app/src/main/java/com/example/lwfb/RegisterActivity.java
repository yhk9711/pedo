package com.example.lwfb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mPostReference;

    Button btn_Update;
    Button btn_Insert;
    Button btn_Select;
    EditText edit_ID;
    EditText edit_PW;
    EditText edit_Name;
    EditText edit_Age;
    TextView text_ID;
    TextView text_PW;
    TextView text_Name;
    TextView text_Age;
    TextView text_Gender;
    CheckBox check_Man;
    CheckBox check_Woman;

    String ID;
    String PW;
    String name;
    int step = 0;
    int goal_step = 10000;

    public List<String> friends = new ArrayList<String>();

    long age;

    String gender = "";
    String sort = "id";

    ArrayAdapter<String> arrayAdapter;



    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        btn_Insert = (Button) findViewById(R.id.btn_insert);

        btn_Insert.setOnClickListener(this);

        edit_ID = (EditText) findViewById(R.id.edit_id);

        edit_PW = (EditText) findViewById(R.id.edit_pw);

        edit_Name = (EditText) findViewById(R.id.edit_name);

        edit_Age = (EditText) findViewById(R.id.edit_age);

        text_ID = (TextView) findViewById(R.id.text_id);

        text_PW = (TextView) findViewById(R.id.text_pw);

        text_Name = (TextView) findViewById(R.id.text_name);

        text_Age = (TextView) findViewById(R.id.text_age);

        text_Gender= (TextView) findViewById(R.id.text_gender);

        check_Man = (CheckBox) findViewById(R.id.check_man);

        check_Man.setOnClickListener(this);

        check_Woman = (CheckBox) findViewById(R.id.check_woman);

        check_Woman.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        getFirebaseDatabase();

        btn_Insert.setEnabled(true);

    }



    public void setInsertMode(){

        edit_ID.setText("");

        edit_PW.setText("");

        edit_Name.setText("");

        edit_Age.setText("");

        check_Man.setChecked(false);

        check_Woman.setChecked(false);

        btn_Insert.setEnabled(true);


    }



    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {

        @Override

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.e("On Click", "position = " + position);

            Log.e("On Click", "Data: " + arrayData.get(position));

            String[] tempData = arrayData.get(position).split("\\s+");

            Log.e("On Click", "Split Result = " + tempData);

            edit_ID.setText(tempData[0].trim());

            edit_PW.setText(tempData[1].trim());

            edit_Name.setText(tempData[2].trim());

            edit_Age.setText(tempData[3].trim());

            if(tempData[4].trim().equals("Man")){

                check_Man.setChecked(true);

                gender = "남";

            }else{

                check_Woman.setChecked(true);

                gender = "여";

            }

            edit_ID.setEnabled(false);

            btn_Insert.setEnabled(false);

            btn_Update.setEnabled(true);

        }

    };




    public boolean IsExistID(){

        boolean IsExist = arrayIndex.contains(ID);

        return IsExist;

    }



    public void postFirebaseDatabase(boolean add){

        friends.add(ID);
        FirebasePost.friends=friends;
        Log.d("regis.fr", String.valueOf(friends));

        mPostReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();

        Map<String, Object> postValues = null;

        if(add){

            FirebasePost post = new FirebasePost(ID, PW, name, age, gender, step, goal_step, friends);
            postValues = post.toMap();

        }

        childUpdates.put("/MEMBER/" + ID, postValues);

        mPostReference.updateChildren(childUpdates);
    }


    public void getFirebaseDatabase(){

        ValueEventListener postListener = new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());

                arrayData.clear();

                arrayIndex.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    String key = postSnapshot.getKey();

                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);

                    String[] info = {get.id, get.pw, get.name, String.valueOf(get.age), get.gender, String.valueOf(get.step), String.valueOf(get.goal_step)};


                    arrayIndex.add(key);

                    Log.d("getFirebaseDatabase", "key: " + key);

                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3] + info[4] + info[5] + info[6]);

                }

                arrayAdapter.clear();

                arrayAdapter.addAll(arrayData);

                arrayAdapter.notifyDataSetChanged();

            }



            @Override

            public void onCancelled(DatabaseError databaseError) {

                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());

            }

        };

        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("MEMBER").orderByChild(sort);
        sortbyAge.addListenerForSingleValueEvent(postListener);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_insert:

                ID = edit_ID.getText().toString();

                PW = edit_PW.getText().toString();

                name = edit_Name.getText().toString();

                age = Long.parseLong(edit_Age.getText().toString());

                if(!IsExistID()){

                    postFirebaseDatabase(true);
                    getFirebaseDatabase();

                    setInsertMode();
                    Toast.makeText(getApplicationContext(),"회원가입에 성공하셨습니다.",Toast.LENGTH_LONG).show();

                } else{
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 ID 입니다. 다른 ID로 설정해주세요.", Toast.LENGTH_LONG).show();
                }

                edit_ID.requestFocus();
                edit_ID.setCursorVisible(true);

                Intent intent = new Intent(getApplicationContext(), com.example.lwfb.MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case R.id.check_man:

                check_Woman.setChecked(false);
                gender = "남";
                break;


            case R.id.check_woman:
                check_Man.setChecked(false);
                gender = "여";
                break;


        }
    }

}