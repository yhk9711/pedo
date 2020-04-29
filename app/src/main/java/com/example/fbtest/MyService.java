package com.example.fbtest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    private static final String TAG = "MyService";

    public MyService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_REDELIVER_INTENT; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            String command = intent.getStringExtra("command");
            String name = intent.getStringExtra("name");

            //Log.d(TAG, "전달받은 데이터: " + command+ ", " +name);

            try{
                Thread.sleep(5000); //5초동안 정지
            } catch(Exception e) {}

            Intent showIntent = new Intent(getApplicationContext(), PedoActivity.class);

            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            showIntent.putExtra("command", "show");
            showIntent.putExtra("name", name + " from service.");

            startActivity(showIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
