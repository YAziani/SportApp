package com.example.mb7.sportappbp.Adapters;

import android.content.Intent;
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
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.Fragments.TabFragment;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;

import java.util.Date;
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
            if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.tagebucheintrag))) {
                Intent open = new Intent(context.getActivity(), ActivityDiaryEntry.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get(getAdapterPosition()).getDate());
                open.putExtra("NotificationDate",NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.stimmungsabgabe))) {
                Intent open = new Intent(context.getActivity(), ActivityStimmungsAbgabe.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get(getAdapterPosition()).getDate());
                open.putExtra("NotificationDate",NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.fitnessfragebogen))) {
                Intent open = new Intent(context.getActivity(), ActivityFitnessFragebogen.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get(getAdapterPosition()).getDate());
                open.putExtra("NotificationDate",NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.aktivitaetsfragebogen) )) {
                Intent open = new Intent(context.getActivity(), ActivityFragebogen.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get(getAdapterPosition()).getDate());
                open.putExtra("NotificationDate",NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.bewegen_sie_sich))) {
                Intent open = new Intent(context.getActivity(), ActivityMotivationMessage.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get(getAdapterPosition()).getDate());
                open.putExtra("NotificationDate",NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.naechstes_training))) {
                String nextTrainingTime = PreferenceManager
                        .getDefaultSharedPreferences(ActivityMain.activityMain
                                .getApplicationContext()).getString("nextTrainingTime","");
                if(!nextTrainingTime.equals("")) {
                    Toast.makeText(
                            ActivityMain.activityMain,
                            context.getString(R.string.ihr_train_begin_in)
                                    + MotivationMethod.timeTillTraining(nextTrainingTime)
                                    + context.getString(R.string.minuten) + ".",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }

            // remove the notification that has been read
/*            notifications.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(),notifications.size());*/
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