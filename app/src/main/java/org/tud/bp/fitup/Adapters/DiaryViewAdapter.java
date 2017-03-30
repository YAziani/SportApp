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

import com.tud.bp.fitup.Activity.ActivityDiaryEntry;
import com.tud.bp.fitup.BusinessLayer.DiaryEntry;
import com.tud.bp.fitup.R;

import java.util.List;

/**
 * Created by M.Braei on 25.03.2017.
 */

public class DiaryViewAdapter extends RecyclerView.Adapter<DiaryViewAdapter.DiaryEntryHolder> {

    List<DiaryEntry> diaryEntries;
    Activity context;
    Integer selectedPosition = -1;

    public class DiaryEntryHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        ImageView imageView;
        public View view;

        DiaryEntryHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtDateDiary);
            txtSubText = (TextView) itemView.findViewById(R.id.txtTimeDiary);
            cv = (CardView) itemView.findViewById(R.id.card_view_diary);
            imageView = (ImageView) itemView.findViewById(R.id.imgViewDiary);
            view = itemView;
        }

    }

    public DiaryViewAdapter(List<DiaryEntry> diaryEntries, Activity context) {
        this.diaryEntries = diaryEntries;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return diaryEntries.size();
    }

    @Override
    public DiaryEntryHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //// TODO: 26.03.2017  
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_diary, viewGroup, false);
        DiaryEntryHolder bvh = new DiaryEntryHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final DiaryEntryHolder holder, final int position) {
        DiaryEntryHolder diaryEntryHolder = (DiaryEntryHolder) holder;
        diaryEntryHolder.txtTitle.setText(diaryEntries.get(position).sDate);
        diaryEntryHolder.txtSubText.setText(diaryEntries.get(position).sTime);
        diaryEntryHolder.imageView.setImageResource(R.mipmap.ic_tagebuch_eintrag);

        if (position == selectedPosition) {
            diaryEntryHolder.itemView.setBackgroundColor(Color.GRAY);
        } else {
            diaryEntryHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
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
                    //send the information of the selected item to the activity
                    Intent open = new Intent(context, ActivityDiaryEntry.class);
                    DiaryEntry diaryEntry = diaryEntries.get(position);
                    open.putParcelableArrayListExtra("oldExercises", diaryEntry.getExerciseList());
                    open.putExtra("date", diaryEntry.getDate());
                    context.startActivity(open);
                }

            }
        });
    }

    public DiaryEntry getSelectedObject() {
        return diaryEntries.get(selectedPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}



