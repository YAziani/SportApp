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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

public class Activity_FitnessFragebogen_rpt extends Activity_LineChartReports {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__fitness_fragebogen_rpt);
        lineChart = (LineChart) findViewById(R.id.lineChart);
    }

    // hook method of our template method computeDrawFive
    float convertValue(DataSnapshot object) {
        // first try with double because it is the default value in firebase
        // otherwise we try it with Long
        try {
            return ((Double) object.getValue()).floatValue();

        } catch (ClassCastException ex) {
            return ((Long) object.getValue()).floatValue();
        }
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
                computeDrawFive(addDays(new Date(), -30), new Date(), lineChart, Activity_FitnessFragebogen_rpt.this
                        .getString(R.string.gesamtscore_fitnessfragebogen));

                return true;
            case R.id.fourteendays:
                computeDrawFive(addDays(new Date(), -14), new Date(), lineChart, Activity_FitnessFragebogen_rpt.this
                        .getString(R.string.gesamtscore_fitnessfragebogen));
                return true;
            case R.id.sevendays:
                computeDrawFive(addDays(new Date(), -7), new Date(), lineChart, Activity_FitnessFragebogen_rpt.this
                        .getString(R.string.gesamtscore_fitnessfragebogen));
                return true;
            case R.id.alldays:
                computeDrawFive(null, new Date(), lineChart,  Activity_FitnessFragebogen_rpt.this
                        .getString(R.string.gesamtscore_fitnessfragebogen));
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        computeDrawFive(addDays(new Date(), -7), new Date(), lineChart, Activity_FitnessFragebogen_rpt.this.getString
                (R.string.gesamtscore_fitnessfragebogen));
    }


    /**
     * @param Startdate  set that start interval for the data. Set this to null if you want all data be displayed
     * @param EndDate
     * @param lineChart
     * @param legend
     */
    protected void computeDrawFive(final Date Startdate, final Date EndDate, final LineChart lineChart, final String
            legend) {
        try {
            // first show the progress dialog
            pd = new ProgressDialog(this);
            pd.setMessage(getString(R.string.wird_geladen));
            pd.show();

            // then run
            final ArrayList<String> xAXES = new ArrayList<>();
            final ArrayList<Entry> yAXES_Ausdauer = new ArrayList<>();
            final ArrayList<Entry> yAXES_Beweglichkeit = new ArrayList<>();
            final ArrayList<Entry> yAXES_Koordination = new ArrayList<>();
            final ArrayList<Entry> yAXES_Kraft = new ArrayList<>();
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/FitnessFragebogen/");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    // Hier kriegst du den Knoten date zurueck
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String x = "";
                                                        float y_Ausdauer = 0;
                                                        float y_Beweglichkeit = 0;
                                                        float y_Koordination = 0;
                                                        float y_Kraft = 0;

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
                                                                    if (child2L.getKey().equals("Score_Ausdauer")) {
                                                                        y_Ausdauer += convertValue(child2L);
                                                                        flag = true;
                                                                    } else if (child2L.getKey().equals
                                                                            ("Score_Beweglichkeit")) {
                                                                        y_Beweglichkeit += convertValue(child2L);
                                                                        flag = true;
                                                                    } else if (child2L.getKey().equals
                                                                            ("Score_Koordination")) {
                                                                        y_Koordination += convertValue(child2L);
                                                                        flag = true;
                                                                    } else if (child2L.getKey().equals("Score_Kraft")) {
                                                                        y_Kraft += convertValue(child2L);
                                                                        flag = true;
                                                                    }
                                                                }

                                                                // get the average of on day
                                                                if (flag) {
                                                                    // if one of them wasn't filled in an iteration,
                                                                    // we consider it like zero! Therefore i as a
                                                                    // devider for all
                                                                    y_Ausdauer = y_Ausdauer / i;
                                                                    y_Beweglichkeit = y_Beweglichkeit / i;
                                                                    y_Koordination = y_Koordination / i;
                                                                    y_Kraft = y_Kraft / i;
                                                                    // Now we have the days
                                                                    x = DAL_Utilities.ConvertDateToString
                                                                            (DAL_Utilities
                                                                                    .ConvertFirebaseKeyStringToDateTime(sDate + " 00:00:00"));
                                                                    //x  = sDate ;
                                                                    max_y = Math.max(y_Kraft, Math.max
                                                                            (y_Koordination, Math.max(y_Ausdauer, Math.max
                                                                                            (y_Beweglichkeit, max_y))
                                                                            ));
                                                                    min_y = Math.min(y_Kraft, Math.min
                                                                            (y_Koordination, Math.min(y_Ausdauer, Math.min
                                                                                            (min_y, y_Beweglichkeit))
                                                                            ));
                                                                    xAXES.add(x);

                                                                    yAXES_Beweglichkeit.add(new Entry
                                                                            (y_Beweglichkeit, j));
                                                                    yAXES_Ausdauer.add(new Entry(y_Ausdauer, j));
                                                                    yAXES_Koordination.add(new Entry(y_Koordination,
                                                                            j));
                                                                    yAXES_Kraft.add(new Entry(y_Kraft, j));


                                                                    flag = false;
                                                                    j++;
                                                                     y_Ausdauer = y_Beweglichkeit =
                                                                            y_Koordination = y_Kraft = 0;
                                                                }
                                                            }
                                                        }
                                                        if (xAXES.size() != 0) {

                                                            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                                                            // set the datasets of our chart
                                                            LineDataSet lineDataSet1 = new LineDataSet
                                                                    (yAXES_Beweglichkeit,
                                                                            Activity_FitnessFragebogen_rpt.this
                                                                                    .getString(R.string.beweglichkeit));
                                                            lineDataSet1.setDrawCircles(true);
                                                            lineDataSet1.setColor(Color.BLUE);

                                                            LineDataSet lineDataSet2 = new LineDataSet
                                                                    (yAXES_Ausdauer, Activity_FitnessFragebogen_rpt
                                                                            .this.getString(R.string.ausdauer));
                                                            lineDataSet2.setDrawCircles(true);
                                                            lineDataSet2.setColor(Color.RED);

                                                            LineDataSet lineDataSet4 = new LineDataSet
                                                                    (yAXES_Koordination,
                                                                            Activity_FitnessFragebogen_rpt.this
                                                                                    .getString(R.string.kooridination));
                                                            lineDataSet4.setDrawCircles(true);
                                                            lineDataSet4.setColor(Color.MAGENTA);

                                                            LineDataSet lineDataSet5 = new LineDataSet(yAXES_Kraft,
                                                                    Activity_FitnessFragebogen_rpt.this.getString(R
                                                                            .string.kraft));
                                                            lineDataSet5.setDrawCircles(true);
                                                            lineDataSet5.setColor(Color.CYAN);

                                                            lineDataSets.add(lineDataSet2);
                                                            lineDataSets.add(lineDataSet1);
                                                            lineDataSets.add(lineDataSet4);
                                                            lineDataSets.add(lineDataSet5);

                                                            lineChart.invalidate();
                                                            // set the visual properties of the chart
                                                            // .AxisDependency.LEFT);
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
                                                            lineChart.getLegend().setEnabled(false);


                                                        }
                                                        // after all the data process has been done
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
