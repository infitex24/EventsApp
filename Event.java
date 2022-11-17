package com.example.events3;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Event {


    //Zmienne przechowujące informacje o wydarzeniu
    private String key,  adminKey, adminName, title, description, longitude, latitude, type, place, groupKey, groupName, dateStartDefaultTimezone, dateEndDefaultTimezone, dateOfCreationDefaultTimezone, limitUsers;
    private Map<String, String> usersRegistered;
    private long minutesToStart, minutesAgoAdded;
    private float distanceToUser;

    public Event()
    {
    }

    public Event(String adminKey, String adminName, String title, String description, String longitude, String latitude, String type, String place, String groupKey, String groupName, String dateStartDefaultTimezone, String dateEndDefaultTimezone, String dateOfCreationDefaultTimezone, String limitUsers, Map<String, String> usersRegistered) {
        this.adminKey = adminKey;
        this.adminName = adminName;
        this.title=title;
        this.description=description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.place = place;
        this.groupKey = groupKey;
        this.groupName = groupName;
        this.dateStartDefaultTimezone = dateStartDefaultTimezone;
        this.dateEndDefaultTimezone = dateEndDefaultTimezone;
        this.dateOfCreationDefaultTimezone = dateOfCreationDefaultTimezone;
        this.limitUsers = limitUsers;
        this.usersRegistered = usersRegistered;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDateStartDefaultTimezone() {
        return dateStartDefaultTimezone;
    }

    public void setDateStartDefaultTimezone(String dateStartDefaultTimezone) {
        this.dateStartDefaultTimezone = dateStartDefaultTimezone;
    }

    public String getDateEndDefaultTimezone() {
        return dateEndDefaultTimezone;
    }

    public void setDateEndDefaultTimezone(String dateEndDefaultTimezone) {
        this.dateEndDefaultTimezone = dateEndDefaultTimezone;
    }

    public String getDateOfCreationDefaultTimezone() {
        return dateOfCreationDefaultTimezone;
    }

    public void setDateOfCreationDefaultTimezone(String dateOfCreationDefaultTimezone) {
        this.dateOfCreationDefaultTimezone = dateOfCreationDefaultTimezone;
    }

    public String getLimitUsers() {
        return limitUsers;
    }

    public void setLimitUsers(String limitUsers) {
        this.limitUsers = limitUsers;
    }

    public Map<String, String> getUsersRegistered() {
        return usersRegistered;
    }

    public void setUsersRegistered(Map<String, String> usersRegistered) {
        this.usersRegistered = usersRegistered;
    }

    public long getMinutesToStart() {
        return minutesToStart;
    }

    public void setMinutesToStart(long minutesToStart) {
        this.minutesToStart = minutesToStart;
    }

    public long getMinutesAgoAdded() {
        return minutesAgoAdded;
    }

    public void setMinutesAgoAdded(long minutesAgoAdded) {
        this.minutesAgoAdded = minutesAgoAdded;
    }

    public float getDistanceToUser() {
        return distanceToUser;
    }

    public void setDistanceToUser(float distanceToUser) {
        this.distanceToUser = distanceToUser;
    }
}
