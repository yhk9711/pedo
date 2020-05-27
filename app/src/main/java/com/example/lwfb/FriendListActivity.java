package com.example.lwfb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FriendListActivity extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener{
    String fname;
    String fstep;
    private DatabaseReference databaseReference;
    List<String> friendlist =new ArrayList<String>();
    String fid;

//    Button find_friend = (Button) findViewById(R.id.find_friend);
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
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
//        Intent i4 = getIntent();
//        i4.getStringExtra("my_id");
//        Bundle bundle4 = getIntent().getExtras();
//        if (bundle4 != null) {
//            my_id = bundle4.getString("my_id");
//            Log.d("my_id",my_id);
//        }
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
    }
    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {
        ListViewBtnItem item ;
        int i ;

        if (list == null) {
            list = new ArrayList<ListViewBtnItem>() ;
        }

        // 순서를 위한 i 값을 1로 초기화.
        i = 1 ;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while(child.hasNext()) {
                    DataSnapshot dt = child.next();
                    fid = dt.getKey();
                    int j =0;
                    Map<String, String> map = (Map) dt.getValue();

                    while (dt.getKey().equals(friendlist.get(j))){
                        fname = map.get("name");
                        fstep = map.get("step");
                        Log.d("fname", fname);
                        Log.d("fstep", fstep);
                        j++;
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 아이템 생성.
        item = new ListViewBtnItem() ;
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.firstimg)) ;
        item.setText(Integer.toString(i)+"등" + "   "+ fname+ "   "+ fstep +"걸음") ;
        list.add(item) ;
//        i++ ;
//
//        item = new ListViewBtnItem() ;
//        item.setIcon(ContextCompat.getDrawable(this, R.drawable.secondimg)) ;
//        item.setText(Integer.toString(i)+"등" + "   "+name+ "   "+ Integer.toString(step) +"걸음") ;
//        list.add(item) ;
//        i++ ;
//
//        item = new ListViewBtnItem() ;
//        item.setIcon(ContextCompat.getDrawable(this, R.drawable.thirdimg)) ;
//        item.setText(Integer.toString(i)+"등"+ "   " +name+ "   "+ Integer.toString(step) +"걸음") ;
//        list.add(item) ;
//        i++ ;
//
//        item = new ListViewBtnItem() ;
//        item.setIcon(ContextCompat.getDrawable(this, R.drawable.norank)) ;
//        item.setText(Integer.toString(i)+"등"+ "   " +name+ "   "+ Integer.toString(step) +"걸음") ;
//        list.add(item) ;
//


        return true ;
    }


    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position+1) + " Item is selected..", Toast.LENGTH_SHORT).show() ;
    }
}
