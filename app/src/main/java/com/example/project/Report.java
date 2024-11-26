package com.example.project;

public class Report {
    private long id;
    private String title;
    private String description;
    private String location;
    private String imageUrl;
    private String category;
    private long timestamp;

    // Empty constructor required for SQLite
    public Report() {
    }

    public Report(long id, String title, String description, String location, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}