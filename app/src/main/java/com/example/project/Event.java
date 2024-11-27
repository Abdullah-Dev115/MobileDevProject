package com.example.project;

public class Event {
    private int id;
    private String title;
    private String description;
    private String timestamp;
    private int adminId;

    // Default constructor
    public Event() {}

    // Constructor for creating new events
    public Event(String title, String description, String timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Constructor with all fields
    public Event(int id, String title, String description, String timestamp, int adminId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.adminId = adminId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
