package org.team7.sports.model;

/**
 * Proudly created by zhangxinye on 5/10/17.
 *
 */

public class Account {

    private String name;
    private String image;
    private int numoffriends;

    public Account(String name, String image, int numoffriends) {
        this.name = name;
        this.image = image;
        this.numoffriends = numoffriends;
    }

    public Account(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumoffriends() {
        return numoffriends;
    }

    public void setNumoffriends(int numoffriends) {
        this.numoffriends = numoffriends;
    }
}
