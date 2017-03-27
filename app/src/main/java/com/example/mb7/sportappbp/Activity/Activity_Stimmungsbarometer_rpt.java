package com.example.mb7.sportappbp.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mb7.sportappbp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Activity_Stimmungsbarometer_rpt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__stimmungsbarometer_rpt);
        LineChart lineChart;


            lineChart = (LineChart)findViewById(R.id.linechart);

            ArrayList<String> xAxes = new ArrayList<>();
            ArrayList<Entry> yAxesSin = new ArrayList<>();
            ArrayList<Entry> yAxesCos = new ArrayList<>();
            ArrayList<Entry> xAxes2 = new ArrayList<>();

            float    x = 0;

            int numDataPoints = 1000;
            for(int i = 0; i < numDataPoints; i++)
            {
                float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x)));
                float cosFunction= Float.parseFloat(String.valueOf(Math.cos(x)));
                x = x + 0.1f;
                yAxesSin.add(new Entry(x, sinFunction));
                yAxesCos.add(new Entry( x,cosFunction));
                xAxes.add(i,String.valueOf(x));


            }
            String[] xaxes = new String[xAxes.size()];
            for(int i = 0; i<xAxes.size(); i++)
            {
                xaxes[i] = xAxes.get(i).toString();
            }

            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            LineDataSet lineDataSet1 = new LineDataSet(yAxesCos,"cos");
            lineDataSet1.setDrawCircles(false); // otherwise it would not be smooth
            lineDataSet1.setColor(Color.BLUE);

            LineDataSet lineDataSet2 = new LineDataSet(yAxesSin,"sin");
            lineDataSet2.setDrawCircles(false);
            lineDataSet1.setColor(Color.RED);

            lineDataSets.add(lineDataSet1);
            lineDataSets.add(lineDataSet2);


            lineChart.setData(new LineData( lineDataSets));

            lineChart.setVisibleXRangeMaximum(65f);


        }

    }


