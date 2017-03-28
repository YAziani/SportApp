package com.example.mb7.sportappbp.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.github.mikephil.charting.charts.LineChart;

import java.util.Date;

public class Activity_EnergieIndex_rpt extends Activity_LineChartReports {

    float convertValue(DataSnapshot object) {
        try{
            return  ((Double) object.getValue()).floatValue();

        }
        catch (ClassCastException ex)
        {
            return ((Long) object.getValue()).floatValue();
        }
        //return 0;
    }

    LineChart lineChart;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity__energie_index_rpt);
            lineChart = (LineChart)findViewById(R.id.lineChart);

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            //set an other menu xml
            inflater.inflate(R.menu.menu_context_report, menu);

            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){


            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {

                case R.id.thirtydays:
                    computeDraw(addDays(new Date(),-30), new Date(),lineChart,"Energieindex",Activity_EnergieIndex_rpt.this.getString(R.string.werte_fuer_energieindex));

                    return true;
                case R.id.fourteendays:
                    computeDraw(addDays(new Date(),-14), new Date(),lineChart,"Energieindex",Activity_EnergieIndex_rpt.this.getString(R.string.werte_fuer_energieindex));
                    return  true;
                case R.id.sevendays:
                    computeDraw(addDays(new Date(),-7), new Date(),lineChart,"Energieindex",Activity_EnergieIndex_rpt.this.getString(R.string.werte_fuer_energieindex));
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }



        @Override
        protected void onStart() {
            super.onStart();
            computeDraw(addDays(new Date(),-4) ,new Date(), lineChart,"Energieindex",Activity_EnergieIndex_rpt.this.getString(R.string.werte_fuer_energieindex));
        }




    }


