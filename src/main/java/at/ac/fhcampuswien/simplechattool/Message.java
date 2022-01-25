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
    private final String username;
    private String Message;
    private String time;
    private ArrayList<String> user_list = new ArrayList<>();
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");
    private boolean InternalInformation;    // internal message received by client if new connection is established - String Message = "myUsername" (not displayed on GUI)

    public Message(String username, String text) {
        this.username = username;
        this.Message = text;
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        time = SIMPLE_DATE_FORMAT.format(timestamp);
    }

    public Message(String username, String message, String AdditionalInformation){
        this.username = username;
        this.Message = message;
    }

    public boolean getInternalInformation() {
        return InternalInformation;
    }

    public void setInternalInformation(boolean internalInformation) {
        this.InternalInformation = internalInformation;
    }

    public String getMessage(){
        return "[" + getTime() + " " + getUsername() + "] " + getText();
    }

    public void setUsers(ArrayList<String> users){
        this.user_list = users;
        System.out.println("User list of Message objects: " + user_list);
    }

    public ArrayList<String> getUsers(){
        return user_list;
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

}