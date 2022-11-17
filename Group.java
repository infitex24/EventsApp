package com.example.events3;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Group {

    //Zmienne przechowujące informacje o grupie
    private String key,  adminKey, adminName, name, description, dateOfCreationDefaultTimezone, longitude, latitude;
    private float distanceToUser;

    private Map<String, String> memberList;
    private List<Event> eventList;

    Group() {

    }

    Group(String adminKey, String adminName, String name, String description, String dateOfCreationDefaultTimezone, String longitude, String latitude, Map<String, String> memberList)
    {
        this.adminKey = adminKey;
        this.adminName = adminName;
        this.name = name;
        this.description = description;
        this.dateOfCreationDefaultTimezone = dateOfCreationDefaultTimezone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.memberList = memberList;


    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdminKey() {
        return adminKey;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getDateOfCreationDefaultTimezone() {
        return dateOfCreationDefaultTimezone;
    }

    public void setDateOfCreationDefaultTimezone(String dateOfCreationDefaultTimezone) {
        this.dateOfCreationDefaultTimezone = dateOfCreationDefaultTimezone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Map<String, String> getMemberList() {
        return memberList;
    }

    public void setMemberList(Map<String, String> memberList) {
        this.memberList = memberList;
    }

    public void setDistanceToUser(float distanceToUser) {
        this.distanceToUser = distanceToUser;
    }

    public float getDistanceToUser() {
        return distanceToUser;
    }
}
