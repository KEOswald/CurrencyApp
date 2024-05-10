package com.techelevator.tenmo.model;

public class RecipientsDTO {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
