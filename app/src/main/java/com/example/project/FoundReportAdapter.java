package com.example.project;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoundReportAdapter extends RecyclerView.Adapter<FoundReportAdapter.ViewHolder> {
    private List<Report> reportList;
    private Context context;

    public FoundReportAdapter(Context context) {
        this.context = context;
        this.reportList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_found_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = reportList.get(position);

        holder.titleText.setText(report.getTitle());
        holder.descriptionText.setText(report.getDescription());
        holder.locationText.setText(report.getLocation());
        holder.finderContactText.setText("Finder Contact: " + report.getContactInfo());

        ImageUtils.loadImage(context, report.getImageUrl(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public void setReports(List<Report> reports) {
        this.reportList = reports;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText;
        TextView locationText;
        TextView descriptionText;
        TextView finderContactText;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.report_image);
            titleText = itemView.findViewById(R.id.report_title);
            locationText = itemView.findViewById(R.id.report_location);
            descriptionText = itemView.findViewById(R.id.report_description);
            finderContactText = itemView.findViewById(R.id.finder_contact);
        }
    }
}