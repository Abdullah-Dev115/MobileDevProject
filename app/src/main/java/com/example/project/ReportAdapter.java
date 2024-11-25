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

import com.example.project.R;
import com.example.project.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private List<Report> reportList;
    private Context context;

    public ReportAdapter(Context context) {
        this.context = context;
        this.reportList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = reportList.get(position);

        holder.titleText.setText(report.getTitle());
        holder.locationText.setText(report.getLocation());
        holder.descriptionText.setText(report.getDescription());

        if (report.getImageUrl() != null) {
            try {
                Uri imageUri = Uri.parse(report.getImageUrl());
                holder.imageView.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
                holder.imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.report_image);
            titleText = itemView.findViewById(R.id.report_title);
            locationText = itemView.findViewById(R.id.report_location);
            descriptionText = itemView.findViewById(R.id.report_description);
        }
    }
}