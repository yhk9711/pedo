package com.example.lets_walk_firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    //  String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {
        //  PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, PedoActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PedoActivity.cnt=0;
        FirebasePost user = new FirebasePost();
        user.WriteStep(PedoActivity.my_id, PedoActivity.cnt);
        Log.d("알람서비스!!!!!!!!!", "걸음수 바뀜ㅁㅁㅁㅁㅁ");
        Toast.makeText(context.getApplicationContext(),"alarm!", Toast.LENGTH_LONG).show();
    }

}