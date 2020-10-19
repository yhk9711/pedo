package com.example.lwfb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

public class MonthActivity extends AppCompatActivity {
    Button MonthBtn;
    public static int syear=0;
    public static int smonth=0;
    public static int sday=0;

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            Intent intent=new Intent(MonthActivity.this,StatisticsActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);
        MonthBtn = (Button) findViewById(R.id.month_button);

        MonthBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                com.example.lwfb.PickerActivity pd = new com.example.lwfb.PickerActivity();
                pd.setListener(d);
                pd.show(getSupportFragmentManager(), "PickerActivity");
            }
        });
    }
}
//
//    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
//            Intent intent=new Intent(MonthActivity.this,StatisticsActivity.class);
//            startActivity(intent);
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button statisticsBtn = findViewById(R.id.statistics_button);
//
//        statisticsBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                PickerActivity pd = new PickerActivity();
//                pd.setListener(d);
//                pd.show(getSupportFragmentManager(), "YearMonthPicker");
//            }
//        });
//    }
//}