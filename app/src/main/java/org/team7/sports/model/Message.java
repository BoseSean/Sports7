package org.team7.sports.model;

/**
 * Proudly created by zhangxinye on 23/10/17.
 */

public class Message {
    private String message;
    private long  time;
    private boolean seen;

    private String from;

    public Message(String from) {
        this.from = from;
    }

    public Message(String message, String type, long time, boolean seen) {
        this.message = message;
        this.time = time;
        this.seen = seen;
    }

    public Message(){

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
