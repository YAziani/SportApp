package com.example.mb7.sportappbp.Fragments;


import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by MB7 on 08.01.2017.
 */

public class TabFragment extends Fragment {


    private String title;
    private Activity activity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title1) {
        title = title1;
    }

    public void Initialize(Activity act, String t) {
        activity = act;
        title = t;
    }
}
