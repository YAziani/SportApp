package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Comparator.UserSortPoints;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Basti on 23.03.2017.
 */

public class NewChallengeViewAdapter extends BaseAdapter {

    private Activity _context;
    private ArrayList<User> _users;

    public NewChallengeViewAdapter(Activity context, ArrayList<User> users)
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
            view = _context.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);

        TextView txtPos = (TextView) view.findViewById(android.R.id.text1);
        txtPos.setText(user.getName());

        return view;
    }

    @Override
    public int getCount() {
        return _users.size();
    }
}
