package com.tud.bp.fitup.Adapters;

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

import com.tud.bp.fitup.Activity.ActivityDiaryEntry;
import com.tud.bp.fitup.Activity.ActivityFitnessFragebogen;
import com.tud.bp.fitup.Activity.ActivityFragebogen;
import com.tud.bp.fitup.Activity.ActivityMotivationMessage;
import com.tud.bp.fitup.Activity.ActivityStimmungsAbgabe;
import com.tud.bp.fitup.Activity.Activity_lst_Challenge;
import com.tud.bp.fitup.BusinessLayer.Notification;
import com.tud.bp.fitup.DataAccessLayer.DAL_Utilities;
import com.tud.bp.fitup.Fragments.TabFragment;
import com.tud.bp.fitup.Observe.Observer;
import com.tud.bp.fitup.R;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by M.Braei on 18.03.2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    List<Notification> notifications;
    TabFragment context;

    public class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            // insert the date of the notificatino in the extra which is the unique field to delete the
            // notification from the database
            if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.tagebucheintrag)
            )) {
                Intent open = new Intent(context.getActivity(), ActivityDiaryEntry.class);
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get
                        (getAdapterPosition()).getDate());
                open.putExtra("NotificationDate", NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string
                    .stimmungsabgabe))) {
                Intent open = new Intent(context.getActivity(), ActivityStimmungsAbgabe.class);
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get
                        (getAdapterPosition()).getDate());
                if (notifications.get(getAdapterPosition()).getSubText().equals(context.getString(R.string
                        .ntf_stimmungsabgabe)))
                    open.putExtra("Vor", "1");
                else
                    open.putExtra("Vor", "0");
                open.putExtra("NotificationDate", NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string
                    .fitnessfragebogen))) {
                Intent open = new Intent(context.getActivity(), ActivityFitnessFragebogen.class);
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get
                        (getAdapterPosition()).getDate());
                open.putExtra("NotificationDate", NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string
                    .aktivitaetsfragebogen))) {
                Intent open = new Intent(context.getActivity(), ActivityFragebogen.class);
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get
                        (getAdapterPosition()).getDate());
                open.putExtra("NotificationDate", NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string
                    .bewegen_sie_sich))) {
                Intent open = new Intent(context.getActivity(), ActivityMotivationMessage.class);
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get
                        (getAdapterPosition()).getDate());
                open.putExtra("NotificationDate", NotificationDate);
                context.startActivity(open);
            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string
                    .trNotiTitle))) {
                String nextTrainingTime = PreferenceManager
                        .getDefaultSharedPreferences(context.getContext()).getString("nextTrainingTime", "");
                if (!nextTrainingTime.equals("") && Observer.timeTillTraining(nextTrainingTime) > 0) {
                    Toast.makeText(
                            context.getContext(),
                            context.getString(R.string.ihr_train_begin_in)
                                    + " " + Observer.timeTillTraining(nextTrainingTime)
                                    + " " + context.getString(R.string.minuten) + ".",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            } else if (notifications.get(getAdapterPosition()).getTitle().equals(context.getString(R.string
                    .Challenge))) {
                Intent open = new Intent(context.getActivity(), Activity_lst_Challenge.class);
                String NotificationDate = DAL_Utilities.ConvertDateTimeToFirebaseString(notifications.get
                        (getAdapterPosition()).getDate());
                open.putExtra("NotificationDate", NotificationDate);
                context.startActivity(open);
            }
            else
            {
                throw new InvalidParameterException("Item not declare in Notification Adapter");
            }

        }
    }

    public NotificationAdapter(List<Notification> notifications, TabFragment context) {
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
