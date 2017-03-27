package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
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
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

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
        lineChart = (LineChart)findViewById(R.id.lineChart);



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

    private void prepareChart()
    {

        // no description text

        // enable touch gestures
  /*      lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setPinchZoom(true);
        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setViewPortOffsets(10f, 10f, 10f, 10f);*/
    }
    private void buildChart(LineChart lineChart, Float MinY, Float MaxY)
    {
/*        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);

               XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);
        //  xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(16f);
        xAxis.setTextColor(Color.BLUE);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelsToSkip(0);
        xAxis.setDrawGridLines(true);
        //xAxis.setTextColor(Color.rgb(255, 192, 56));


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //     leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);*/
        lineChart.setVisibleXRangeMaximum(65f);

    }

    void computeStimmungsBarometer (final Date Startdate, final Date EndDate,final LineChart lineChart){
        try {
            final ArrayList<String> xAXES = new ArrayList<>();
            final ArrayList<Entry> yAXESsin = new ArrayList<>();
        URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() + "/StimmungabfrageScore/");
        final Firebase root = new Firebase(url.toString());

        root.addValueEventListener(new ValueEventListener() {

                                       // Hier kriegst du den Knoten date zurueck
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           String x = "";
                                           float y = 0;
                                           float max_y = 0;
                                           float min_y = 0;
                                           boolean flag = false;
                                           int j  = 0;
                                           //final String sDate = dataSnapshot.getKey();

                                           // dataSnapshot.getKey() declares which strategy the notification belongs to (Stimmungsabgabe....)
                                           // the child.key of dataSnapshop declare the unique datetime of the notification
                                           for (DataSnapshot child : dataSnapshot.getChildren()) {
                                               // Here I get the time
                                               final String sDate = child.getKey();// Date
                                               Date d = new Date();

                                               // Here I have V or N

                                               int i = 1;
                                               for (DataSnapshot child2L : child.getChildren()) {
                                                   final String sTime = child2L.getKey();

                                                   // Now convert the string date to real date
                                                   d = DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " " + sTime);
                                                   if (!d.before(Startdate) && !d.after(EndDate)) {
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
                                               if (flag) {
                                                   y = y / i;
                                                   // Now we have the days
                                                   x  = sDate ;
                                                   max_y = Math.max(y, max_y);
                                                   min_y = Math.min(min_y, y);
                                                   xAXES.add(x);
                                                   yAXESsin.add(new Entry(y,j));

                                                   flag = false;
                                                   j++;
                                               }
                                           }

                                           if (xAXES.size() != 0) {
/*                                               ArrayList<String> xAXES = new ArrayList<>();
                                               ArrayList<Entry> yAXESsin = new ArrayList<>();
                                               double x1 = 0 ;
                                               int numDataPoints = 1000;
                                               for(int i=0;i<numDataPoints;i++){
                                                   float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x1)));
                                                   x1 = x1 + 0.1;
                                                   yAXESsin.add(new Entry(sinFunction,i));
                                                   xAXES.add(i, String.valueOf(x1));
                                               }
                                               String[] xaxes = new String[xAXES.size()];
                                               for(int i=0; i<xAXES.size();i++){
                                                   if (i < 300)
                                                       xaxes[i] = "12.03.2017";
                                                   else if(i >= 300 && i <= 600)
                                                        xaxes[i] = "13.03.2017";
                                                   else
                                                       xaxes[i] = "14.03.2017";
                                               }*/

                                               ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();


                                               LineDataSet lineDataSet2 = new LineDataSet(yAXESsin,"sin");
                                               lineDataSet2.setDrawCircles(false);
                                               lineDataSet2.setColor(Color.RED);


                                               lineDataSets.add(lineDataSet2);

                                               lineChart.invalidate();
                                               //lineChart.setVisibleYRangeMaximum(3f,YAxis.AxisDependency.LEFT);
                                               //lineChart.getXAxis().setLabelsToSkip(0);
                                               lineChart.getAxisLeft().resetAxisMinValue();
                                               lineChart.getAxisLeft().setAxisMinValue((float)Math.floor(min_y) - 0.5f);
                                               lineChart.getAxisLeft().setAxisMaxValue((float)Math.ceil(max_y) + 0.5f);
                                               lineChart.getAxisRight().setTextColor(Color.WHITE);
                                               lineChart.setExtraLeftOffset( 10);
                                               lineChart.setData(new LineData(xAXES,lineDataSets));
                                               lineChart.setDragEnabled(true   );
                                               lineChart.setScaleEnabled(true);
                                               lineChart.setHighlightPerDragEnabled(true);
                                               Paint p = lineChart.getPaint(Chart.PAINT_INFO);
                                               p.setTextSize(Utils.convertDpToPixel(5));
                                               //lineChart.setVisibleXRangeMaximum(65f);



                                           }
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


