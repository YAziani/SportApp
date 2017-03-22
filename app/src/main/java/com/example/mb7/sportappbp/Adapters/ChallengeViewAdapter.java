package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by Basti on 21.03.2017.
 */

public class ChallengeViewAdapter extends BaseAdapter {

    private Activity _context;
    private ArrayList<User> _users;

    public ChallengeViewAdapter(Activity context, ArrayList<User> users)
    {
        _users = users;
        _context = context;
    }

    @Override
    public Object getItem(int position) {
        return _users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        User user = _users.get(position);
        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lst_challenge_cell, null);

        TextView txtPos = (TextView) view.findViewById(R.id.textViewChallengePos);
        txtPos.setText(String.valueOf(position + 1));

        TextView txtName = (TextView) view.findViewById(R.id.textViewChallengeName);
        txtName.setText(user.getName());

        TextView txtPoints = (TextView) view.findViewById(R.id.textViewChallengePoints);
        txtPoints.setText(user.getPoints());

        return view;
    }

    @Override
    public int getCount() {
        return _users.size();
    }
}
