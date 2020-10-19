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

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {
    //  String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    private DatabaseReference databaseReference;
    String dt_id;
    String dt_step;
    //int index;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("알람서비스eeeeee!!!!!!!!!", "걸음수 바뀜ㅁㅁㅁㅁㅁ");

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch(day){
            case 1:
                PedoActivity.index = 6; //일요일
                break;
            case 2:
                PedoActivity.index = 0; //월요일
                break;
            case 3:
                PedoActivity.index = 1; //화요일
                break;
            case 4:
                PedoActivity.index = 2; //수요일
                break;
            case 5:
                PedoActivity.index = 3; //목요일
                break;
            case 6:
                PedoActivity.index = 4; //금요일
                break;
            case 7:
                PedoActivity.index = 5; //토요일
                break;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
        /*for (int i = 0 ; i < 7 ; i++) {
            //Log.e("cntlist", Integer.toString(RegisterActivity.cntlistreg.get(i)));
        }*/
        FirebasePost user = new FirebasePost();
        user.WriteIndex(PedoActivity.my_id, PedoActivity.index);
        //user.WriteStep(PedoActivity.my_id, PedoActivity.cnt);
        if(PedoActivity.index == 0){
            user.WriteZeroSteps(PedoActivity.my_id, 6, PedoActivity.cnt);
        } else{
            user.WriteZeroSteps(PedoActivity.my_id, PedoActivity.index - 1, PedoActivity.cnt);
        }


       /* Log.e("인덱스 값 alarm 전", String.valueOf(PedoActivity.index));
        PedoActivity.index++;

        Log.e("인덱스 값 alarm 후", String.valueOf(PedoActivity.index));
        if (PedoActivity.index == 7){
            PedoActivity.index=0;
        }*/
        //user.WriteIndex(PedoActivity.my_id, PedoActivity.index);
        //user.WriteSteps(PedoActivity.my_id, PedoActivity.cntlist);

        //cntlist.set(0,PedoActivity.cnt);
        PedoActivity.cnt = 0;
        user.WriteStep(PedoActivity.my_id, PedoActivity.cnt);
        user.WriteZeroSteps(PedoActivity.my_id, PedoActivity.index, PedoActivity.cnt);


        //cntlist.add(6,PedoActivity.cnt);
        //cntlist.add(0,cntlist.get(1));

//        user.WriteSteps(PedoActivity.my_id, PedoActivity.cntlist);
        //user.WriteSteps(PedoActivity.my_id);
        //Toast.makeText(context.getApplicationContext(),"alarm!", Toast.LENGTH_LONG).show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FirebasePost user2 = new FirebasePost();
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while(child.hasNext()) {
                    DataSnapshot dt = child.next();
                    dt_id = dt.getKey();
                    if (!dt_id.equals(PedoActivity.my_id)) {
                        Map<String, String> map = (Map) dt.getValue();
                        dt_step = String.valueOf(map.get("step"));

                        user2.WriteIndex(dt_id, PedoActivity.index);
                        user2.WriteZeroSteps(dt_id, PedoActivity.index - 1, Integer.parseInt(dt_step));
                        user2.WriteStep(dt_id, 0);
                        user2.WriteZeroSteps(dt_id, PedoActivity.index, 0);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}