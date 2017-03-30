package com.tud.bp.fitup.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tud.bp.fitup.Activity.Activity_Differenz_rpt;
import com.tud.bp.fitup.Activity.Activity_EnergieIndex_rpt;
import com.tud.bp.fitup.Activity.Activity_FitnessFragebogen_gesamt_rpt;
import com.tud.bp.fitup.Activity.Activity_FitnessFragebogen_rpt;
import com.tud.bp.fitup.Activity.Activity_Stimmungsbarometer_rpt;
import com.tud.bp.fitup.Activity.Activity_Trainings_rpt;
import com.tud.bp.fitup.Activity.Activity_bsa_gesamt_rpt;
import com.tud.bp.fitup.Activity.Activity_bsa_rpt;
import com.tud.bp.fitup.BusinessLayer.Report;
import com.tud.bp.fitup.Fragments.TabFragment;
import com.tud.bp.fitup.R;
import java.util.List;


/**
 * Created by M.Braei on 27.03.2017.
 */

public class ReportAdapter  extends RecyclerView.Adapter<ReportAdapter.ReportHolder> {
    List<Report> reports;
    TabFragment context;

    public class ReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView txtTitle;
        TextView txtSubText;
        ImageView imageView;

        ReportHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitleReport);
            txtSubText = (TextView) itemView.findViewById(R.id.txtSubtextReport);
            cv = (CardView) itemView.findViewById(R.id.card_view_Reports);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewReports);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.stimmungsbarometer_dgr_ttl))){
                Intent open = new Intent(context.getActivity(),Activity_Stimmungsbarometer_rpt.class                );
                context.startActivity(open);
            }
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.energieindex_dgr_ttl))){
                Intent open = new Intent(context.getActivity(),Activity_EnergieIndex_rpt.class                );
                context.startActivity(open);
            }
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.differenzwert_dgr_ttl))){
                Intent open = new Intent(context.getActivity(),Activity_Differenz_rpt.class                );
                context.startActivity(open);
            }
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.punkteproWoche_dgr_ttl))){
                Intent open = new Intent(context.getActivity(),Activity_Trainings_rpt.class                );
                context.startActivity(open);
            }
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.bsa_dgr_ttl))){
                Intent open = new Intent(context.getActivity(),Activity_bsa_rpt.class                );
                context.startActivity(open);
            }
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.bsa_dgr_ttl_gesamt))){
                Intent open = new Intent(context.getActivity(),Activity_bsa_gesamt_rpt.class                );
                context.startActivity(open);
            }
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.fitnessfragen_dgr_ttl))){
                Intent open = new Intent(context.getActivity(),Activity_FitnessFragebogen_rpt.class                );
                context.startActivity(open);
            }
            if(reports.get(getAdapterPosition()).getTitle().equals(context.getString(R.string.fitnessfragen_dgr_ttl_gesamt))){
                Intent open = new Intent(context.getActivity(),Activity_FitnessFragebogen_gesamt_rpt.class                );
                context.startActivity(open);
            }
        }
    }



    public ReportAdapter(List<Report> reports, TabFragment context) {
        this.reports = reports;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    @Override
    public ReportAdapter.ReportHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_reports, viewGroup, false);
        ReportAdapter.ReportHolder bvh = new ReportAdapter.ReportHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final ReportAdapter.ReportHolder holder, final int position) {
        ReportAdapter.ReportHolder notificationHolder = (ReportAdapter.ReportHolder) holder;
        notificationHolder.txtTitle.setText(reports.get(position).getTitle());
        notificationHolder.txtSubText.setText(reports.get(position).getSubText());
        notificationHolder.imageView.setImageResource(reports.get(position).getImage());


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
