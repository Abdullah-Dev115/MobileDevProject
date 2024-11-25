package com.example.project;

public class Event {
    private int id;
    private String title;
    private String description;
    private String timestamp;

    // Constructor
    public Event(){}
    public Event(int id, String title, String description, String timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Overloaded Constructor for new Events (without ID)
    public Event(String title, String description, String timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
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
}
