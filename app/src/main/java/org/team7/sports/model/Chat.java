package org.team7.sports.model;

/**
 * Proudly created by zhangxinye on 25/10/17.
 */

public class Chat {
    private long lastTime;
    private String latestMessage;

    public Chat(long lastTime, String latestMessage) {
        this.lastTime = lastTime;
        this.latestMessage = latestMessage;
    }

    public Chat() {
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }
}
