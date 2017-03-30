package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mb7.sportappbp.Activity.ActivityChallenge;
import com.example.mb7.sportappbp.Activity.Activity_lst_Challenge;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian on 25.03.2017.
 */

public class ChallengeLstViewAdapter extends RecyclerView.Adapter<ChallengeLstViewAdapter.ChallengeHolder> {
    List<Challenge> challenges;
    Activity context;
    Integer selectedPosition = -1;

    public class ChallengeHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        ImageView imageView;
        public View view;

        ChallengeHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtDateLstChallenge);
            txtSubText = (TextView) itemView.findViewById(R.id.txtTimeLstChallenge);
            cv = (CardView) itemView.findViewById(R.id.card_view_lstchallenge);
            imageView = (ImageView) itemView.findViewById(R.id.imgViewLstChallenge);
            view = itemView;
        }

    }

    public ChallengeLstViewAdapter(List<Challenge> challenges, Activity context) {
        this.challenges = challenges;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    @Override
    public ChallengeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_lstchallenge, viewGroup, false);
        ChallengeHolder bvh = new ChallengeHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final ChallengeHolder holder, final int position) {
        //set resources of the layput
        ChallengeHolder challengeHolder = (ChallengeHolder) holder;
        challengeHolder.txtTitle.setText(challenges.get(position).getName());
        challengeHolder.txtSubText.setText(getDateOrFinished(challenges.get(position)));
        challengeHolder.imageView.setImageResource(R.mipmap.ic_challenge);

        if (position == selectedPosition) {
            challengeHolder.itemView.setBackgroundColor(Color.GRAY);
        } else {
            challengeHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // reset the previous item as unselected if exists
                if (selectedPosition != -1)
                    notifyItemChanged(selectedPosition);
                // set the new item as selected
                selectedPosition = position;
                notifyItemChanged(selectedPosition);


                return false;
            }

        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if an item is longclicked -> exit longcligck state
                if (selectedPosition != -1) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = -1;

                } else {

                    // if nothing is longclicked -> go to the ActivityStimmung of the selected item
                    Intent open = new Intent(context, ActivityChallenge.class);

                    // pass the clicked stimmungsangabe to the activity
                    Challenge challenge = challenges.get(position);
                    open.putExtra(context.getString(R.string.Challenges), challenge);
                    context.startActivity(open);

                }
            }
        });
    }

    public Challenge getSelectedObject() {
        return challenges.get(selectedPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Returns the string "Challenge beendet" if the challenge is finished. Else the end date
     *
     * @param challenge challenge to check if finished
     * @return returns string
     */
    private String getDateOrFinished(Challenge challenge) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

        if (challenge.finished())
            return context.getString(R.string.ChallengeBeendet);
        else
            return context.getString(R.string.EndetAm) + " " + sdf.format(challenge.getEndDate()).toString();
    }

}



