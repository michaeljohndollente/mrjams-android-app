package com.example.mrjams.ui.announcement;

public class Announcement {

    String date, message;

    public Announcement(String date, String message) {
        this.date = date;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
