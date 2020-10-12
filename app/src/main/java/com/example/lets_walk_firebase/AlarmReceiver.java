package com.example.lets_walk_firebase;

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

    @Override
    public void onReceive(Context context, Intent intent) {

        FirebasePost user = new FirebasePost();

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");

        PedoActivity.steps.set(PedoActivity.index, PedoActivity.cnt);
        //user.steps = PedoActivity.steps;

        PedoActivity.cnt=0;
        PedoActivity.index++;
        PedoActivity.pedo_index++;
        if(PedoActivity.index == 7){
            PedoActivity.index = 0;
            PedoActivity.pedo_index = 0;
        }
        user.WriteIndex(PedoActivity.my_id, PedoActivity.index);
        user.WriteStep(PedoActivity.my_id, PedoActivity.cnt);

        Log.d("알람서비스!!!!!!!!!", "걸음수 바뀜ㅁㅁㅁㅁㅁ");

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