package com.example.mb7.sportappbp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mb7.sportappbp.Activity.ActivityCategories;
import com.example.mb7.sportappbp.Activity.ActivityChallenge;
import com.example.mb7.sportappbp.Activity.ActivityDiary;
import com.example.mb7.sportappbp.Activity.ActivityDiaryEntry;
import com.example.mb7.sportappbp.Activity.ActivityKompass;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityNewChallenge;
import com.example.mb7.sportappbp.Adapters.ReportAdapter;
import com.example.mb7.sportappbp.Adapters.TaskCategAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Report;
import com.example.mb7.sportappbp.BusinessLayer.TaskCategory;
import com.example.mb7.sportappbp.ClientIF;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;
import com.example.mb7.sportappbp.R;
import com.firebase.client.Firebase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by MB7 on 07.01.2017.
 */

public class TbReportContent extends TabFragment {

    View view;
    ListView listView;
    List<Report> reports;
    RecyclerView rv;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setTitle("Berichte");
        view = inflater.inflate(R.layout.tbreportcontent, container, false);

        Report r1 = new Report(getString(R.string.stimmungsbarometer_dgr_ttl), getString(R.string
                .stimmungsbarometer_dgr_text), R.mipmap.ic_report_violett);
        Report r2 = new Report(getString(R.string.energieindex_dgr_ttl), getString(R.string.energieeindex_dgr_text),
                R.mipmap.ic_report_green);
        Report r3 = new Report(getString(R.string.differenzwert_dgr_ttl), getString(R.string.differenzwert_dgr_text),
                R.mipmap.ic_report_orange);
        Report r4 = new Report(getString(R.string.punkteproWoche_dgr_ttl), getString(R.string
                .punkteproWoche_dgr_text), R.mipmap.ic_report_rosa);
        Report r5 = new Report(getString(R.string.fitnessfragen_dgr_ttl), getString(R.string.fittnessfragen_dgr_text)
                , R.mipmap.ic_report_blue);
        Report r6 = new Report(getString(R.string.bsa_dgr_ttl), getString(R.string.bsa_dgr_text), R.mipmap
                .ic_report_red);
        reports = new LinkedList<Report>(Arrays.asList(r1, r2, r3, r4, r5, r6));


        rv = (RecyclerView) view.findViewById(R.id.recycler_report);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        // just create a list of tasks
        rv.setAdapter(new ReportAdapter(reports, this));

        return view;
    }
}
