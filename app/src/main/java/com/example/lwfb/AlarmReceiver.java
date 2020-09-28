package com.example.lwfb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PedoActivity.cnt=0;
        FirebasePost user = new FirebasePost();
        user.WriteStep(PedoActivity.my_id, PedoActivity.cnt);
        //Toast.makeText(context.getApplicationContext(),"alarm!", Toast.LENGTH_LONG).show();
    }
}
