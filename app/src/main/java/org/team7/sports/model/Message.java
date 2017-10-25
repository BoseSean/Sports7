package org.team7.sports.model;

/**
 * Proudly created by zhangxinye on 23/10/17.
 */

public class Message {
    private String message;
    private long  time;

//    private String from;

    public Message(){

    }

//    public Message(String from) {
//        this.from = from;
//    }

    public Message(String message, long time) {
        this.message = message;
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

}
