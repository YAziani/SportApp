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

public class TbReportContent extends TabFragment{

    View view;
    ListView listView;
    List<Report> reportsStimmung;
    List<Report> reportsFitness;
    RecyclerView rvStimmung;
    RecyclerView rvfitness;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setTitle("Berichte");
        view = inflater.inflate(R.layout.tbreportcontent, container, false);

        Report r1 = new Report(getString( R.string.stimmungsbarometer_dgr_ttl),getString(R.string.stimmungsbarometer_dgr_text), R.mipmap.ic_report_violett);
        Report r2 = new Report(getString( R.string.energieindex_dgr_ttl), getString( R.string.energieeindex_dgr_text), R.mipmap.ic_report_green);
        Report r3 = new Report(getString( R.string.differenzwert_dgr_ttl), getString( R.string.differenzwert_dgr_text),R.mipmap.ic_report_orange);
        Report r4 = new Report(getString( R.string.punkteproWoche_dgr_ttl), getString( R.string.punkteproWoche_dgr_text),R.mipmap.ic_report_rosa);
        Report r5 = new Report(getString( R.string.fitnessfragen_dgr_ttl), getString(R.string.fittnessfragen_dgr_text),R.mipmap.ic_report_blue);
        Report r6 = new Report(getString( R.string.fitnessfragen_dgr_ttl_gesamt), getString(R.string
                .fittnessfragen_gsmt_dgr_text),R.mipmap.ic_report_blue_gesamt);
        Report r7 = new Report(getString( R.string.bsa_dgr_ttl), getString(R.string.bsa_dgr_text),R.mipmap
                .ic_report_red);
        Report r8 = new Report(getString( R.string.bsa_dgr_ttl_gesamt), getString(R.string.bsa_gsmt_dgr_text),R.mipmap
                .ic_report_red_gesamt);
        reportsStimmung =new LinkedList<Report>(Arrays.asList(r1,r2,r3));
        reportsFitness =new LinkedList<Report>(Arrays.asList(r4,r5,r6,r7,r8));


        rvStimmung = (RecyclerView)view.findViewById(R.id.recycler_report_stimmung);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rvStimmung.setLayoutManager(lm);
        // just create a list of tasks
        rvStimmung.setAdapter(new ReportAdapter(reportsStimmung, this));

        rvfitness = (RecyclerView)view.findViewById(R.id.recycler_report_other);

        LinearLayoutManager lm2 = new LinearLayoutManager(getContext());
        rvfitness.setLayoutManager(lm2);
        // just create a list of tasks
        rvfitness.setAdapter(new ReportAdapter(reportsFitness, this));
        return view;
    }
}
