package com.example.collegemanagement;

public class Report {
    private int id;
    private int userId;
    private String userRole;
    private String title;
    private String description;
    private String status;

    public Report(int id, int userId, String userRole, String title, String description, String status) {
        this.id = id;
        this.userId = userId;
        this.userRole = userRole;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getUserRole() { return userRole; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
}
