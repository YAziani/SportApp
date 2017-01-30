package com.example.mb7.sportappbp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.Objects.Exercise;

import java.util.ArrayList;

/**
 * Created by Basti on 29.01.2017.
 */

public class ExerciseViewAdapter extends BaseAdapter{

    private Activity _context;
    private ArrayList<Exercise> _exercises;

    public ExerciseViewAdapter(Activity context, ArrayList<Exercise> notifications)
    {
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

        TextView txtTitle =(TextView) view.findViewById(R.id.txtExercise);
        txtTitle.setText(exercise.getActivity());

        TextView txtText = (TextView) view.findViewById(R.id.txtDuration);
        txtText.setText(String.valueOf(exercise.getTimeHours()) + " Stunden " + exercise.getTimeMunites() + " Minuten");
        return view;
    }

    @Override
    public int getCount() {
        return _exercises.size();
    }
}

