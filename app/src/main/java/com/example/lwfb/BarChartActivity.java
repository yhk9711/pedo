package com.example.lwfb;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        BarChart chart = findViewById(R.id.barchart);

        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(700f, 0));
        NoOfEmp.add(new BarEntry(10400f, 1));
        NoOfEmp.add(new BarEntry(6532f, 2));
        NoOfEmp.add(new BarEntry(7893f, 3));
        NoOfEmp.add(new BarEntry(1239f, 4));
        NoOfEmp.add(new BarEntry(6789f, 5));
        NoOfEmp.add(new BarEntry(10500f, 6));

        ArrayList year = new ArrayList();

        year.add("5/08");
        year.add("5/09");
        year.add("5/10");
        year.add("5/11");
        year.add("5/12");
        year.add("5/13");
        year.add("5/14");
        BarDataSet bardataset = new BarDataSet(NoOfEmp, "");
        //chart.animateY(5000);
        BarData data = new BarData(year, bardataset);      // MPAndroidChart v3.X 오류 발생
        final int[] MY_COLORS = {Color.rgb(255,255,255), Color.rgb(207,227,255),
                 Color.rgb(150,194,255), Color.rgb(97,157,242), Color.rgb(40,99,176), Color.rgb(14,73,156),Color.rgb(0,40,97)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        bardataset.setColors(colors);
        //dataSet.setColors(colors);
        chart.setData(data);
    }
}