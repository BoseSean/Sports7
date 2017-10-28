package org.team7.sports.model;

/**
 * Proudly created by zhangxinye on 5/10/17.
 *
 */

public class Account {

    private String name;
    private String image;

    public Account(String name, String image) {
        this.name = name;
        this.image = image;
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
}
