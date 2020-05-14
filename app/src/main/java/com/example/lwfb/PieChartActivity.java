package com.example.lwfb;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedo);
        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList Step = new ArrayList();
        int numtogoal = PedoActivity.goal-PedoActivity.cnt;
        Step.add(new Entry(PedoActivity.cnt, 0));
        Step.add(new Entry(numtogoal, 1));
        String nowstep = Integer.toString(PedoActivity.cnt);

        PieDataSet dataSet = new PieDataSet(Step,"");

        ArrayList year = new ArrayList();

        year.add("현재 걸음수");
        year.add("남은 걸음수");

        pieChart.setCenterText(nowstep);
        pieChart.setCenterTextSize(40f);
        pieChart.setCenterTextColor(Color.BLACK);

        PieData data = new PieData(year, dataSet);          // MPAndroidChart v3.X 오류 발생
        pieChart.setData(data);
        data.setValueTextSize(14f);
        //data.setValueTextColor(Color.TRANSPARENT);
        final int[] MY_COLORS = {Color.rgb(102,153,204), Color.rgb(255,255,255), Color.rgb(255,192,0),
                Color.rgb(127,127,127), Color.rgb(146,208,80), Color.rgb(0,176,80), Color.rgb(79,129,189)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        dataSet.setColors(colors);
        //dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieChart.animateXY(3000, 3000);

        pieChart.setDrawHoleEnabled(true);
        //pieChart.setHoleColorTransparent(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(80);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setDescription(null);
    }
}