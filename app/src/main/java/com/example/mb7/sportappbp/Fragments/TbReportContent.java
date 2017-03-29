package com.example.mb7.sportappbp.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mb7.sportappbp.Adapters.ReportAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Report;
import com.example.mb7.sportappbp.R;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by M.Braei on 07.01.2017.
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

        // our list of reports that we want to display in the tab each as a cardview in the recyclerview
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
        // we create just two sections to devide the whole reports in them
        reportsStimmung =new LinkedList<Report>(Arrays.asList(r1,r2,r3));
        reportsFitness =new LinkedList<Report>(Arrays.asList(r4,r5,r6,r7,r8));

        // section 1
        rvStimmung = (RecyclerView)view.findViewById(R.id.recycler_report_stimmung);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rvStimmung.setLayoutManager(lm);
        rvStimmung.setAdapter(new ReportAdapter(reportsStimmung, this));

        // section 2
        rvfitness = (RecyclerView)view.findViewById(R.id.recycler_report_other);
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext());
        rvfitness.setLayoutManager(lm2);
        rvfitness.setAdapter(new ReportAdapter(reportsFitness, this));
        return view;
    }
}
