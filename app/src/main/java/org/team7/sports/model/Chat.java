package org.team7.sports.model;

/**
 * Proudly created by zhangxinye on 25/10/17.
 */

public class Chat {
    private long lastTime;
    private String latestMessage;
    private boolean isGroup;

    public Chat(long lastTime, String latestMessage, boolean isGroup) {
        this.lastTime = lastTime;
        this.latestMessage = latestMessage;
        this.isGroup = isGroup;
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

    public boolean getIsGroup() {
        return this.isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }
}
