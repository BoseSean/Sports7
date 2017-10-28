package org.team7.sports.model;

/**
 * Proudly created by zhangxinye on 23/10/17.
 */

public class Message {
    private String message;
    private String sender;
    private long  time;

//    private String from;

    public Message(){

    }

//    public Message(String from) {
//        this.from = from;
//    }

    public Message(String message, String sender, long time) {
        this.message = message;
        this.sender = sender;
        this.time = time;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
