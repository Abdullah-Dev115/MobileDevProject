package com.example.project;

public class Report {
    private long id;
    private String title;
    private String description;
    private String location;
    private String imageUrl;
    private String contactInfo;
    private boolean isFound;
    private String category;
    private long timestamp;

    // Empty constructor required for SQLite
    public Report() {
    }

    // Constructor for creating a new report
    public Report(long id, String title, String description, String location, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.timestamp = System.currentTimeMillis();
    }

    // Constructor for found items report
    public Report(String title, String location, String description, String contactInfo, String imageUrl, boolean isFound) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.contactInfo = contactInfo;
        this.imageUrl = imageUrl;
        this.isFound = isFound;
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

    // Add getters and setters for new fields
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }
}