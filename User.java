package com.example.events3;

import com.google.firebase.database.Exclude;

import java.util.List;

public class User {

    //Zmienne przechowujące informacje o użytkowniku
    private String email, fullName, salt;
    private List<String> groups;

    public User() {
    }

    public User(String fullName, String salt) {
        this.fullName = fullName;
        this.salt = salt;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

}
