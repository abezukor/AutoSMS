package com.abezukor.abezukor.autosms;

/**
 * Created by abezu on 7/19/2016.
 */
public class autosmsObject {
    //declares object properties
    private int _id;
    private String _homescreenname;
    private int _number;
    private String _message;

    //empty contructor
    public autosmsObject(){}

    //full constructor
    public autosmsObject( String _homescreenname, int _number, String _message) {
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

    public int get_number() {
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

    public void set_number(int _number) {
        this._number = _number;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

}
