package com.abezukor.abezukor.autosms;

/**
 * Created by abezu on 7/19/2016.
 */
public class autosmsObject {
    //declares object properties
    private int _id;
    private String _homescreenname;
    private String _number;
    private String _message;

    //empty contructor
    public autosmsObject(){}

    //full constructor
    public autosmsObject( String _homescreenname, String _number, String _message) {
        this._homescreenname = _homescreenname;
        this._number = _number;
        this._message = _message;
    }
    //setters and getters
    public int get_id() {
        return _id;
    }

    public String get_homescreenname() {
        return _homescreenname;
    }

    public String get_number() {
        return _number;
    }

    public String get_message() {
        return _message;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_homescreenname(String _homescreenname) {
        this._homescreenname = _homescreenname;
    }

    public void set_number(String _number) {
        this._number = _number;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

}
