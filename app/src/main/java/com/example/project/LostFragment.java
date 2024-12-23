package com.example.project;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;  // Add this import
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LostFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private DatabaseHandler dbHandler;
    private FloatingActionButton addButton;
    private TextView titleTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lost_fragment, container, false);
        dbHandler = new DatabaseHandler(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.lost_recycler_view);
        addButton = view.findViewById(R.id.add_lost_item_fab);
        titleTextView=view.findViewById(R.id.report_title);

        
        // Set up RecyclerView
        adapter = new ReportAdapter(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        
        // Set up FAB click listener
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddReportActivity.class);

            startActivity(intent);
        });
        
        // Load initial data
        loadLostItems();
    }

    private void loadLostItems() {
        if (dbHandler == null) {
            dbHandler = new DatabaseHandler(requireContext());
        }
        List<Report> reports = dbHandler.getAllLostReports();  // false for unfound items
        adapter.setReports(reports);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLostItems();
    }
}