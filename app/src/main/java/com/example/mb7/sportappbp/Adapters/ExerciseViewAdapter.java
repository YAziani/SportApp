package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by Basti on 29.01.2017.
 */

public class ExerciseViewAdapter extends BaseAdapter {

    private Activity _context;
    private ArrayList<Exercise> _exercises;

    public ExerciseViewAdapter(Activity context, ArrayList<Exercise> notifications) {
        _exercises = notifications;
        _context = context;
    }

    @Override
    public Object getItem(int position) {
        return _exercises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        Exercise exercise = _exercises.get(position);
        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lst_exercisecell, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtExercise);
        txtTitle.setText(exercise.getName());

        TextView txtText = (TextView) view.findViewById(R.id.txtDuration);
        txtText.setText(String.valueOf(exercise.getTimeHours()) + " " + _context.getResources().getString(R.string
                .Stunden) + " " + exercise.getTimeMunites() + " " + _context.getResources().getString(R.string
                .Minuten));
        return view;
    }

    @Override
    public int getCount() {
        return _exercises.size();
    }
}

