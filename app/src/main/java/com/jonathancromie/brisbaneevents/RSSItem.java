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
    String _guid;
    String _image;

    // constructor
    public RSSItem(){

    }

    // constructor with parameters
    public RSSItem(String title, String link, String address, String date, String guid, String image){
        this._title = title;
        this._link = link;
        this._address = address;
        this._date = date;
        this._guid = guid;
        this._image = image;
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


    public void setGuid(String guid){
        this._guid = guid;
    }

    public void setImage(String image){
        this._image = image;
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

    public String getGuid(){
        return this._guid;
    }

    public String getImage(){
        return this._image;
    }
}
