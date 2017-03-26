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

import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.R;
import java.util.List;

/**
 * Created by Felix on 26.03.2017.
 */

public class FitnessFrageViewAdapter extends RecyclerView.Adapter<FitnessFrageViewAdapter.FitnessFrageHolder>{
    List<FitnessFragebogen> fitnessFragebogenList;
    Activity context;
    Integer selectedPosition =-1;

    public  class FitnessFrageHolder extends RecyclerView.ViewHolder  {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        ImageView imageView;
        public View view   ;
        FitnessFrageHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtDateFitnessfragebogen);
            //txtSubText = (TextView) itemView.findViewById(R.id.txtTimeFitnessfragebogen);
            cv = (CardView) itemView.findViewById(R.id.card_view_fitnessfragebogen);
            imageView = (ImageView) itemView.findViewById(R.id.imgViewFitnessfragebogen);
            view = itemView;
        }

    }

    public FitnessFrageViewAdapter(List<FitnessFragebogen> fitnessFragebogenList, Activity context){
        this.fitnessFragebogenList=fitnessFragebogenList;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return fitnessFragebogenList.size();
    }

    @Override
    public FitnessFrageViewAdapter.FitnessFrageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_fitnessfragebogen, viewGroup, false);
        FitnessFrageViewAdapter.FitnessFrageHolder bvh = new FitnessFrageViewAdapter.FitnessFrageHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final FitnessFrageViewAdapter.FitnessFrageHolder holder, final int position) {
        FitnessFrageViewAdapter.FitnessFrageHolder fitnessFrageHolder = (FitnessFrageViewAdapter.FitnessFrageHolder) holder;
        fitnessFrageHolder.txtTitle.setText(fitnessFragebogenList.get(position).Date);

        //fitnessFrageHolder.txtSubText.setText(fitnessFragebogenList.get(position).Time   );
        fitnessFrageHolder.imageView.setImageResource(R.mipmap.ic_fitness_fragebogen);

        if (position == selectedPosition)
        {
            fitnessFrageHolder.itemView.setBackgroundColor(Color.GRAY);
        }
        else
        {
            fitnessFrageHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.view.setOnLongClickListener(new View.OnLongClickListener(){
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
        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // if an item is longclicked -> exit longcligck state
                if (selectedPosition != -1) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = -1;

                }
                else {
                    // if nothing is longclicked -> go to the ActivityStimmung of the selected item
                    Intent open = new Intent(context, ActivityFitnessFragebogen.class);
                    /*
                    // insert the date of the notificatino in the extra which is the unique field to delete the notification from the database
                    if (fitnessFragebogenList.get(position).Vor)
                        open.putExtra("Vor", "1");
                    else
                        open.putExtra("Vor", "0");
                    */
                    // pass the clicked Fitnessfragebogen to the activity
                    FitnessFragebogen fitnessFragebogen = fitnessFragebogenList.get(position);
                    open.putExtra(context.getString(R.string.fitnessfragebogen), fitnessFragebogen);
                    context.startActivity(open);
                }
            }
        });
    }

    public FitnessFragebogen getSelectedObject(){return fitnessFragebogenList.get(selectedPosition);}

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
