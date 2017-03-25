package com.example.mb7.sportappbp.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mb7.sportappbp.Activity.ActivityDiaryEntry;
import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.Activity.Activity_lst_stimmungsabfrage;
import com.example.mb7.sportappbp.BusinessLayer.TaskCategory;
import com.example.mb7.sportappbp.Fragments.TabFragment;
import com.example.mb7.sportappbp.R;

import java.util.List;

/**
 * Created by M.Braei on 25.03.2017.
 */

public class TaskCategAdapter  extends RecyclerView.Adapter<TaskCategAdapter.TaskCategHolder> {
    List<TaskCategory> taskCategories;
    TabFragment context;
    public class TaskCategHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        ImageView imageView;

        TaskCategHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitleTaskCateg);
            txtSubText = (TextView) itemView.findViewById(R.id.txtSubtextTaskCateg);
            cv = (CardView) itemView.findViewById(R.id.card_view_task_categ);
            imageView = (ImageView) itemView.findViewById(R.id.imgViewTaskCateg);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (taskCategories.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.tagebucheintrag))) {
                Intent open = new Intent(context.getActivity(), ActivityDiaryEntry.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                context.startActivity(open);

            } else if (taskCategories.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.stimmungsabgabe))) {
                Intent open = new Intent(context.getActivity(), Activity_lst_stimmungsabfrage.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                context.startActivity(open);

            } else if (taskCategories.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.fitnessfragebogen))) {
                Intent open = new Intent(context.getActivity(), ActivityFitnessFragebogen.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                context.startActivity(open);
            } else if (taskCategories.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.aktivitaetsfragebogen))) {
                Intent open = new Intent(context.getActivity(), ActivityFragebogen.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                context.startActivity(open);
            } else if (taskCategories.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.bewegen_sie_sich))) {
                Intent open = new Intent(context.getActivity(), ActivityMotivationMessage.class);
                // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                context.startActivity(open);
            }


        }
    }

    public TaskCategAdapter(List<TaskCategory> taskCategories, TabFragment context){
        this.taskCategories = taskCategories;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return taskCategories.size();
    }

    @Override
    public TaskCategAdapter.TaskCategHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_taskcateg, viewGroup, false);
        TaskCategAdapter.TaskCategHolder bvh = new TaskCategAdapter.TaskCategHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final TaskCategAdapter.TaskCategHolder holder, final int position) {
        TaskCategAdapter.TaskCategHolder taskCategHolder = (TaskCategAdapter.TaskCategHolder) holder;
        taskCategHolder.txtTitle.setText(taskCategories.get(position).getTitle());
        taskCategHolder.txtSubText.setText(taskCategories.get(position).getSubText());
        taskCategHolder.imageView.setImageResource(taskCategories.get(position).getImage());


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
