package com.tud.bp.fitup.Adapters;

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

import com.tud.bp.fitup.Activity.ActivityStimmungsAbgabe;
import com.tud.bp.fitup.BusinessLayer.StimmungsAngabe;
import com.tud.bp.fitup.R;

import java.util.List;

/**
 * Created by M.Braei on 25.03.2017.
 */

public class StimmungsAngabeViewAdapter extends RecyclerView.Adapter<StimmungsAngabeViewAdapter.StimmungsAngabeHolder> {
    List<StimmungsAngabe> stimmungsAngaben;
    Activity context;
    Integer selectedPosition = -1;

    public class StimmungsAngabeHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        ImageView imageView;
        public View view;

        StimmungsAngabeHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtDateStmAbfrage);
            txtSubText = (TextView) itemView.findViewById(R.id.txtTimeStmAbfrage);
            cv = (CardView) itemView.findViewById(R.id.card_view_stmAbgabe);
            imageView = (ImageView) itemView.findViewById(R.id.imgViewstimmungabgabe);
            view = itemView;
        }

    }

    public StimmungsAngabeViewAdapter(List<StimmungsAngabe> stimmungsAngaben, Activity context) {
        this.stimmungsAngaben = stimmungsAngaben;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return stimmungsAngaben.size();
    }

    @Override
    public StimmungsAngabeViewAdapter.StimmungsAngabeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_stimmungabgabe, viewGroup,
                false);
        StimmungsAngabeViewAdapter.StimmungsAngabeHolder bvh = new StimmungsAngabeViewAdapter.StimmungsAngabeHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final StimmungsAngabeViewAdapter.StimmungsAngabeHolder holder, final int position) {
        StimmungsAngabeViewAdapter.StimmungsAngabeHolder stimmungsAngabeHolder = (StimmungsAngabeViewAdapter
                .StimmungsAngabeHolder) holder;
        stimmungsAngabeHolder.txtTitle.setText(stimmungsAngaben.get(position).Date);
        stimmungsAngabeHolder.txtSubText.setText(stimmungsAngaben.get(position).Time);
        stimmungsAngabeHolder.imageView.setImageResource(R.mipmap.ic_stimmungs_abgabe);

        if (position == selectedPosition) {
            stimmungsAngabeHolder.itemView.setBackgroundColor(Color.GRAY);
        } else {
            stimmungsAngabeHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
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
                    Intent open = new Intent(context, ActivityStimmungsAbgabe.class);
                    // insert the date of the notificatino in the extra which is the unique field to delete the
                    // notification from the database
                    if (stimmungsAngaben.get(position).Vor)
                        open.putExtra("Vor", "1");
                    else
                        open.putExtra("Vor", "0");

                    // pass the clicked stimmungsangabe to the activity
                    StimmungsAngabe stimmungsAngabe = stimmungsAngaben.get(position);
                    open.putExtra(context.getString(R.string.stimmungsabgabe), stimmungsAngabe);
                    context.startActivity(open);
                }
            }
        });
    }

    public StimmungsAngabe getSelectedObject() {
        return stimmungsAngaben.get(selectedPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}



