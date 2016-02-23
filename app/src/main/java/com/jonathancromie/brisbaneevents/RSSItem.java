package com.jonathancromie.brisbaneevents;

/**
 * This class handle RSS Item <item> node in rss xml
 * */
public class RSSItem {

    // All <item> node name
    String _title;
    String _link;
    String _address;
    String _date;
    String _booking;
    String _guid;
    String _image;
    String _cost;
    String _meeting_point;
    String _requirements;
    String _description;
    String _time_start;
    String _time_end;

    // constructor
    public RSSItem(){

    }

    // constructor with parameters
    public RSSItem(String title, String link, String address, String date,
                   String booking, String guid, String image, String cost,
                   String meeting_point, String requirements, String description,
                   String time_start, String time_end) {
        this._title = title;
        this._link = link;
        this._address = address;
        this._date = date;
        this._booking = booking;
        this._guid = guid;
        this._image = image;
        this._cost = cost;
        this._meeting_point = meeting_point;
        this._requirements = requirements;
        this._description = description;
        this._time_start = time_start;
        this._time_end = time_end;
    }

    /**
     * All SET methods
     * */
    public void setTitle(String title){
        this._title = title;
    }

    public void setLink(String link){
        this._link = link;
    }

    public void setAddress(String address){
        this._address = address;
    }

    public void setDate(String date){
        this._date = date;
    }

    public void setBooking(String _booking) {
        this._booking = _booking;
    }

    public void setGuid(String guid){
        this._guid = guid;
    }

    public void setImage(String image){
        this._image = image;
    }

    public void setCost(String _cost) {
        this._cost = _cost;
    }

    public void setMeetingPoint(String _meeting_point) {
        this._meeting_point = _meeting_point;
    }

    public void setRequirements(String _requirements) {
        this._requirements = _requirements;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public void setTimeStart(String _time_start) {
        this._time_start = _time_start;
    }

    public void setTimeEnd(String _time_end) {
        this._time_end = _time_end;
    }

    /**
     * All GET methods
     * */
    public String getTitle(){
        return this._title;
    }

    public String getLink(){
        return this._link;
    }

    public String getAddress(){
        return this._address;
    }

    public String getDate(){
        return this._date;
    }

    public String getBooking() {
        return _booking;
    }

    public String getGuid(){
        return this._guid;
    }

    public String getImage(){
        return this._image;
    }

    public String getCost() {
        return _cost;
    }

    public String getMeetingPoint() {
        return _meeting_point;
    }

    public String getRequirements() {
        return _requirements;
    }

    public String getDescription() {
        return _description;
    }

    public String getTimeStart() {
        return _time_start;
    }

    public String getTimeEnd() {
        return _time_end;
    }
}
