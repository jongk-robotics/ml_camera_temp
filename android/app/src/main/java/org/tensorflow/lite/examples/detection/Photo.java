package org.tensorflow.lite.examples.detection;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Photo {
    private String url;
    private String userEmail;
    private Timestamp timeStamp;
    private String locationName;
    private String memo = "";

    private ArrayList<String> friends = new ArrayList<>();

    private GeoPoint location;

    private boolean isLiked = false;
    private boolean isShared = false;
    private boolean isPeople = false;

    Photo()
    {

    }


    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setFriends(ArrayList<String> friends)
    {
        this.friends = friends;
    }

    public void setTimeStamp(Timestamp timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public void setLocation(GeoPoint location)
    {
        this.location = location;
    }

    public void setIsLiked(boolean isLiked)
    {
        this.isLiked = isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public void setPeople(boolean people) {
        isPeople = people;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUrl() {
        return url;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getMemo() {
        return memo;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean getPeolple()
    {
        return isPeople;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("url", url);
        result.put("userEmail", userEmail);
        result.put("friends", friends);
        result.put("timeStamp", timeStamp);
        result.put("location", location);
        result.put("locationName", locationName);
        result.put("memo", memo);
        result.put("isLiked", isLiked);
        result.put("isShared", isShared);
        result.put("isPeople", isPeople);

        return result;
    }

}
