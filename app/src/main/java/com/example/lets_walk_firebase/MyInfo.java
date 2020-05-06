package com.example.lets_walk_firebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class MyInfo extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private EditText edit_goal;
    Button btn_changed;
    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex = new ArrayList<String>();

    static ArrayList<String> arrayData = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        btn_changed = (Button) findViewById(R.id.btn_changed);
        edit_goal = (EditText) findViewById(R.id.edit_goal);
        btn_changed.setEnabled(true);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        //drawerLayout.openDrawer(drawerView);
        Button logout = (Button) findViewById(R.id.button);
        TextView name = (TextView) findViewById(R.id.nameofuser);

        String user_name = null;
        Intent i = getIntent();
        i.getStringExtra("name");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_name = bundle.getString("name");
            //Log.d("id", id_value2);
        }
        name.setText("" + user_name + " 님");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(MyInfo.this, MainActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(MyInfo.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
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
                            //Log.d("id", id_value);
                        }

                        FirebasePost user = new FirebasePost();
                        user.WriteGoal(id_value, PedoActivity.goal);


                        break;


                }
            }
        });


    }

    public void setInsertMode() {
        edit_goal.setText("");

    }

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {

        @Override

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String[] tempData = arrayData.get(position).split("\\s+");

            edit_goal.setText(tempData[0].trim());

            btn_changed.setEnabled(true);

        }

    };

    /*
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_changed:

                PedoActivity.goal = Integer.parseInt(edit_goal.getText().toString());
                setInsertMode();
                Toast.makeText(getApplicationContext(),"목표 걸음 수가 변경되었습니다.",Toast.LENGTH_LONG).show();

                String id_value = null;
                Intent i = getIntent();
                i.getStringExtra("id");
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    id_value = bundle.getString("id");
                    //Log.d("id", id_value);
                }

                FirebasePost user = new FirebasePost();
                user.WriteGoal(id_value, PedoActivity.goal);


                break;


        }
    }
    */
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