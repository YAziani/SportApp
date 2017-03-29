package com.example.mb7.sportappbp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.github.mikephil.charting.charts.LineChart;

import java.security.InvalidParameterException;
import java.util.Date;

public class Activity_Differenz_rpt extends Activity_LineChartReports {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__differenz_rpt);
        lineChart = (LineChart) findViewById(R.id.lineChart);
    }


    // hook method of our template method computeDrawDiff
    @Override
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
                computeDrawDiff(addDays(new Date(), -30), new Date(), lineChart, "Score Stimmungsbarometer",
                        Activity_Differenz_rpt.this.getString(R.string.differenz_der_werte));
                return true;
            case R.id.fourteendays:
                computeDrawDiff(addDays(new Date(), -14), new Date(), lineChart, "Score Stimmungsbarometer",
                        Activity_Differenz_rpt.this.getString(R.string.differenz_der_werte));
                return true;
            case R.id.sevendays:
                computeDrawDiff(addDays(new Date(), -7), new Date(), lineChart, "Score Stimmungsbarometer",
                        Activity_Differenz_rpt.this.getString(R.string.differenz_der_werte));
                return true;
            case R.id.alldays:
                computeDrawDiff(null, new Date(), lineChart, "Score Stimmungsbarometer", Activity_Differenz_rpt.this
                        .getString(R.string.differenz_der_werte));
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        computeDrawDiff(addDays(new Date(), -7), new Date(), lineChart, "Score Stimmungsbarometer",
                Activity_Differenz_rpt.this.getString(R.string.differenz_der_werte));
    }


}
