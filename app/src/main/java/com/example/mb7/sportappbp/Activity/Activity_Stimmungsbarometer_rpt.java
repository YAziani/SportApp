package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.mb7.sportappbp.Adapters.StimmungsAngabeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Activity_Stimmungsbarometer_rpt extends AppCompatActivity {

    ProgressDialog pd;
    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__stimmungsbarometer_rpt);


        lineChart = (LineChart)findViewById(R.id.linechart);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setHighlightPerDragEnabled(true);

/*
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

        lineDataSets.add(lineDataSet1);


        lineChart.setData(new LineData( lineDataSets));

        lineChart.setVisibleXRangeMaximum(65f);
*/






    }
    public  Date addDays(Date d, int days)
    {
        d.setTime( d.getTime() + (long)days*1000*60*60*24 );
        return d;
    }

    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString( R.string.wird_geladen));
        pd.show();
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        computeStimmungsBarometer(addDays(new Date(),-4) ,new Date(), lineChart);
    }

    private void buildChart(LineChart lineChart)
    {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
/*        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
// days to milisecons
                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }


        });*/
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    void computeStimmungsBarometer (final Date Startdate, final Date EndDate,final LineChart lineChart){
        try {
            final ArrayList<Entry> xyAxesSin = new ArrayList<>();
        URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() + "/StimmungabfrageScore/");
        final Firebase root = new Firebase(url.toString());

        root.addValueEventListener(new ValueEventListener() {

                                       // Hier kriegst du den Knoten date zurueck
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                            float x = 0;
                                            float y = 0;
                                           float max_y = 0; float min_y = 0;
                                           boolean flag = false;
                                           //final String sDate = dataSnapshot.getKey();

                                           // dataSnapshot.getKey() declares which strategy the notification belongs to (Stimmungsabgabe....)
                                           // the child.key of dataSnapshop declare the unique datetime of the notification
                                           int j = 1;
                                           for (DataSnapshot child : dataSnapshot.getChildren()) {
                                               // Here I get the time
                                               final String sDate= child.getKey();// Date
                                               Date d = new Date();

                                               // Here I have V or N

                                               int i = 1;
                                               for (DataSnapshot child2L : child.getChildren()) {
                                                   final String sTime = child2L.getKey();

                                                   // Now convert the string date to real date
                                                   d = DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " " + sTime);
                                                   if (!Startdate.after(d) &&  !d.before( EndDate) ){
                                                       i++;


                                                       // create the object and insert it in the list
                                                       for (DataSnapshot child3L : child2L.getChildren()) {
                                                           if (child3L.getKey().equals("Score Stimmungsbarometer")) {
                                                               y += ((Long) child3L.getValue()).floatValue();
                                                               flag = true;
                                                           }
                                                       }
                                                   }

                                               }
                                               // get the average of on day
                                               if(flag) {
                                                   y = y / i;
                                                   // Now we have the days
                                                   x = TimeUnit.MILLISECONDS.toDays(DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " " + "00:00:00").getTime());
                                                   max_y = Math.max(y,max_y); min_y = Math.min(min_y,y);
                                                   xyAxesSin.add(new Entry(j,y));
                                                   j++;
                                                   flag = false;
                                               }
                                           }

                                           LineDataSet lineDataSet1 = new LineDataSet(xyAxesSin,"Dates-Values-MB");
                                           lineDataSet1.setDrawCircles(false); // otherwise it would not be smooth
                                           lineDataSet1.setColor(Color.BLUE);
                                           //lineDataSets.add(lineDataSet1);


                                           lineChart.setData(new LineData( lineDataSet1));
                                           //lineChart.setVisibleYRangeMaximum(max_y,);
                                           Activity_Stimmungsbarometer_rpt.this.buildChart(lineChart);




                                           // close the progress dialog
                                           pd.dismiss();


                                       }
                                       @Override
                                       public void onCancelled(FirebaseError firebaseError) {

                                       }
                                   }
        );
    }
        catch (Exception ex)
    {
        Log.e("Exc",ex.getMessage());
    }

    }

}


