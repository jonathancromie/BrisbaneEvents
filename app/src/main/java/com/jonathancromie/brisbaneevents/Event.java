package com.jonathancromie.brisbaneevents;

/**
 * Created by jonathancromie on 27/02/2016.
 */
public class Event {
    private String title;
    private String address;
    private String date;
    private String booking;
    private String image;
    private String cost;
    private String meeting_point;
    private String requirements;
    private String description;
    private String time_start;
    private String time_end;

    public Event(String address, String booking, String cost, String date, String description,
                 String image, String meeting_point, String requirements,
                 String time_end, String time_start, String title) {
        this.address = address;
        this.booking = booking;
        this.cost = cost;
        this.date = date;
        this.description = description;
        this.image = image;
        this.meeting_point = meeting_point;
        this.requirements = requirements;
        this.time_end = time_end;
        this.time_start = time_start;
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMeeting_point() {
        return meeting_point;
    }

    public void setMeeting_point(String meeting_point) {
        this.meeting_point = meeting_point;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return "Meeting point: " + meeting_point + "\n" +
                "Requirements: " + requirements + "\n" +
                "Bookings: " + booking;
    }
}
