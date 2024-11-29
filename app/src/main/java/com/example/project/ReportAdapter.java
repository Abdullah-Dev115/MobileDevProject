package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

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
        holder.descriptionText.setText(report.getDescription());
        holder.locationText.setText(report.getLocation());

        // Inside your onBindViewHolder method
        holder.foundItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportFoundItemActivity.class);
            intent.putExtra("item_id", report.getId());
            Log.d("checking the type", "The id provided is: " + report.getId() );
            context.startActivity(intent);
        });
        
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText;
        TextView descriptionText;
        TextView locationText;
        Button foundItemButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.report_image);
            titleText = itemView.findViewById(R.id.report_title);
            descriptionText = itemView.findViewById(R.id.report_description);
            locationText = itemView.findViewById(R.id.report_location);
            foundItemButton = itemView.findViewById(R.id.found_item_button);
        }
    }
}