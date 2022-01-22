package at.ac.fhcampuswien.simplechattool;

import java.sql.Timestamp;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*
Message currently (16.01.2022) consists of 3 parts:
    Username, Message, AdditionalInformation( -> Prepared for future usage)
 */

public class Message implements Serializable {
    private String username;
    private String Message;
    private String time;
    private String AdditionalInformation;
    public ArrayList<String> user_list = new ArrayList<String>();
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");
    // Internal Message  -> not displayed on GUI
    boolean InternalInformation;    //message received by client if new connection is established - String Message = "myUsername"


    public Message(String username, String text) {
        this.username = username;
        this.Message = text;
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        time = SIMPLE_DATE_FORMAT.format(timestamp);
    }

    public Message(String username, String Message, String AdditionalInformation){
        this.username = username;
        this.Message = Message;
        this.AdditionalInformation = AdditionalInformation;
    }

    public boolean isInternalInformation() {
        return InternalInformation;
    }

    public void setInternalInformation(boolean internalInformation) {
        InternalInformation = internalInformation;
    }

    public String getMessageAsString() {
        return time + ":" + username + ":" + Message + ":" + AdditionalInformation;
    }

    public String getMessage(){
        return "[" + getTime() + " " + getUsername() + "] " + getText();
    }

    public void setUsers(ArrayList<String> users){
        user_list.addAll(users);
    }

    public ArrayList<String> getUsers(){
        return user_list;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setText(String text) {
        this.Message = text;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return Message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdditionalInformation() {
        return AdditionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        AdditionalInformation = additionalInformation;
    }
}