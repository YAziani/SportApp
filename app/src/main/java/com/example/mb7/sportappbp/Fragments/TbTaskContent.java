package com.example.mb7.sportappbp.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mb7.sportappbp.R;

/**
 * Created by MB7 on 07.01.2017.
 */

public class TbTaskContent extends TabFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setTitle("Tätigkeiten");
        return inflater.inflate(R.layout.tbtaskcontent, container, false);
    }

}
