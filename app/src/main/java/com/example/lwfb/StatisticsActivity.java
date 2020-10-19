package com.example.lwfb;


import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        int year = MonthActivity.syear;
        int month = MonthActivity.smonth;
        int day = MonthActivity.sday;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        cal.set(year, month, day); //월은 -1해줘야 해당월로 인식
        BarChart chart = findViewById(R.id.barchart);


        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(945f, 0));
        NoOfEmp.add(new BarEntry(1040f, 1));
        NoOfEmp.add(new BarEntry(1133f, 2));
        NoOfEmp.add(new BarEntry(1240f, 3));
        NoOfEmp.add(new BarEntry(1369f, 4));
        NoOfEmp.add(new BarEntry(1487f, 5));
        NoOfEmp.add(new BarEntry(1501f, 6));
        NoOfEmp.add(new BarEntry(1645f, 7));
        NoOfEmp.add(new BarEntry(1578f, 8));
        NoOfEmp.add(new BarEntry(1695f, 9));
        NoOfEmp.add(new BarEntry(143f, 10));
        NoOfEmp.add(new BarEntry(1325f, 11));
        NoOfEmp.add(new BarEntry(1465f, 12));
        NoOfEmp.add(new BarEntry(1095f, 13));
        NoOfEmp.add(new BarEntry(1135f, 14));
        NoOfEmp.add(new BarEntry(1095f, 15));
        NoOfEmp.add(new BarEntry(945f, 16));
        NoOfEmp.add(new BarEntry(1040f, 17));
        NoOfEmp.add(new BarEntry(1133f, 18));
        NoOfEmp.add(new BarEntry(Long.valueOf(PedoActivity.cnt), 19));

        ArrayList cday = new ArrayList();

        for(int i=1 ; i<=cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            cday.add(String.valueOf(i));
        }

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
        BarData data = new BarData(cday, bardataset);      // MPAndroidChart v3.X 오류 발생
        final int[] MY_COLORS = {Color.rgb(255,255,255), Color.rgb(207,227,255),
                Color.rgb(150,194,255), Color.rgb(97,157,242), Color.rgb(40,99,176), Color.rgb(14,73,156),Color.rgb(0,40,97)};
        //bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        bardataset.setColors(colors);
        chart.setData(data);
    }
}