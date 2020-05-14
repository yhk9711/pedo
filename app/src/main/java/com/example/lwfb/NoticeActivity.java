package com.example.lwfb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {
    //    Button find_friend = (Button) findViewById(R.id.find_friend);
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        findViewById(R.id.notice1).setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notice1:
                new AlertDialog.Builder(this)
                        .setTitle("[필독] 5월 13일 앱 업데이트 사항 및 버그 수정")
                        .setMessage("1. 친구 검색 시 결과가 안나오는 오류가 수정되었습니다.\n 2. 홈트레이닝 영상이 5가지 추가되었습니다.\n 3. 걸음 수에 따른 음식 정보가 추가되었습니다 \n 항상 저희 앱을 사용해주시는 여러분께 감사드립니다 !  ")
                        .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                            }
                        })
                        .show(); // 팝업창 보여줌
                break;
        }
    }

}
