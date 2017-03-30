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

import com.tud.bp.fitup.Activity.ActivityFragebogen;
import com.tud.bp.fitup.BusinessLayer.Fragebogen;
import com.tud.bp.fitup.R;

import java.util.List;

/**
 * Created by Felix on 26.03.2017.
 */
public class BsaFragebogenViewAdapter extends RecyclerView.Adapter<BsaFragebogenViewAdapter.FragebogenHolder> {
    List<Fragebogen> fragebogenList;
    Activity context;
    Integer selectedPosition = -1;

    public class FragebogenHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        ImageView imageView;
        public View view;

        /**
         * Instantiates a new Fragebogen holder.
         *
         * @param itemView the item view
         */
        FragebogenHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtDatebsasfragebogen);
            cv = (CardView) itemView.findViewById(R.id.card_view_bsafragebogen);
            imageView = (ImageView) itemView.findViewById(R.id.imgViewBsaFragebogen);
            view = itemView;
        }
    }

    /**
     * Instantiates a new Bsa fragebogen view adapter.
     *
     * @param fragebogenList the fragebogen list
     * @param context the context
     */
    public BsaFragebogenViewAdapter(List<Fragebogen> fragebogenList, Activity context) {
        this.fragebogenList = fragebogenList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return fragebogenList.size();
    }

    @Override
    public BsaFragebogenViewAdapter.FragebogenHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_bsafragebogen, viewGroup, false);
        BsaFragebogenViewAdapter.FragebogenHolder bvh = new BsaFragebogenViewAdapter.FragebogenHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final BsaFragebogenViewAdapter.FragebogenHolder holder, final int position) {
        BsaFragebogenViewAdapter.FragebogenHolder fragebogenHolder = (BsaFragebogenViewAdapter.FragebogenHolder) holder;
        fragebogenHolder.txtTitle.setText(fragebogenList.get(position).Date);
        fragebogenHolder.imageView.setImageResource(R.mipmap.ic_aktivitaets_fragebogen);

        if (position == selectedPosition) {
            fragebogenHolder.itemView.setBackgroundColor(Color.GRAY);
        } else {
            fragebogenHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (selectedPosition != -1)
                    notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);

                return false;
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedPosition != -1) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = -1;
                } else {
                    Intent open = new Intent(context, ActivityFragebogen.class);
                    Fragebogen fragebogen = fragebogenList.get(position);
                    open.putExtra(context.getString(R.string.aktivitaetsfragebogen), fragebogen);
                    context.startActivity(open);
                }
            }
        });
    }

    /**
     * @return the selected object
     */
    public Fragebogen getSelectedObject() {
        return fragebogenList.get(selectedPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
