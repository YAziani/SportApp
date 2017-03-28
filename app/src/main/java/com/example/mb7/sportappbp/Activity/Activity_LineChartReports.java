package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by M.Braei on 28.03.2017.
 */

public abstract class Activity_LineChartReports extends AppCompatActivity {
    ProgressDialog pd;
    LineChart lineChart;

    protected Date addDays(Date d, int days) {
        d.setTime(d.getTime() + (long) days * 1000 * 60 * 60 * 24);
        return d;
    }

    abstract float convertValue(DataSnapshot object);

    protected void computeDraw(final Date Startdate, final Date EndDate, final LineChart lineChart, final String ReportName, final String legend) {
        try {
            // first show the progress dialog
            pd = new ProgressDialog(this);
            pd.setMessage(getString(R.string.wird_geladen));
            pd.show();

            // then run
            final ArrayList<String> xAXES = new ArrayList<>();
            final ArrayList<Entry> yAXES = new ArrayList<>();
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() + "/StimmungabfrageScore/");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    // Hier kriegst du den Knoten date zurueck
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String x = "";
                                                        float y = 0;
                                                        float max_y = 0;
                                                        float min_y = 0;
                                                        boolean flag = false;
                                                        int j = 0;
                                                        //final String sDate = dataSnapshot.getKey();

                                                        // dataSnapshot.getKey() declares which strategy the notification belongs to (Stimmungsabgabe....)
                                                        // the child.key of dataSnapshop declare the unique datetime of the notification
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            // Here I get the time
                                                            final String sDate = child.getKey();// Date
                                                            Date d = new Date();


                                                            int i = 0;
                                                            for (DataSnapshot child2L : child.getChildren()) {
                                                                String sTime = child2L.getKey();
                                                                // it could be unique key instead of time
                                                                if (sTime.charAt(0) == '-')
                                                                    sTime = "00:00:00";

                                                                // Now convert the string date to real date
                                                                d = DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " " + sTime);
                                                                if (!d.before(Startdate) && !d.after(EndDate)) {
                                                                    i++;

                                                                    // Here I have V or N
                                                                    for (DataSnapshot child3L : child2L.getChildren()) {

                                                                        // create the object and insert it in the list
                                                                        for (DataSnapshot child4L : child3L.getChildren()) {
                                                                            if (child4L.getKey().equals(ReportName)) {
                                                                                y += convertValue(child4L);
                                                                                flag = true;
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                            // get the average of on day
                                                            if (flag) {
                                                                y = y / i;
                                                                // Now we have the days
                                                                x = DAL_Utilities.ConvertDateToString(DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " 00:00:00"));
                                                                //x  = sDate ;
                                                                max_y = Math.max(y, max_y);
                                                                min_y = Math.min(min_y, y);
                                                                xAXES.add(x);
                                                                yAXES.add(new Entry(y, j));

                                                                flag = false;
                                                                j++;
                                                                y = 0;
                                                            }
                                                        }

                                                        if (xAXES.size() != 0) {

                                                            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();


                                                            LineDataSet lineDataSet2 = new LineDataSet(yAXES, legend);
                                                            lineDataSet2.setDrawCircles(false);
                                                            lineDataSet2.setColor(Color.RED);


                                                            lineDataSets.add(lineDataSet2);

                                                            lineChart.invalidate();
                                                            //lineChart.setVisibleYRangeMaximum(3f,YAxis.AxisDependency.LEFT);
                                                            //lineChart.getXAxis().setLabelsToSkip(0);
                                                            lineChart.getAxisLeft().resetAxisMinValue();
                                                            lineChart.getAxisLeft().setAxisMinValue((float) Math.floor(min_y) - 0.5f);
                                                            lineChart.getAxisLeft().setAxisMaxValue((float) Math.ceil(max_y) + 0.5f);
                                                            lineChart.getAxisRight().setTextColor(Color.WHITE);
                                                            lineChart.setExtraLeftOffset(15);
                                                            lineChart.setData(new LineData(xAXES, lineDataSets));
                                                            lineChart.setDragEnabled(true);
                                                            lineChart.setScaleEnabled(true);
                                                            lineChart.setDescription("");
                                                            lineChart.setHighlightPerDragEnabled(true);

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
        } catch (Exception ex) {
            Log.e("Exc", ex.getMessage());
        }

    }

    protected void computeDrawDiff(final Date Startdate, final Date EndDate, final LineChart lineChart, final String ReportName, final String legend) {
        try {
            // first show the progress dialog
            pd = new ProgressDialog(this);
            pd.setMessage(getString(R.string.wird_geladen));
            pd.show();

            // then run
            final ArrayList<String> xAXES = new ArrayList<>();
            final ArrayList<Entry> yAXES_Vor = new ArrayList<>();
            final ArrayList<Entry> yAXES_Nach = new ArrayList<>();
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() + "/StimmungabfrageScore/");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    // Hier kriegst du den Knoten date zurueck
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String x = "";
                                                        float y_Vor = 0;
                                                        float y_Nach = 0;
                                                        float max_y = 0;
                                                        float min_y = 0;
                                                        boolean flag = false;
                                                        int j = 0;
                                                        //final String sDate = dataSnapshot.getKey();

                                                        // dataSnapshot.getKey() declares which strategy the notification belongs to (Stimmungsabgabe....)
                                                        // the child.key of dataSnapshop declare the unique datetime of the notification
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            // Here I get the time
                                                            final String sDate = child.getKey();// Date
                                                            Date d = new Date();


                                                            int i = 0;
                                                            for (DataSnapshot child2L : child.getChildren()) {
                                                                final String sTime = child2L.getKey();

                                                                // Now convert the string date to real date
                                                                d = DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " " + sTime);
                                                                if (!d.before(Startdate) && !d.after(EndDate)) {
                                                                    i++;

                                                                    // Here I have V or N
                                                                    for (DataSnapshot child3L : child2L.getChildren()) {
                                                                        Boolean Vor = child3L.getKey().equals("V") ? true : false;

                                                                        // create the object and insert it in the list
                                                                        for (DataSnapshot child4L : child3L.getChildren()) {
                                                                            if (child4L.getKey().equals(ReportName)) {
                                                                                if (Vor)
                                                                                    y_Vor += convertValue(child4L);
                                                                                else
                                                                                    y_Nach += convertValue(child4L);
                                                                                flag = true;
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                            // get the average of on day
                                                            if (flag) {
                                                                y_Vor = y_Vor / i;
                                                                y_Nach = y_Nach / i;
                                                                // Now we have the days
                                                                x = DAL_Utilities.ConvertDateToString(DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " 00:00:00"));
                                                                //x  = sDate ;
                                                                max_y = Math.max(y_Nach, Math.max(y_Vor, max_y));
                                                                min_y = Math.min(y_Nach, Math.min(min_y, y_Nach));
                                                                xAXES.add(x);
                                                                yAXES_Vor.add(new Entry(y_Vor, j));
                                                                yAXES_Nach.add(new Entry(y_Nach, j));

                                                                flag = false;
                                                                j++;
                                                                y_Vor = y_Nach = 0;
                                                            }
                                                        }

                                                        if (xAXES.size() != 0) {

                                                            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();


                                                            LineDataSet lineDataSet1 = new LineDataSet(yAXES_Nach, "Nach dem Training");
                                                            lineDataSet1.setDrawCircles(false);
                                                            lineDataSet1.setColor(Color.BLUE);

                                                            LineDataSet lineDataSet2 = new LineDataSet(yAXES_Vor, "Vor dem Training");
                                                            lineDataSet2.setDrawCircles(false);
                                                            lineDataSet2.setColor(Color.RED);


                                                            lineDataSets.add(lineDataSet2);
                                                            lineDataSets.add(lineDataSet1);

                                                            lineChart.invalidate();
                                                            //lineChart.setVisibleYRangeMaximum(3f,YAxis.AxisDependency.LEFT);
                                                            //lineChart.getXAxis().setLabelsToSkip(0);
                                                            lineChart.getAxisLeft().resetAxisMinValue();
                                                            lineChart.getAxisLeft().setAxisMinValue((float) Math.floor(min_y) - 0.5f);
                                                            lineChart.getAxisLeft().setAxisMaxValue((float) Math.ceil(max_y) + 0.5f);
                                                            lineChart.getAxisRight().setTextColor(Color.WHITE);
                                                            lineChart.setExtraLeftOffset(15);
                                                            lineChart.setData(new LineData(xAXES, lineDataSets));
                                                            lineChart.setDragEnabled(true);
                                                            lineChart.setScaleEnabled(true);
                                                            lineChart.setDescription("");
                                                            lineChart.setHighlightPerDragEnabled(true);

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
        } catch (Exception ex) {
            Log.e("Exc", ex.getMessage());
        }

    }

    protected void computeScores(final Date Startdate, final Date EndDate, final LineChart lineChart, final String ReportName, final String legend) {
        try {
            // first show the progress dialog
            pd = new ProgressDialog(this);
            pd.setMessage(getString(R.string.wird_geladen));
            pd.show();

            // then run
            final ArrayList<String> xAXES = new ArrayList<>();
            final ArrayList<Entry> yAXES = new ArrayList<>();
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() + "/Diary/");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    // Hier kriegst du den Knoten date zurueck
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String x = "";
                                                        float y = 0;
                                                        float max_y = 0;
                                                        float min_y = 0;
                                                        boolean flag = false;
                                                        int j = 0;
                                                        //final String sDate = dataSnapshot.getKey();

                                                        // dataSnapshot.getKey() declares which strategy the notification belongs to (Stimmungsabgabe....)
                                                        // the child.key of dataSnapshop declare the unique datetime of the notification
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            // Here I get the time
                                                            final String sDate = child.getKey();// Date
                                                            Date d = new Date();


                                                            int i = 0;
                                                            for (DataSnapshot child2L : child.getChildren()) {
                                                                final String sTime = child2L.getKey();

                                                                // Now convert the string date to real date
                                                                d = DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " " + sTime);
                                                                if (!d.before(Startdate) && !d.after(EndDate)) {
                                                                    i++;


                                                                    // create the object and insert it in the list
                                                                    for (DataSnapshot child4L : child2L.getChildren()) {
                                                                        if (child4L.getKey().equals(ReportName)) {
                                                                            y += convertValue(child4L);
                                                                            flag = true;
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                            // get the average of on day
                                                            if (flag) {
                                                                y = y / i;
                                                                // Now we have the days
                                                                x = DAL_Utilities.ConvertDateToString(DAL_Utilities.ConvertFirebaseKeyStringToDateTime(sDate + " 00:00:00"));
                                                                //x  = sDate ;
                                                                max_y = Math.max(y, max_y);
                                                                min_y = Math.min(min_y, y);
                                                                xAXES.add(x);
                                                                yAXES.add(new Entry(y, j));

                                                                flag = false;
                                                                j++;
                                                                y = 0;
                                                            }
                                                        }

                                                        if (xAXES.size() != 0) {

                                                            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();


                                                            LineDataSet lineDataSet2 = new LineDataSet(yAXES, legend);
                                                            lineDataSet2.setDrawCircles(false);
                                                            lineDataSet2.setColor(Color.RED);


                                                            lineDataSets.add(lineDataSet2);

                                                            lineChart.invalidate();
                                                            //lineChart.setVisibleYRangeMaximum(3f,YAxis.AxisDependency.LEFT);
                                                            //lineChart.getXAxis().setLabelsToSkip(0);
                                                            lineChart.getAxisLeft().resetAxisMinValue();
                                                            lineChart.getAxisLeft().setAxisMinValue((float) Math.floor(min_y) - 0.5f);
                                                            lineChart.getAxisLeft().setAxisMaxValue((float) Math.ceil(max_y) + 0.5f);
                                                            lineChart.getAxisRight().setTextColor(Color.WHITE);
                                                            lineChart.setExtraLeftOffset(15);
                                                            lineChart.setData(new LineData(xAXES, lineDataSets));
                                                            lineChart.setDragEnabled(true);
                                                            lineChart.setScaleEnabled(true);
                                                            lineChart.setDescription("");
                                                            lineChart.setHighlightPerDragEnabled(true);

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
        } catch (Exception ex) {
            Log.e("Exc", ex.getMessage());
        }

    }

}