package com.example.mb7.sportappbp.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mb7.sportappbp.Fragments.TbNotificationContent;
import com.example.mb7.sportappbp.Fragments.TbReportContent;
import com.example.mb7.sportappbp.Fragments.TbTaskCategContent;

import java.util.ArrayList;

/**
 * Created by M.Braei on 07.01.2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    TbNotificationContent tbNotificationContent = null;
    TbTaskCategContent tbTaskCategContent = null;
    TbReportContent tbReportContent = null;

    ArrayList<String> csTabsList = null;
    int mNumOfTabs;
    Context csContext;

    public PagerAdapter(FragmentManager fm, Context cxt, ArrayList<String> tbs) {
        super(fm);
        csTabsList = tbs;
        this.mNumOfTabs = csTabsList.size();
        csContext = cxt;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {


        Fragment fragment = null;

        if (csTabsList != null) {
            String tab = csTabsList.get(position);
            if (tab.equalsIgnoreCase("tbTask")) {
                if (tbTaskCategContent == null)
                    tbTaskCategContent = new TbTaskCategContent();
                fragment = tbTaskCategContent;
            }

            if (tab.equalsIgnoreCase("tbNotification")) {
                if (tbNotificationContent == null)
                    tbNotificationContent = new TbNotificationContent();
                fragment = tbNotificationContent;
            }
            if (tab.equalsIgnoreCase("tbReport")) {
                if (tbReportContent == null)
                    tbReportContent = new TbReportContent();
                fragment = tbReportContent;

            }

        }

        return fragment;
    }
}
