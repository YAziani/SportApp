package com.example.mb7.sportappbp.Adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Activity.ActivityDiaryEntry;
import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.Fragments.TabFragment;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;

import java.util.List;

/**
 * Created by M.Braei on 18.03.2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    List<Notification> notifications;
    TabFragment context;
    public  class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        TextView txtDateDiff;
        ImageView imageView;
        NotificationHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubText = (TextView) itemView.findViewById(R.id.txtSubtext);
            txtDateDiff = (TextView) itemView.findViewById(R.id.txtDate);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewNotification);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (notifications.get(getAdapterPosition()).getTitle().equals("Trainingeintrag")) {
                Intent open = new Intent(context.getActivity(), ActivityDiaryEntry.class);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals("Stimmungsabfrage")) {
                Intent open = new Intent(context.getActivity(), ActivityStimmungsAbgabe.class);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals("Fitnessfragebogen")) {
                Intent open = new Intent(context.getActivity(), ActivityFitnessFragebogen.class);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals("Fragebogen zur Aktivität")) {
                Intent open = new Intent(context.getActivity(), ActivityFragebogen.class);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals("Bewegen Sie sich!")) {
                Intent open = new Intent(context.getActivity(), ActivityMotivationMessage.class);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals("Nächstes Training")) {
                String nextTrainingTime = PreferenceManager
                        .getDefaultSharedPreferences(ActivityMain.activityMain
                                .getApplicationContext()).getString("nextTrainingTime","");
                if(!nextTrainingTime.equals("")) {
                    Toast.makeText(
                            ActivityMain.activityMain,
                            "Ihr Training beginnt in "
                                    + MotivationMethod.timeTillTraining(nextTrainingTime)
                                    + " Minuten.",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }

            // remove the notification that has been read
            notifications.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(),notifications.size());
        }
    }

    public NotificationAdapter(List<Notification> notifications, TabFragment context){
        this.notifications = notifications;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_notification, viewGroup, false);
        NotificationHolder bvh = new NotificationHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final NotificationHolder holder, final int position) {
        NotificationHolder notificationHolder = (NotificationHolder) holder;
        notificationHolder.txtTitle.setText(notifications.get(position).getTitle());
        notificationHolder.txtSubText.setText(notifications.get(position).getSubText());
        notificationHolder.txtDateDiff.setText(notifications.get(position).getPresentationDate());
        notificationHolder.imageView.setImageResource(notifications.get(position).getImage());


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
