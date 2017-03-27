package com.example.mb7.sportappbp.Fragments;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Activity.ActivityDiaryEntry;
import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.Adapters.NotificationViewAdapter;
import com.example.mb7.sportappbp.Adapters.TaskCategAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.BusinessLayer.Task;
import com.example.mb7.sportappbp.BusinessLayer.TaskCategory;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MB7 on 07.01.2017.
 */

public class TbTaskCategContent extends TabFragment {

    View view;
    ListView listView;
    List<TaskCategory> taskCategories;
    RecyclerView rv;

    TbTaskCategContent tbTaskCategContent = null;

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setTitle(getString( R.string.aktivitaeten));
        view = inflater.inflate(R.layout.tbtaskcontent, container, false);

        TaskCategory tc1 = new TaskCategory(getString( R.string.tagebucheintrag),getString(R.string.tagebucheintrag_desc), R.mipmap.ic_tagebuch_eintrag);
        TaskCategory tc2 = new TaskCategory(getString( R.string.stimmungsabgabe), getString( R.string.stimmungsabgabe_desc), R.mipmap.ic_stimmungs_abgabe);
        TaskCategory tc3 = new TaskCategory(getString( R.string.aktivitaetsfragebogen), getString( R.string.aktivitaetsfragebogen_desc),R.mipmap.ic_aktivitaets_fragebogen);
        TaskCategory tc4 = new TaskCategory(getString( R.string.fitnessfragebogen), getString( R.string.fitnessfragebogen_desc),R.mipmap.ic_fittnessfragebogen);
        TaskCategory tc5 = new TaskCategory(getString( R.string.trainingszeiten_und_studioadresse), getString(R.string.wann_und_wo_findet_training_statt),R.mipmap.ic_trainings_zeiten_ort);
        TaskCategory tc6 = new TaskCategory(getString( R.string.Challenges), getString(R.string.wann_und_wo_findet_training_statt),R.mipmap.ic_trainings_zeiten_ort);
        taskCategories =new LinkedList<TaskCategory>(Arrays.asList(tc1,tc2,tc3,tc4,tc5,tc6));

        rv = (RecyclerView)view.findViewById(R.id.recyclerTaskCateg);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rv.setItemAnimator(new DefaultItemAnimator());
        // just create a list of tasks
        rv.setAdapter(new TaskCategAdapter(taskCategories, this));
        tbTaskCategContent = this;
        return view;

    }


    @Override
    public void onStart() {


        super.onStart();

    }

}
