package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

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
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

public class Activity_bsa_rpt extends Activity_LineChartReports {

    @Override
    float convertValue(DataSnapshot object) {
        try {
            return ((Long) object.getValue()).floatValue();

        } catch (ClassCastException ex) {
            return ((Double) object.getValue()).floatValue();
        }
        //return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsa_rpt);
        lineChart = (LineChart) findViewById(R.id.lineChart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_report, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.thirtydays:
                computeDrawThree(addDays(new Date(), -30), new Date(), lineChart, Activity_bsa_rpt.this.getString(R
                        .string.erzielte_punkte));

                return true;
            case R.id.fourteendays:
                computeDrawThree(addDays(new Date(), -14), new Date(), lineChart, Activity_bsa_rpt.this.getString(R
                        .string.erzielte_punkte));
                return true;
            case R.id.sevendays:
                computeDrawThree(addDays(new Date(), -7), new Date(), lineChart, Activity_bsa_rpt.this.getString(R
                        .string.erzielte_punkte));
                return true;
            case R.id.alldays:
                computeDrawThree(null, new Date(), lineChart, Activity_bsa_rpt.this.getString(R.string
                        .erzielte_punkte));
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        computeDrawThree(addDays(new Date(), -7), new Date(), lineChart, Activity_bsa_rpt.this.getString(R.string
                .erzielte_punkte));
    }


    /**
     * @param Startdate set that start interval for the data. Set this to null if you want all data be displayed
     * @param EndDate
     * @param lineChart
     * @param legend
     */
    protected void computeDrawThree(final Date Startdate, final Date EndDate, final LineChart lineChart, final String
            legend) {
        try {
            // first show the progress dialog
            pd = new ProgressDialog(this);
            pd.setMessage(getString(R.string.wird_geladen));
            pd.show();

            // then run
            final ArrayList<String> xAXES = new ArrayList<>();
            final ArrayList<Entry> yAXES_Bewegung = new ArrayList<>();
            final ArrayList<Entry> yAXES_Sportscoring = new ArrayList<>();
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/BSAFragebogen/");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    // Hier kriegst du den Knoten date zurueck
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String x = "";
                                                        float y_Bewegung = 0;
                                                        float y_SportScoring = 0;
                                                        float max_y = 0;
                                                        float min_y = 0;
                                                        boolean flag = false;
                                                        int j = 0;

                                                        // dataSnapshot.getKey() declares which strategy the
                                                        // notification belongs to (Stimmungsabgabe....)
                                                        // the child.key of dataSnapshop declare the unique datetime
                                                        // of the notification
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            // Here I get the time
                                                            final String sDate = child.getKey();// Date
                                                            Date d = new Date();

                                                            final String sTime = "00:00:00";
                                                            d = DAL_Utilities.ConvertFirebaseKeyStringToDateTime
                                                                    (sDate + " " + sTime);

                                                            if (Startdate == null || (!d.before(Startdate) && !d
                                                                    .after(EndDate))) {
                                                                int i = 0;

                                                                i++;
                                                                for (DataSnapshot child2L : child.getChildren()) {

                                                                    // Now convert the string date to real date

                                                                    // create the object and insert it in the list
                                                                    if (child2L.getKey().equals("Bewegungsscoring")) {
                                                                        y_Bewegung += convertValue(child2L);
                                                                        flag = true;
                                                                    } else if (child2L.getKey().equals
                                                                            ("Sportscoring")) {
                                                                        y_SportScoring += convertValue(child2L);
                                                                        flag = true;
                                                                    } else if (child2L.getKey().equals
                                                                            ("Sportscoring")) {
                                                                        y_SportScoring += convertValue(child2L);
                                                                        flag = true;
                                                                }

                                                                // get the average of on day
                                                                if (flag) {
                                                                    // if one of them wasn't filled in an iteration,
                                                                    // we consider it like zero! Therefore i as a
                                                                    // devider for all
                                                                    y_SportScoring = y_SportScoring / i;
                                                                    y_Bewegung = y_Bewegung / i;
                                                                    // Now we have the days
                                                                    x = DAL_Utilities.ConvertDateToString
                                                                            (DAL_Utilities
                                                                .ConvertFirebaseKeyStringToDateTime(sDate + " 00:00:00"));
                                                                    //x  = sDate ;
                                                                    max_y = Math.max(y_SportScoring, Math.max(y_Bewegung, max_y));
                                                                    min_y = Math.min(y_SportScoring, Math.min(min_y, y_Bewegung));
                                                                    xAXES.add(x);

                                                                    yAXES_Bewegung.add(new Entry(y_Bewegung, j));
                                                                    yAXES_Sportscoring.add(new Entry(y_SportScoring,j));


                                                                    flag = false;
                                                                    j++;
                                                                     y_SportScoring = y_Bewegung = 0;
                                                                }
                                                            }
                                                        }
                                                        if (xAXES.size() != 0) {
                                                            // set the datasets of our chart
                                                            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
                                                            LineDataSet lineDataSet1 = new LineDataSet
                                                                    (yAXES_Bewegung, Activity_bsa_rpt.this.getString
                                                                            (R.string.bewegungswerte));
                                                            lineDataSet1.setDrawCircles(true);
                                                            lineDataSet1.setColor(Color.RED);
                                                            LineDataSet lineDataSet3 = new LineDataSet
                                                                    (yAXES_Sportscoring, Activity_bsa_rpt.this
                                                                            .getString(R.string.sportwerte));
                                                            lineDataSet3.setDrawCircles(true);
                                                            lineDataSet3.setColor(Color.BLUE);
                                                            lineDataSets.add(lineDataSet1);
                                                            lineDataSets.add(lineDataSet3);

                                                            lineChart.invalidate();

                                                            // set the visual properties of the chart
                                                            lineChart.getAxisLeft().resetAxisMinValue();
                                                            lineChart.getAxisLeft().setAxisMinValue((float) Math
                                                                    .floor(min_y) - 0.5f);
                                                            lineChart.getAxisLeft().setAxisMaxValue((float) Math.ceil
                                                                    (max_y) + 0.5f);
                                                            lineChart.getAxisRight().setTextColor(Color.WHITE);
                                                            lineChart.setExtraLeftOffset(15);
                                                            lineChart.setExtraRightOffset(10);
                                                            lineChart.setData(new LineData(xAXES, lineDataSets));
                                                            lineChart.setDragEnabled(true);
                                                            lineChart.setScaleEnabled(true);
                                                            lineChart.setDescription("");
                                                            lineChart.setHighlightPerDragEnabled(true);
                                                            lineChart.getAxisRight().setDrawGridLines(false);
                                                            lineChart.getLegend().setEnabled(false);


                                                        }
                                                        // after all the data process has been done
                                                        // close the progress dialog
                                                        pd.dismiss();

                                                    }}

                                                    @Override
                                                    public void onCancelled(FirebaseError firebaseError) {

                                                    }
                                                }
            );
        }
        catch (Exception ex) {
            Log.e("Exc", ex.getMessage());
        }


    }

}

