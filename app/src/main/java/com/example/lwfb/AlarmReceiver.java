package com.example.lwfb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class AlarmReceiver extends BroadcastReceiver {
    //  String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    private DatabaseReference databaseReference;
    String dt_id;
    public static int alcnt=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("알람서비스eeeeee!!!!!!!!!", "걸음수 바뀜ㅁㅁㅁㅁㅁ");

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        for (int i =0 ; i<7 ; i++) {
            //Log.e("cntlist", Integer.toString(RegisterActivity.cntlistreg.get(i)));
        }
        FirebasePost user = new FirebasePost();
        user.WriteStep(PedoActivity.my_id, PedoActivity.cnt);
        Log.e("인덱스 값 alarm 전", String.valueOf(PedoActivity.index));
        PedoActivity.index++;
        Log.e("인덱스 값 alarm 후", String.valueOf(PedoActivity.index));
        if (PedoActivity.index==7){
            PedoActivity.index=0;
        }
        user.WriteIndex(PedoActivity.my_id, PedoActivity.index);
        //user.WriteSteps(PedoActivity.my_id, PedoActivity.cntlist);
        user.WriteSteps(PedoActivity.my_id);
        alcnt = PedoActivity.cnt;
        //cntlist.set(0,PedoActivity.cnt);
        PedoActivity.cnt=0;

        //cntlist.add(6,PedoActivity.cnt);
        //cntlist.add(0,cntlist.get(1));

//        user.WriteSteps(PedoActivity.my_id, PedoActivity.cntlist);
        user.WriteSteps(PedoActivity.my_id);
        //Toast.makeText(context.getApplicationContext(),"alarm!", Toast.LENGTH_LONG).show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FirebasePost user2 = new FirebasePost();
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while(child.hasNext())
                {
                    DataSnapshot dt = child.next();
                    dt_id = dt.getKey();
                    //Map<String, String> map = (Map)dt.getValue();

                    user2.WriteStep(dt_id, 0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}